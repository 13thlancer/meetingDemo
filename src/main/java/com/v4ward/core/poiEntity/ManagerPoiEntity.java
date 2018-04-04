package com.v4ward.core.poiEntity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class ManagerPoiEntity  implements Serializable {

    private static final long serialVersionUID = -680993095523805544L;


    private String id;

    @Excel(name = "帐号", orderNum = "0")
    private String account;

    private String password;

    private String salt;

    @Excel(name = "姓名", orderNum = "1")
    private String name;

    @Excel(name = "职务", orderNum = "2")
    private String position;

    @Excel(name = "组织编号", orderNum = "3")
    private String orgNum;

    private String remark;

    @Excel(name = "电话", orderNum = "4")
    private String phone;

    private String mail;

    private Integer status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;

    private String createemp;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updatetime;

    private String updateemp;

    private Integer version;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreateemp() {
        return createemp;
    }

    public void setCreateemp(String createemp) {
        this.createemp = createemp;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateemp() {
        return updateemp;
    }

    public void setUpdateemp(String updateemp) {
        this.updateemp = updateemp;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOrgNum() {
        return orgNum;
    }

    public void setOrgNum(String orgNum) {
        this.orgNum = orgNum;
    }

    @Override
    public String toString() {
        return "ManagerPoiEntity{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", createtime=" + createtime +
                ", createemp='" + createemp + '\'' +
                ", updatetime=" + updatetime +
                ", updateemp='" + updateemp + '\'' +
                ", version=" + version +
                '}';
    }

    public static ManagerPoiEntity newInstance(String account, String name, String position, String orgNum, String phone){
        ManagerPoiEntity mpe = new ManagerPoiEntity();
        mpe.setAccount(account);
        mpe.setName(name);
        mpe.setPosition(position);
        mpe.setOrgNum(orgNum);
        mpe.setPhone(phone);
        return mpe;
    }
}
