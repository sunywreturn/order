package com.smartearth.order.pojo;

public class PageResponse {
    private Object data;
    private long total;
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }

}
