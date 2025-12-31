package com.smartearth.order.controller;

import com.smartearth.order.annotation.Page;
import com.smartearth.order.annotation.RequestLimit;
import com.smartearth.order.pojo.Result;
import com.smartearth.order.service.GeneralService;
import com.smartearth.order.util.CheckUtil;
import com.smartearth.order.util.FileUtil;
import com.smartearth.order.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/general")
public class GeneralController {

    @Autowired
    private GeneralService generalService;


    /**
     * 查询通用数据，支持分页和条件筛选
     *
     * @param column 查询的列名，默认为 "*"
     * @param function 查询的表名
     * @param condition 查询条件，默认为 "1=1"（即无条件）
     * @param pageNum 页码，默认为 null
     * @param pageSize 每页的记录数，默认为 null
     * @return 查询结果
     */
    @Page
    @RequestMapping("query")
    public List<Map<String, Object>> query(
            @RequestParam(value = "column", defaultValue = "*") String column,
            @RequestParam("function") String function,
            @RequestParam(value = "condition", defaultValue = "1=1") String condition,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        CheckUtil.checkTable(function);
        CheckUtil.checkColumn(column);
        CheckUtil.checkCondition(condition);
        return generalService.query(column, function, condition);
    }

    /**
     * 删除指定表中的记录
     *
     * @param function 表名
     * @param ids 要删除的记录ID，多个ID用逗号分隔
     */
    @RequestMapping("delete")
    public void delete(@RequestParam("function") String function, @RequestParam("ids") String ids) {
        CheckUtil.checkTable(function);
        generalService.delete(function, ids);
    }

    /**
     * 插入数据到指定的表中
     *
     * @param function 表名
     * @param jsonData JSON格式的数据
     * @return 插入操作的结果字符串
     */
    @RequestMapping("insert")
    public String insert(@RequestParam("function") String function, @RequestParam("jsonData") String jsonData) {
        Map<String, Object> data = JsonUtil.parseObject(jsonData, Map.class);
        CheckUtil.checkTable(function);
        return generalService.insert(function, data);
    }

    @RequestMapping("update")
    public void update(@RequestParam("function") String function, @RequestParam("id") String id, @RequestParam("jsonData") String jsonData) {
        Map<String, Object> data = JsonUtil.parseObject(jsonData, Map.class);
        CheckUtil.checkTable(function);
        generalService.update(function, id, data);
    }

    /**
     * 发送验证码
     * @param phone 手机号
     * @throws Exception
     */
    @RequestLimit(value = 60000)
    @RequestMapping("sendCode")
    public void sendCode(@RequestParam("phone") String phone) {
        generalService.sendCode(phone);
    }

    /**
     * 文件上传
     * @param file 文件
     * @return 文件路径
     * @throws IOException
     */
    @RequestMapping("upload")
    public Result upload(@RequestParam MultipartFile file) throws IOException {
        Result result = new Result();
        result.setResult(FileUtil.save(file));
        return result;
    }

    /**
     * 校验验证码
     * @param code 验证码
     * @return 1验证码已过期 2验证码错误 3验证通过 4手机号不是验证的手机号
     * @throws Exception
     */
    @RequestMapping("checkCode")
    public int checkCode(@RequestParam("phone") String phone,@RequestParam("code") String code) {
        return generalService.checkCode(phone, code);
    }

    @RequestMapping("sendMail")
    public void sendMail(@RequestParam("host") String host,@RequestParam("title") String title,@RequestParam("content") String content) throws MessagingException {
        generalService.sendHtmlMail(host,title,content);
    }

    @RequestMapping("sendSms")
    public void sendSms(@RequestParam("phone") String phone,@RequestParam("code") String code,@RequestParam("results") List<String> results){
        generalService.sendSms(phone,code,results);
    }
}