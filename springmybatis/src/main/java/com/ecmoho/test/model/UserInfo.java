package com.ecmoho.test.model;

/**
 * Created by meidejing on 2016/6/7.
 */
public class UserInfo {
    private int id;
    private String uname;
    private int unumber;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setUname(String uname) {
        this.uname = uname;
    }
    public String getUname() {
        return uname;
    }
    public void setUnumber(int unumber) {
        this.unumber = unumber;
    }
    public int getUnumber() {
        return unumber;
    }
    @Override
    public String toString() {
        return "id:"+id+",uname:"+uname+",unumber:"+unumber;
    }
}
