package com.smartearth.order.service.impl;

import com.smartearth.order.OrderApplication;
import com.smartearth.order.WebConfigurer;
import com.smartearth.order.mapper.GeneralMapper;
import com.smartearth.order.pojo.Code;
import com.smartearth.order.service.GeneralService;
import com.smartearth.order.service.OrderService;
import com.smartearth.order.service.UserService;
import com.smartearth.order.util.JsonUtil;
import com.smartearth.order.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeneralServiceImpl implements GeneralService {

    @Autowired
    private GeneralMapper generalMapper;

    @Override
    public List<Map<String, Object>> query(String column, String table, String condition) {
        return generalMapper.query(column, table, condition);
    }

    @Override
    public void delete(String table, String ids) {
        for (String id : ids.split(",")) {
            generalMapper.deleteByPrimaryKey(table, id);
        }
    }

    @Override
    public String insert(String table, Map<String, Object> data) {
        generalMapper.insert(table, data);
        Object id = data.get("id");
        if(id!=null){
            return data.get("id").toString();
        }
        return "";
    }

    @Override
    public void update(String table, String id, Map<String, Object> data) {
        generalMapper.update(table, id, data);
    }

    private ConcurrentHashMap<String,String> codeMap = new ConcurrentHashMap<>();

    @Override
    public void sendCode(String phone) {
        // TODO Auto-generated method stub
        String code = (int) (Math.random() * 9000 + 1000) + "";
        List<String> templateParams = new ArrayList<>();
        templateParams.add("{\"code\":\"" + code + "\"}");
        SmsUtil.sendSms(phone,"SMS_181060020",templateParams);
        Code codeObject = new Code();
        codeObject.setCode(code);
        codeObject.setTime(System.currentTimeMillis());
        codeObject.setPhone(phone);
        codeMap.put(phone,JsonUtil.toJson(codeObject));
    }

    @Override
    public int checkCode(String phone, String code) {
        String jsonCodeStr = codeMap.get(phone);
        if(jsonCodeStr==null){
            return 4;
        }
        Code codeObject = JsonUtil.parseObject(jsonCodeStr, Code.class);
        if(!codeObject.getPhone().equals(phone)){
            return 4;
        }
        String codeStr = codeObject.getCode();
        long time = codeObject.getTime();
        if (!codeStr.equals(code)) {
            return 2;
        }

        if ((System.currentTimeMillis() - time) > OrderApplication.CODE_EXPIRE) {
            return 1;
        }
        return 3;
    }

    @Override
    public List<Map<String, Object>> query(String table, String condition) {
        return query("*", table, condition);
    }

    @Override
    public void sendSms(String phone,String code, List<String> results) {
        SmsUtil.sendSms(phone,code, results);
    }

    /**
     * Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 配置文件中我的qq邮箱
     */
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);
    }

    /**
     * html邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;

        messageHelper = new MimeMessageHelper(message, true);
        //邮件发送人
        messageHelper.setFrom(from);
        //邮件接收人
        messageHelper.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容，html格式
        messageHelper.setText(content, true);
        //发送
        mailSender.send(message);

    }

    /**
     * 带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        helper.addAttachment(fileName, file);
        mailSender.send(message);
    }
}
