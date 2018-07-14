package com.example.demo.policestation.mapper;

import com.example.demo.policestation.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author wangdejian
 * @since 2018/7/14
 */
@Component
public interface ExportExcelMapper {

    /**
     * 查询出role集合
     *
     * @return
     */
    List<Map> selectExportExcel();

}
