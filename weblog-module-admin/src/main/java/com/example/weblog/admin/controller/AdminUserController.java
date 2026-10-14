package com.example.weblog.admin.controller;


import com.example.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.example.weblog.admin.service.AdminUserService;
import com.example.weblog.common.aspect.ApiOperationLog;
import com.example.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 用户模块")
public class AdminUserController {

    @Autowired
    private AdminUserService userService;

    @PostMapping("password/update")
    @ApiOperation(value = "修改用户密码")
    @ApiOperationLog(description = "修改用户密码")
    public Response updatePassword(@RequestBody @Validated UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        return userService.updatePassword(updateAdminUserPasswordReqVO);
    }

    @PostMapping("/user/info")
    @ApiOperation(value = "获取用户信息")
    @ApiOperationLog(description = "获取用户信息")
    public Response findUserInfo() {
        return userService.findUserInfo();
    }


}
