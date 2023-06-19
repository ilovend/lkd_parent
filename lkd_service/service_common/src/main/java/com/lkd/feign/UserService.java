package com.lkd.feign;
import com.lkd.feign.fallback.UserServiceFallbackFactory;
import com.lkd.vo.PartnerVO;
import com.lkd.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "user-service",fallbackFactory = UserServiceFallbackFactory.class)
public interface UserService{
    @GetMapping("/user/{id}")
    UserVO getUser(@PathVariable("id") int id);
    @GetMapping("/user/repairers/{regionId}")
    List<UserVO> getRepairers(@PathVariable("regionId") String regionId);
    @GetMapping("/user/operators/{regionId}")
    List<UserVO> getOperators(@PathVariable("regionId") String regionId);

    @GetMapping("/user/operaterCount")
    Integer getOperatorCount();

    @GetMapping("/user/repairerCount")
    Integer getRepairerCount();

    @GetMapping("/user/repairerList/{innerCode}")
    List<UserVO> getRepairerListByInnerCode(@PathVariable String innerCode);

    @GetMapping("/user/operatorList/{innerCode}")
    List<UserVO> getOperatorListByInnerCode(@PathVariable String innerCode);

    @GetMapping("/partner/{id}")
    PartnerVO getPartner(@PathVariable Integer id);

    @GetMapping("/user/countByRegion/{regionId}/{isRepair}")
    Integer getCountByRegion(@PathVariable Long regionId,@PathVariable Boolean isRepair);

    @GetMapping("/partner/name")
    String getPartnerName(@PathVariable Integer id);
}
