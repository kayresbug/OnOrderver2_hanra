package com.example.onorderver2.model;

import java.io.Serializable;

public class MenuRecyclerItemOpt implements Serializable {

    public String name;
    public String price;
    public String picurl;
    public String info;
    public String code;
    public String ctgcode;
    public String option;
    public MenuRecyclerItemOpt(String name, String price, String picurl, String info, String code, String ctgcode, String option) {
        this.name = name;
        this.price = price;
        this.picurl = picurl;
        this.info = info;
        this.code = code;
        this.ctgcode = ctgcode;
        this.option = option;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCtgcode() {
        return ctgcode;
    }

    public void setCtgcode(String ctgcode) {
        this.ctgcode = ctgcode;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
