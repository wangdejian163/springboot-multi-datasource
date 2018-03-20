package com.example.demo.policestation.service;

import com.example.demo.policestation.model.Oracle;
import com.example.demo.policestation.model.Mysql;

import java.util.List;

/**
 * <p>
 * 测试
 * </p>
 *
 * @author wangdejian
 * @since 2018/3/19
 */

public interface TestService {

    List<Oracle> findOracleList();

    List<Mysql> findMysqlList();

}
