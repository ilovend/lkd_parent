package com.lkd.feign.fallback;

import com.lkd.feign.UserService;
import com.lkd.vo.PartnerVO;
import com.lkd.vo.UserVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable throwable) {
        log.error("用户服务调用失败",throwable);
        return new UserService() {
            @Override
            public UserVO getUser(int id) {
                return null;
            }

            @Override
            public List<UserVO> getRepairers(String regionId) {
                return null;
            }

            @Override
            public List<UserVO> getOperators(String regionId) {
                return null;
            }

            @Override
            public Integer getOperatorCount() {
                return 0;
            }

            @Override
            public Integer getRepairerCount() {
                return 0;
            }

            @Override
            public List<UserVO> getRepairerListByInnerCode(String innerCode) {
                return null;
            }

            @Override
            public List<UserVO> getOperatorListByInnerCode(String innerCode) {
                return null;
            }

            @Override
            public PartnerVO getPartner(Integer id) {
                return new PartnerVO();
            }

            @Override
            public Integer getCountByRegion(Long regionId, Boolean isRepair) {
                return 0;
            }

            @Override
            public String getPartnerName(Integer id) {
                return null;
            }
        };
    }
}
