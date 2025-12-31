package com.smartearth.order.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.smartearth.order.OrderApplication;
import com.smartearth.order.pojo.SendSmsResponse;
import com.smartearth.order.service.GeneralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SmsUtil {

	private static final Logger LOG = LoggerFactory.getLogger(SmsUtil.class);

	public static void sendSms(String phone, String templateCode, List<String> templateParams){
		if(OrderApplication.test){
			for (String param:templateParams) {
				LOG.info(param);
			}
			return;
		}
		// 从环境变量或配置文件中读取AccessKey
		String accessKeyId = System.getenv("ALIYUN_ACCESS_KEY_ID");
		String accessKeySecret = System.getenv("ALIYUN_ACCESS_KEY_SECRET");
		
		if (accessKeyId == null || accessKeySecret == null) {
			LOG.error("阿里云AccessKey未配置，请设置环境变量ALIYUN_ACCESS_KEY_ID和ALIYUN_ACCESS_KEY_SECRET");
			return;
		}
		
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("PhoneNumbers", phone);// 接受验证码的手机号
		request.putQueryParameter("SignName", "平行世界数字孪生服务");// 签名
		request.putQueryParameter("TemplateCode", templateCode);// 模板代码
		if(templateParams!=null){
			for (String templateParam:templateParams) {
				request.putQueryParameter("TemplateParam", templateParam);
			}
		}
		try {
			CommonResponse response = client.getCommonResponse(request);
			String returnstr = response.getData();
			System.out.println(returnstr);
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args) {
		System.out.println("Hello");
	}

	// 发短信,FASAKSJAKJSdskdsdKDSDJSAKDASKJDDKSAJDKJSADJKAS
	public static void sendSms(String phone, String templateCode, String templateParam) {
		if(OrderApplication.test == true){

		}
	}
}