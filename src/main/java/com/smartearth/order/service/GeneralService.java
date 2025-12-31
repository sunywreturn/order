package com.smartearth.order.service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface GeneralService {
    List<Map<String, Object>> query(String column, String table, String condition);

    void delete(String table, String ids);

    void sendCode(String phone);

    String insert(String table, Map<String, Object> data);

    void update(String table, String id, Map<String, Object> data);

    int checkCode(String phone, String code);

    List<Map<String, Object>> query(String table, String condition);

    void sendSms(String phone,String code, List<String> results);

    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException;



    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException;
}
