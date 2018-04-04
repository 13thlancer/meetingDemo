package com.v4ward.core.model;

import java.io.Serializable;

public class RestUser implements Serializable {

    private static final long serialVersionUID = 8127222888608547324L;

    private String token;

    private String name;

    private String job;

    private String role;

    private String imName;

    private String imToken;

    private String portraitUrl;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImName() {
        return imName;
    }

    public void setImName(String imName) {
        this.imName = imName;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public static RestUser newInstance(String token, String name, String job, String role, String imName, String imToken,String portraitUrl){
        RestUser restUser = new RestUser();
        restUser.setJob(job);
        restUser.setName(name);
        restUser.setRole(role);
        restUser.setToken(token);
        restUser.setImName(imName);
        restUser.setImToken(imToken);
        restUser.setPortraitUrl(portraitUrl);
        return restUser;
    }
}
