package com.smartearth.order.service.impl;

import com.smartearth.order.pojo.Response;
import com.smartearth.order.service.GeneralService;
import com.smartearth.order.service.UserService;
import com.smartearth.order.util.JsonUtil;
import com.smartearth.order.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private GeneralService generalService;

    @Override
    public Response login(String username, String password) {
        Response response = new Response();
        int loginResult;
        if (getUserByUsername(username) == null) {
            loginResult = 0;
        } else {
            List<Map<String, Object>> queryUsernamePasswordList = generalService.query("id", "t_user", "username='" + username + "' and password='" + password + "'");
            if (queryUsernamePasswordList.size() == 0) {
                loginResult = 1;
            } else {
                response.setData(queryUsernamePasswordList.get(0).get("id"));
                loginResult = 2;
            }
        }
        response.setStatus(loginResult);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int register(int type, String jsonData, String code) {
        int registerResult = 6;
        Map<String, Object> data = JsonUtil.parseObject(jsonData, Map.class);
        String currentTime = new TimeUtil().getCurrentDateTime();
        String username = data.get("username").toString();
        if(code!=null){
            data.put("registerTime", currentTime);
            int checkCodeResult = generalService.checkCode(username, code);
            if (checkCodeResult == 4) {
                return 3;
            }
            if (checkCodeResult != 3) {
                return checkCodeResult;
            }
        }
        Map<String, Object> user = getUserByUsername(username);
        if(type==1){
            if(user==null){
                if(code!=null){
                    generalService.insert("t_user", data);
                }
            }else {
                registerResult = 4;
            }
        }else {
            if(user==null){
                if(code!=null){
                    if (generalService.query("id", "t_user", "companyName='" + data.get("companyName") + "'").size() == 0) {
                        String userId = generalService.insert("t_user", data);
                        if (type == 2) {
                            Map<String, Object> auth = new HashMap<>();
                            auth.put("type", 1);
                            auth.put("userId", userId);
                            auth.put("createTime", currentTime);
                            generalService.insert("t_auth", auth);
                        }
                    } else {
                        registerResult = 5;
                    }
                }
            }else {
                if (generalService.query("t_auth", "userId=" + user.get("id").toString()).size() == 0) {
                    if(code!=null){
                        String userId = user.get("id").toString();
                        generalService.update("t_user", userId, data);
                        Map<String, Object> auth = new HashMap<>();
                        auth.put("type", 1);
                        auth.put("userId", userId);
                        auth.put("createTime", currentTime);
                        generalService.insert("t_auth", auth);
                    }
                } else {
                    registerResult = 4;
                }
            }
        }
        return registerResult;
    }

    @Override
    public void updatePassword(String username, String password) {
        Object id = getUserByUsername(username).get("id");
        Map<String, Object> data = new HashMap<>();
        data.put("password", password);
        generalService.update("t_user", id.toString(), data);
    }

    @Override
    public Map<String, Object> getUserByUsername(String username) {
        List<Map<String, Object>> queryUsernameResult = generalService.query("companyName,id", "t_user", "username='" + username + "'");
        if (queryUsernameResult.size() == 0) {
            return null;
        } else {
            return queryUsernameResult.get(0);
        }
    }

    @Override
    public int updateCompanyName(String userId, String companyName) {
        if (generalService.query("id", "t_user", "companyName='" + companyName + "'").size() == 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("companyName", companyName);
            generalService.update("t_user", userId, data);
            return 2;
        }
        return 1;
    }

    @Override
    public Map<String, Object> getUserInfo(String userId) {
        Map<String, Object> user = generalService.query("id,username,registerTime,companyName,data,if((select count(*) from t_auth where userId=t_user.id)=0,0,if((select count(*) from t_auth where t_auth.type!=1 and t_auth.status=3 and userId=t_user.id)=0,1,(select sum(type) from t_auth where userId=t_user.id and t_auth.type!=1 and t_auth.status=3))) type", "t_user", "id=" + userId).get(0);
        List<Map<String, Object>> queryUserAuthResult = generalService.query("t_auth", "id in (select max(id) from t_auth where userId=" + userId + " group by type)");
        user.put("auth", queryUserAuthResult);
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void auth(int type, String userJsonData, String userId) {
        Map<String, Object> auth = new HashMap<>();
        auth.put("type", type);
        auth.put("userId", userId);
        auth.put("createTime", new TimeUtil().getCurrentDateTime());
        generalService.insert("t_auth", auth);
        Map<String, Object> user = JsonUtil.parseObject(userJsonData, Map.class);
        generalService.update("t_user", userId, user);
    }
	
	@Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserAndAuth(String userJsonData, String authJsonData) {
        Map<String, Object> user = JsonUtil.parseObject(userJsonData, Map.class);
        Map<String, Object> auth = JsonUtil.parseObject(authJsonData, Map.class);
        String userId = user.get("id").toString();
        String authId = auth.get("id").toString();
        user.remove("id");
        auth.remove("id");
        generalService.update("t_user", userId, user);
        generalService.update("t_auth", authId, auth);
    }
}
