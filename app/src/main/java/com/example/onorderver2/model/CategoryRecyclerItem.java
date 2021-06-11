package com.example.onorderver2.model;

public class CategoryRecyclerItem {

    private String code;
    private String ctgName;

    public CategoryRecyclerItem(String code, String ctgName) {
        this.code = code;
        this.ctgName = ctgName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCtgName() {
        return ctgName;
    }

    public void setCtgName(String ctgName) {
        this.ctgName = ctgName;
    }
}
