package com.xjj.security;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by XuJijun on 2018-05-08.
 */
public class JwtPayload<T> {
    private int type; // 类型

    private T id;  //id 可能是 int 或 String

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> roles;

    private long exp; //过期时间戳

    public JwtPayload(){}

    public JwtPayload(int type, T id, long exp) {
        this.type = type;
        this.id = id;
        this.exp = exp;
    }

    public JwtPayload(int type, T id, List<String> roles, long exp) {
        this.type = type;
        this.id = id;
        this.roles = roles;
        this.exp = exp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }
}
