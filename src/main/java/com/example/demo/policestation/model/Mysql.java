package com.example.demo.policestation.model;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author wangdejian
 * @since 2018/3/20
 */
public class Mysql implements Serializable {

    private static final long serialVersionUID = 3830224355595558537L;

    private Integer id;
    private String name;
    private String namelish;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamelish() {
        return namelish;
    }

    public void setNamelish(String namelish) {
        this.namelish = namelish;
    }

    @Override
    public String toString() {
        return "Mysql{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", namelish='" + namelish + '\'' +
                '}';
    }
}
