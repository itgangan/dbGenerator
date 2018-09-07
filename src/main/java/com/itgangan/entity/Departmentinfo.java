package com.itgangan.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name="DepartmentInfo")
public class Departmentinfo {
    @Id
    @Column(name = "Code")
    private String code;

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private Integer type;

    @Column(name = "Pre")
    private String pre;

    @Column(name = "Security")
    private Integer security;

    @Column(name = "DelStatus")
    private Boolean delstatus;

    @Column(name = "CreateTime")
    private Date createtime;

    @Column(name = "UpdateTime")
    private Date updatetime;

    /**
     * @return Code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return Pre
     */
    public String getPre() {
        return pre;
    }

    /**
     * @param pre
     */
    public void setPre(String pre) {
        this.pre = pre;
    }

    /**
     * @return Security
     */
    public Integer getSecurity() {
        return security;
    }

    /**
     * @param security
     */
    public void setSecurity(Integer security) {
        this.security = security;
    }

    /**
     * @return DelStatus
     */
    public Boolean getDelstatus() {
        return delstatus;
    }

    /**
     * @param delstatus
     */
    public void setDelstatus(Boolean delstatus) {
        this.delstatus = delstatus;
    }

    /**
     * @return CreateTime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * @return UpdateTime
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * @param updatetime
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}