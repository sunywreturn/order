package com.smartearth.order.util;

import com.smartearth.order.service.GeneralService;
import com.smartearth.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class DataInitUtil {
    public static Map<String,Object> initLoginLogData(Object userId, HttpServletRequest request){
        // 创建登录日志数据Map对象
        Map<String,Object> loginLogData = new HashMap<>();
        loginLogData.put("userId",userId);
        loginLogData.put("ip",ClientUtil.getIpAddr(request));
        loginLogData.put("time",new Date());
        return loginLogData;
    }
    public static List<String> tableNameList = new ArrayList<>();

    @Autowired
    private GeneralService generalService;

    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void init(){
        initTableNameList();
        initOrderSendChooseOfferMailTimerTask();
    }

    private void initTableNameList(){
        String tableNameColumn = "TABLE_NAME";
        List<Map<String, Object>> table =  generalService.query(tableNameColumn,"information_schema.TABLES","TABLE_SCHEMA=(select database())");
        for (Map<String, Object> tableItem : table) {
            tableNameList.add(tableItem.get(tableNameColumn).toString());

        }
    }

    private void initOrderSendChooseOfferMailTimerTask(){
        List<Map<String, Object>> orders = generalService.query("t_order.id,t_order.dispatchEndTime","t_order,t_user","t_order.userId=t_user.id and (t_order.type=1 or t_order.type=2 or t_order. type=3) and status=3 and dispatchEndTime>now()");
        for (Map<String, Object> order:orders) {
            String orderId = order.get("id").toString();
            TimerUtil.newTimerTask(orderId, order.get("dispatchEndTime").toString(), new TimerTask() {
                @Override
                public void run() {
                    orderService.sendChooseOfferMail(orderId);
                }
            });
        }
    }

    //生成16位唯一性的订单号
    public static String generateOrderNo() {
        // 时间戳(13位) + 随机数(3位)
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomNum = String.format("%03d", new Random().nextInt(1000));
        return timestamp + randomNum;
    }

}
