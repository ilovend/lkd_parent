package com.lkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.lkd.common.VMSystem;
import com.lkd.dao.UserDao;
import com.lkd.entity.UserEntity;
import com.lkd.exception.LogicException;
import com.lkd.feign.VMService;
import com.lkd.http.view.TokenObject;
import com.lkd.http.vo.LoginReq;
import com.lkd.http.vo.LoginResp;
import com.lkd.service.PartnerService;
import com.lkd.service.UserService;
import com.lkd.sms.SmsSender;
import com.lkd.utils.BCrypt;
import com.lkd.utils.JWTUtil;
import com.lkd.vo.Pager;
import com.lkd.vo.UserVO;
import com.lkd.vo.VmVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao,UserEntity> implements UserService{
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private PartnerService partnerService;

    @Autowired
    private SmsSender smsSender;
    @Autowired
    private VMService vmService;
    @Override
    public Integer getOperatorCount() {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getRoleCode,"1002");

        return this.count(wrapper);
    }

    @Override
    public Integer getRepairerCount() {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getRoleCode,"1003");

        return this.count(wrapper);
    }

    @Override
    public Pager<UserEntity> findPage(long pageIndex, long pageSize, String userName,Integer roleId,Boolean isRepair) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserEntity> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex,pageSize);

        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(userName)){
            wrapper.like(UserEntity::getUserName,userName);
        }
        if(roleId != null && roleId > 0){
            wrapper.eq(UserEntity::getRoleId,roleId);
        }
        if(isRepair != null && isRepair == true){
            wrapper.eq(UserEntity::getRoleCode,"1003");
        }
        if(isRepair != null && isRepair == false){
            wrapper.eq(UserEntity::getRoleCode,"1002");
        }
        wrapper.ne(UserEntity::getRoleId,1);
        this.page(page,wrapper);
        page.getRecords().forEach(u->{
            u.setPassword("");
            u.setSecret("");
        });

        return Pager.build(page);
    }

    @Override
    public LoginResp login(LoginReq req) throws IOException {
        if(req.getLoginType() == VMSystem.LOGIN_ADMIN){
            return this.adminLogin(req);
        }else if(req.getLoginType() == VMSystem.LOGIN_EMP){
            return this.empLogin(req);
        }else if(req.getLoginType() == VMSystem.LOGIN_PARTNER){
            return partnerService.login(req);
        }
        LoginResp resp = new LoginResp();
        resp.setSuccess(false);
        resp.setMsg("不存在该账户");

        return resp;
    }



    @Override
    public void sendCode(String mobile){
        //非空校验
        if(Strings.isNullOrEmpty(mobile)) return;

        //查询用户表中是否存在该手机号
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(UserEntity::getMobile,mobile);
        if(this.count(wrapper)<=0) return;  //如果不存在，直接返回
        if(redisTemplate.opsForValue().get(mobile) != null) return;  //避免5分钟内重复发送
        //生成5位短信验证码
        StringBuilder sbCode = new StringBuilder();
        Stream
                .generate(()-> new Random().nextInt(10))
                .limit(5)
                .forEach(x-> sbCode.append(x));
        //将验证码放入redis  ，5分钟过期
        redisTemplate.opsForValue().set(mobile,sbCode.toString(), Duration.ofMinutes(5));
        log.info("短信验证码："+sbCode.toString());
        //发送短信
        smsSender.sendMsg(mobile,sbCode.toString());
    }

    @Override
    public List<UserVO> getOperatorList(Long regionId) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(UserEntity::getRoleCode,"1002")
                .eq(UserEntity::getRegionId,regionId)
                .eq(UserEntity::getStatus,true);


        return this.list(wrapper)
                .stream()
                .map(u->{
                    UserVO vo = new UserVO();
                    BeanUtils.copyProperties(u,vo);
                    vo.setRoleName(u.getRole().getRoleName());
                    vo.setRoleCode(u.getRoleCode());
                    vo.setUserId(u.getId());
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public List<UserVO> getRepairerList(Long regionId) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(UserEntity::getRoleCode,"1003")
                .eq(UserEntity::getRegionId,regionId)
                .eq(UserEntity::getStatus,true);

        return this.list(wrapper)
                .stream()
                .map(u->{
                    UserVO vo = new UserVO();
                    BeanUtils.copyProperties(u,vo);
                    vo.setRoleName(u.getRole().getRoleName());
                    vo.setRoleCode(u.getRoleCode());
                    vo.setUserId(u.getId());
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public List<UserVO> getUserByInnerCode(String innerCode, String roleCode) {
        //根据设备编号查询设备信息
        VmVO vmVO = vmService.getVMInfo(innerCode);
        if(vmVO == null) {
            throw new LogicException("设备不存在");
        }
        if(VMSystem.USER_OPERATOR.equals(roleCode)) {
            return getOperatorList(vmVO.getRegionId());
        }
        return getRepairerList(vmVO.getRegionId());
    }

    @Override
    public Integer getCountByRegion(Long regionId, Boolean isRepair) {
        var qw = new LambdaQueryWrapper<UserEntity>();
        qw.eq(UserEntity::getRegionId,regionId);
        if(isRepair){
            qw.eq(UserEntity::getRoleId,3);
        }else {
            qw.eq(UserEntity::getRoleId,2);
        }

        return this.count(qw);
    }

    /**
     * 管理员登录
     * @param req
     * @return
     * @throws IOException
     */
    private LoginResp adminLogin(LoginReq req) throws IOException {
        LoginResp resp = new LoginResp();
        resp.setSuccess(false);
        String code =redisTemplate.boundValueOps(req.getClientToken()).get();
        if(Strings.isNullOrEmpty(code)){
            resp.setMsg("验证码错误");
            return resp;
        }
        if(!req.getCode().equals(code)){
            resp.setMsg("验证码错误");
            return resp;
        }
        QueryWrapper<UserEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(UserEntity::getLoginName,req.getLoginName());
        UserEntity userEntity = this.getOne(qw);
        if(userEntity == null){
            resp.setMsg("账户名或密码错误");
            return resp;
        }
        boolean loginSuccess = BCrypt.checkpw(req.getPassword(),userEntity.getPassword());
        if(!loginSuccess){
            resp.setMsg("账户名或密码错误");
            return resp;
        }
        return okResp(userEntity,VMSystem.LOGIN_ADMIN);
    }

    /**
     * 登录成功签发token
     * @param userEntity
     * @param loginType
     * @return
     */
    private LoginResp okResp(UserEntity userEntity,Integer loginType ) throws IOException {
        LoginResp resp = new LoginResp();
        resp.setSuccess(true);
        resp.setRoleCode(userEntity.getRoleCode());
        resp.setUserName(userEntity.getUserName());
        resp.setUserId(userEntity.getId());
        resp.setRegionId(userEntity.getRegionId()+"");
        resp.setMsg("登录成功");

        TokenObject tokenObject = new TokenObject();
        tokenObject.setUserId(userEntity.getId());
        tokenObject.setMobile(userEntity.getMobile());
        tokenObject.setLoginType(loginType);
        String token = JWTUtil.createJWTByObj(tokenObject,userEntity.getMobile() + VMSystem.JWT_SECRET);
        resp.setToken(token);
        return resp;
    }


    /**
     * 运维运营人员登录
     * @param req
     * @return
     * @throws IOException
     */
    private LoginResp empLogin(LoginReq req) throws IOException {
        LoginResp resp = new LoginResp();
        resp.setSuccess(false);
        String code =redisTemplate.boundValueOps(req.getMobile()).get();
        if(Strings.isNullOrEmpty(code)){
            resp.setMsg("验证码错误");
            return resp;
        }
        if(!req.getCode().equals(code)){
            resp.setMsg("验证码错误");
            return resp;
        }

        QueryWrapper<UserEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(UserEntity::getMobile, req.getMobile());
        UserEntity userEntity = this.getOne(qw);
        if (userEntity == null){
            resp.setMsg("不存在该账户");
            return resp;
        }
        return okResp( userEntity,VMSystem.LOGIN_EMP );
    }

}
