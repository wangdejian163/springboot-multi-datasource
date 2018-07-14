package com.example.demo.policestation.model;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author wangdejian
 * @since 2018/7/14
 */
public class Role implements Serializable {

    private static final long serialVersionUID = -295400568378644900L;

    private String id;
    private int num;
    private String pid;
    private String name;
    private int deptid;
    private int version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeptid() {
        return deptid;
    }

    public void setDeptid(int deptid) {
        this.deptid = deptid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", num=" + num +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", deptid=" + deptid +
                ", version=" + version +
                '}';
    }
}
