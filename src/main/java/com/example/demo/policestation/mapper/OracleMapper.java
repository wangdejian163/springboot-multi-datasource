package com.example.demo.policestation.mapper;

import com.example.demo.policestation.model.Oracle;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author wangdejian
 * @since 2018/3/19
 */
@Component
public interface OracleMapper {

    List<Oracle> selectOracleList();
}
