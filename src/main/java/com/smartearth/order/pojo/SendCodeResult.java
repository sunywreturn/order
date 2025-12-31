package com.smartearth.order.pojo;

public class SendCodeResult {
    private String codeJsonStr;
    private boolean test;

    public String getCodeJsonStr() {
        return codeJsonStr;
    }

    public void setCodeJsonStr(String codeJsonStr) {
        this.codeJsonStr = codeJsonStr;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
