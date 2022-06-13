package com.hit.sz.lib.data;

import java.io.Serializable;

public class LoginData extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;
    private String name;
    private String pwd;


    public LoginData(int type, String name, String pwd) {
        super(type);
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
