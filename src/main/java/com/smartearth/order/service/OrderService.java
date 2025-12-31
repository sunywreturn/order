package com.smartearth.order.service;

public interface OrderService {

    void dispatch(String orderJsonData, String dispatchJsonDatas);

    void chooseOffer(String orderJsonData, String selectedDispatchId);

    String insert(String jsonData);

    void sendChooseOfferMail(String id);

    //void sendDispatchMail(String id);
}
