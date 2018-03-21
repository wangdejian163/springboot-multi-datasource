package com.example.demo.policestation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.demo.policestation.config.annotion.DynamicSource;
import com.example.demo.policestation.mapper.MysqlMapper;
import com.example.demo.policestation.mapper.OracleMapper;
import com.example.demo.policestation.model.Mysql;
import com.example.demo.policestation.model.Oracle;
import com.example.demo.policestation.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p></p>
 *
 * @author wangdejian
 * @since 2018/3/19
 */
@Service
@Transactional
public class TestServiceImpl implements TestService {

    @Autowired
    private OracleMapper oracleMapper;

    @Autowired
    private MysqlMapper mysqlMapper;

    @Override
    public List<Oracle> findOracleList() {
        return oracleMapper.selectOracleList();
    }

    /**
     * 切换到mysql数据源.自定义注解aop切点@DynamicSource
     *
     * @return
     */
    @Override
    @DynamicSource
    public List<Mysql> findMysqlList() {
        return mysqlMapper.selectList(new EntityWrapper<Mysql>().eq("id", 7));
    }

	  /**
     * 这里如果不满足既定条件，则抛出自定义异常。
     *
     * @return
     */
    @Override
    @DynamicSource
    public Mysql findPoliceNameById() {
        Mysql mysql = mysqlMapper.selectById(100);
        // 表中不存在100. 定义查询结果为null时抛出自定义异常.
        if (null == mysql) {
            throw new NotFoundException("查询结果为空.................");
        }

        return mysqlMapper.selectById(100);
    }
	
}
