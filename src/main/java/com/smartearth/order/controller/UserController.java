package com.smartearth.order.controller;

import com.smartearth.order.pojo.Response;
import com.smartearth.order.pojo.Result;
import com.smartearth.order.service.GeneralService;
import com.smartearth.order.service.UserService;
import com.smartearth.order.util.DataInitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private GeneralService generalService;

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 0代表登录名不存在 1代表账号密码不正确 2代表登录成功 3代表审核未通过 data字段是用户信息
     */
    @RequestMapping("login")
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        Response response = userService.login(username, password);
        if (response.getStatus() == 2) {
            Object userId = response.getData();
            generalService.insert("t_login_log", DataInitUtil.initLoginLogData(userId,request));
        }
        return response;
    }

    /**
     * 获取用户信息
     */
    @RequestMapping("getUserInfo")
    public Map<String, Object> getUserInfo(HttpServletRequest request) {
        return userService.getUserInfo(request.getHeader("userId"));
    }

    /**
     * 注册
     * type 1代表个人注册 2代表企业用户
     * @return 1验证码已过期 2验证码错误 3手机号不是验证的手机号 4登录名也就是手机号已注册 5企业名称已注册 6注册成功
     */
    @RequestMapping("register")
    public int register(@RequestParam("type") int type,@RequestParam("jsonData") String jsonData, @RequestParam(value = "code", required = false) String code){
        return userService.register(type,jsonData,code);
    }

    /**
     * 通过用户名获取用户公司名称
     * @param username 用户名也就是手机号
     * @return  用户公司名称，如果用户名不存在就返回null
     */
    @RequestMapping("companyName")
    public Result companyName(@RequestParam("username") String username) {
        Result result = new Result();
        Map<String, Object> user = userService.getUserByUsername(username);
        if(user != null){
            result.setResult(user.get("companyName").toString());
        }
        return result;
    }

    /**
     * 修改密码
     * @param password 新密码
     */
    @RequestMapping("updatePassword")
    public void updatePassword(@RequestParam("password") String password, @RequestParam("username") String username) {
        userService.updatePassword(username, password);
    }

    /**
     * 修改企业名称
     * @param companyName 新公司名称
     * @return 1代表企业名称已存在 2代表修改成功
     */
    @RequestMapping("updateCompanyName")
    public int updateCompanyName(@RequestParam("companyName") String companyName,HttpServletRequest request) {
        return userService.updateCompanyName(request.getHeader("userId"),companyName);
    }

    /**
     * 认证
     * @param userJsonData 用户信息data
     */
    @RequestMapping("auth")
    public void auth(@RequestParam("type") int type,@RequestParam("userJsonData") String userJsonData,HttpServletRequest request) {
        userService.auth(type,userJsonData,request.getHeader("userId"));
    }

    /**
     * 修改认证和用户信息
     * @param userJsonData 用户信息data
     */
    @RequestMapping("updateUserAndAuth")
    public void updateUserAndAuth(@RequestParam("userJsonData") String userJsonData,@RequestParam("authJsonData") String authJsonData) {
        userService.updateUserAndAuth(userJsonData,authJsonData);
    }

}
