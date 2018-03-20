package com.example.demo.policestation.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.example.demo.policestation.config.properties.PoliceProperties;
import com.example.demo.policestation.config.properties.SubBureauProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * <p>
 * mybatis-plus java configuration配置
 * </p>
 *
 * @author wangdejian
 * @since 2018/3/19
 */
@Configuration
@MapperScan(value = "com.example.demo.*.mapper")
@EnableTransactionManagement(order = 2)//由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
@ComponentScan({"com.example.demo.policestation.config"})
public class MybatisPlusConfig {

    @Autowired
    private PoliceProperties policeProperties;

    @Autowired
    private SubBureauProperties subBureauProperties;

    private DruidDataSource stationDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        this.dataSourceConfig(dataSource, policeProperties, null);
        return dataSource;
    }

    /**
     * 另一个数据源
     */
    private DruidDataSource subBureauDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        this.dataSourceConfig(dataSource, null, subBureauProperties);
        return dataSource;
    }

    /**
     * 单数据源连接池配置
     */
    @Bean
    @ConditionalOnProperty(prefix = "datasource", name = "muti-datasource-open", havingValue = "false")
    public DruidDataSource singleDatasource() {
        return this.stationDataSource();
    }


    /**
     * 多数据源连接池配置
     * ConditionalOnProperty注解用来控制多数据源下DataSourceAspect切面生效
     */
    @Bean
    @ConditionalOnProperty(prefix = "datasource", name = "muti-datasource-open", havingValue = "true")
    public DynamicDataSource mutiDataSource() {
        // a数据源
        DruidDataSource stationDataSource = this.stationDataSource();
        // b数据源
        DruidDataSource subBureauDataSource = this.subBureauDataSource();

        try {
            stationDataSource.init();
            subBureauDataSource.init();
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> hashMap = new HashMap();
        hashMap.put("stationDataSource", stationDataSource);
        hashMap.put("subBureau", subBureauDataSource);
        dynamicDataSource.setTargetDataSources(hashMap);
        dynamicDataSource.setDefaultTargetDataSource(stationDataSource);
        return dynamicDataSource;
    }

    /**
     * 分页插件，自动识别数据库类型
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 配置数据源连接信息
     *
     * @param dataSource
     */
    private void dataSourceConfig(DruidDataSource dataSource, PoliceProperties policeProperties,
                                  SubBureauProperties subBureauProperties) {

        dataSource.setUrl(policeProperties == null ? subBureauProperties.getUrl() : policeProperties.getUrl());
        dataSource.setUsername(policeProperties == null ? subBureauProperties.getUsername() : policeProperties.getUsername());
        dataSource.setPassword(policeProperties == null ? subBureauProperties.getPassword() : policeProperties.getPassword());

        dataSource.setDriverClassName(policeProperties == null ? subBureauProperties.getDriverClassName() : policeProperties.getDriverClassName());
        dataSource.setInitialSize(policeProperties == null ? subBureauProperties.getInitialSize() :  policeProperties.getInitialSize());     //定义初始连接数
        dataSource.setMinIdle(policeProperties == null ? subBureauProperties.getMinIdle() : policeProperties.getMinIdle());             //最小空闲
        dataSource.setMaxActive(policeProperties == null ? subBureauProperties.getMaxActive() : policeProperties.getMaxActive());         //定义最大连接数
        dataSource.setMaxWait(policeProperties == null ? subBureauProperties.getMaxWait() : policeProperties.getMaxWait());             //最长等待时间

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(policeProperties == null ? subBureauProperties.getTimeBetweenEvictionRunsMillis() : policeProperties.getTimeBetweenEvictionRunsMillis());

        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(policeProperties == null ? subBureauProperties.getMinEvictableIdleTimeMillis() : policeProperties.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(policeProperties == null ? subBureauProperties.getValidationQuery() : policeProperties.getValidationQuery());
        dataSource.setTestWhileIdle(policeProperties == null ? subBureauProperties.getTestWhileIdle() : policeProperties.getTestWhileIdle());
        dataSource.setTestOnBorrow(policeProperties == null ? subBureauProperties.getTestOnBorrow() : policeProperties.getTestOnBorrow());
        dataSource.setTestOnReturn(policeProperties == null ? subBureauProperties.getTestOnReturn() : policeProperties.getTestOnReturn());

        // 打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(policeProperties == null ? subBureauProperties.getPoolPreparedStatements() : policeProperties.getPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(policeProperties == null ? subBureauProperties.getMaxPoolPreparedStatementPerConnectionSize() : policeProperties.getMaxPoolPreparedStatementPerConnectionSize());

        try {
            dataSource.setFilters(policeProperties == null ? subBureauProperties.getFilters() : policeProperties.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
