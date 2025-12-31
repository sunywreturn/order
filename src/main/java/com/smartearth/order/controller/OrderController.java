package com.smartearth.order.controller;

import com.smartearth.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 派单
     * @param orderJsonData
     * @param dispatchJsonDatas
     * @throws Exception
     */
    @RequestMapping("dispatch")
    public void dispatch(@RequestParam("orderJsonData") String orderJsonData, @RequestParam("dispatchJsonDatas") String dispatchJsonDatas) {
        orderService.dispatch(orderJsonData, dispatchJsonDatas);
    }

    /**
     * 选择报价
     */
    @RequestMapping("chooseOffer")
    public void chooseOffer(@RequestParam("orderJsonData") String orderJsonData,@RequestParam("selectedDispatchId") String selectedDispatchId) {
        orderService.chooseOffer(orderJsonData,selectedDispatchId);
    }

    /**
     * 新增订单
     */
    @RequestMapping("insert")
    public String insert(@RequestParam("jsonData") String jsonData) {
        return orderService.insert(jsonData);
    }
}