package com.example.demo.policestation.controller;

import com.example.demo.policestation.exception.NotFoundException;
import com.example.demo.policestation.model.Oracle;
import com.example.demo.policestation.model.Mysql;
import com.example.demo.policestation.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>多数据源配置</p>
 *
 * @author wangdejian
 * @since 2018/3/19
 */
@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping(value = "testOracle")
    public List<Oracle> testOracle() {
        return testService.findOracleList();
    }

    /**
     * 多数据源测试
     *
     * @return
     */
    @RequestMapping(value = "testMysql")
    public List<Mysql> testMysql() {
        return testService.findMysqlList();
    }
	
    /**
     * 测试全局异常是否执行.
     * @return
     */
    @RequestMapping(value = "globalExcepionHanderTest")
    public ResponseEntity<Mysql> globalExcepionHanderTest() {
        Mysql mysql = testService.findMysqlById();
       // 自定义抛异常条件
		if (null == mysql) {
            throw new NotFoundException("查询结果为空.................");
        }
        return new ResponseEntity<Mysql>(mysql, HttpStatus.OK);
    }
}
