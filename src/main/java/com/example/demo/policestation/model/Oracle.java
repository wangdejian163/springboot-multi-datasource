package com.example.demo.policestation.model;

import java.io.Serializable;

/**
 * <p>测试</p>
 *
 * @author wangdejian
 * @since 2018/3/19
 */
public class Oracle implements Serializable {

    private static final long serialVersionUID = 8035873085454349238L;

    private String name;
    private String address;
    private String school;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Oracle{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", school='" + school + '\'' +
                ", age=" + age +
                '}';
    }
}
