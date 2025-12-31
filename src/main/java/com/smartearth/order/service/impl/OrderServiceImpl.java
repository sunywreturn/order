package com.smartearth.order.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smartearth.order.service.GeneralService;
import com.smartearth.order.service.OrderService;
import com.smartearth.order.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private GeneralService generalService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void dispatch(String orderJsonData, String dispatchJsonDatas) {
        Map<String, Object> orderData = JsonUtil.parseObject(orderJsonData, Map.class);
        String orderId = orderData.get("id").toString();
        orderData.remove("id");
        generalService.update("t_order",orderId,orderData);
        JsonArray jsonArray = JsonParser.parseString(dispatchJsonDatas).getAsJsonArray();
        Iterator<JsonElement>  iterator = jsonArray.iterator();
        List<String> dispatchJsonDataList = new ArrayList<>();
        while (iterator.hasNext()){
            dispatchJsonDataList.add(iterator.next().toString());
        }
        for (String dispatchJsonData:dispatchJsonDataList) {
            Map<String, Object> dispatchData = JsonUtil.parseObject(dispatchJsonData, Map.class);
            String username = dispatchData.get("username").toString();
            dispatchData.remove("username");
            generalService.insert("t_dispatch",dispatchData);
            List<String> templateParams = new ArrayList<>();
            templateParams.add("{\"result\":\"" + orderData.get("dispatchEndTime").toString() + "\"}");
            SmsUtil.sendSms(username,"SMS_186613612",templateParams);
        }
        TimerUtil.newTimerTask(orderId, orderData.get("dispatchEndTime").toString(), new TimerTask() {
            @Override
            public void run() {
                sendChooseOfferMail(orderId);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void chooseOffer(String orderJsonData, String selectedDispatchId) {
        Map<String, Object> orderData = JsonUtil.parseObject(orderJsonData, Map.class);
        String orderId = orderData.get("id").toString();
        orderData.remove("id");
        generalService.update("t_order",orderId,orderData);
        List<Map<String, Object>> dispatches = generalService.query("t_dispatch.id,username","t_dispatch,t_user","t_dispatch.userId=t_user.id and orderId="+orderId);
        for (Map<String, Object> dispatch:dispatches) {
            String dispatchId = dispatch.get("id").toString();
            dispatch.remove("id");
            List<String> templateParams = new ArrayList<>();
            if(selectedDispatchId.equals(dispatchId)){
                templateParams.add("{\"result\":\"" + "已通过" + "\"}");
                dispatch.put("status",4);
            }else {
                templateParams.add("{\"result\":\"" + "未通过" + "\"}");
                dispatch.put("status",3);
            }
            SmsUtil.sendSms(dispatch.get("username").toString(),"SMS_185246616",templateParams);
            dispatch.remove("username");
            generalService.update("t_dispatch",dispatchId,dispatch);
        }
    }

    public void sendChooseOfferMail(String orderId){
        TimerTask timerTask = TimerUtil.timerMap.get(orderId);
        if(timerTask!=null){
            TimerUtil.timerMap.remove(orderId);
            timerTask.cancel();
        }
        generalService.sendSimpleMail("xienan@terra-it.cn","平行世界","编号为"+orderId+"的订单可以选择报价了");
    }


    @Override
    public String insert(String jsonData) {
        String id = DataInitUtil.getUUID();
        Map<String, Object> data = JsonUtil.parseObject(jsonData, Map.class);
        data.put("createTime",new TimeUtil().getCurrentDateTime());
        data.put("id", id);
        return generalService.insert("t_order",data);
    }
}
