package com.example.onorderver2.model;

import java.util.ArrayList;

public class PrintOrderModel {

    String table;
    ArrayList<OrderRecyclerItem> order;
    String time;
    String printStatus;
    String type;
    String cardName;
    String cardNum;
    String authDate;
    String authNum;
    String purchaseName;
    String notice;
    String vanTr;
    String price;
    String orderType;
    String orderCheck;


    public PrintOrderModel() {
    }

    public PrintOrderModel(String table, ArrayList<OrderRecyclerItem> order, String time, String printStatus, String type, String cardName,
                           String cardNum, String authDate, String authNum, String purchaseName, String notice, String vanTr,
                           String price, String orderType, String orderCheck) {
        this.table = table;
        this.order = order;
        this.time = time;
        this.printStatus = printStatus;
        this.type = type;
        this.cardName = cardName;
        this.cardNum = cardNum;
        this.authDate = authDate;
        this.authNum = authNum;
        this.purchaseName = purchaseName;
        this.notice = notice;
        this.vanTr = vanTr;
        this.price = price;
        this.orderType = orderType;
        this.orderCheck = orderCheck;
    }

    public PrintOrderModel(String table, ArrayList<OrderRecyclerItem> order, String time, String printStatus, String type) {
        this.table = table;
        this.order = order;
        this.time = time;
        this.printStatus = printStatus;
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ArrayList<OrderRecyclerItem> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderRecyclerItem> order) {
        this.order = order;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getAuthDate() {
        return authDate;
    }

    public void setAuthDate(String authDate) {
        this.authDate = authDate;
    }

    public String getAuthNum() {
        return authNum;
    }

    public void setAuthNum(String authNum) {
        this.authNum = authNum;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getVanTr() {
        return vanTr;
    }

    public void setVanTr(String vanTr) {
        this.vanTr = vanTr;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderCheck() {
        return orderCheck;
    }

    public void setOrderCheck(String orderCheck) {
        this.orderCheck = orderCheck;
    }
}
