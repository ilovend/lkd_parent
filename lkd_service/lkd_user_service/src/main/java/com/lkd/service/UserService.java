package com.lkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lkd.entity.UserEntity;
import com.lkd.http.vo.LoginReq;
import com.lkd.http.vo.LoginResp;
import com.lkd.vo.Pager;
import com.lkd.vo.UserVO;

import java.io.IOException;
import java.util.List;

public interface UserService extends IService<UserEntity> {
    /**
     * 获取所有运营人员数量
     */
    Integer getOperatorCount();

    /**
     * 获取所有维修员数量
     * @return
     */
    Integer getRepairerCount();

    /**
     * 分页查询
     * @param pageIndex
     * @param pageSize
     * @param userName
     * @return
     */
    Pager<UserEntity> findPage(long pageIndex, long pageSize, String userName,Integer roleId,Boolean isRepair);

    /**
     * 后台登录
     * @param req
     * @return
     */
    LoginResp login(LoginReq req) throws IOException;

    /**
     * 发送验证码
     * @param mobile
     */
    void sendCode(String mobile);

    /**
     * 获取某区域下所有运营人员
     * @param regionId
     * @return
     */
    List<UserVO> getOperatorList(Long regionId);

    /**
     * 获取某区域下所有运维人员
     * @param regionId
     * @return
     */
    List<UserVO> getRepairerList(Long regionId);

    /**
     * 根据设备所在区域查询运维/运营人员
     * @param innerCode
     * @param roleCode
     * @return
     */
    List<UserVO> getUserByInnerCode(String innerCode, String roleCode);

    /**
     * 获取某区域下维修员/运营员总数
     * @param isRepair
     * @return
     */
    Integer getCountByRegion(Long regionId,Boolean isRepair);
}
