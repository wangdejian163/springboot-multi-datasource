package com.example.demo.policestation.service.impl;

import com.example.demo.policestation.mapper.ExportExcelMapper;
import com.example.demo.policestation.service.ExportExcelService;
import com.example.demo.policestation.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     导出大数据量测试
 * </p>
 *
 * @author wangdejian
 * @since 2018/7/14
 */
@Service
@Transactional
public class ExportExcelServiceImpl implements ExportExcelService {

    private Logger LOGGER = LoggerFactory.getLogger(ExportExcelServiceImpl.class);
    @Autowired
    private ExportExcelMapper exportExcelMapper;

    @Override
    public void exportExcel(HttpServletResponse response) {
        // 定义表头
        String [] tableHeadValue = new String[]{"ID", "角色", "父ID", "角色名称", "组织结构ID", "版本号"};
        String [] tableHeadKey = new String[] {"id", "num", "pid", "name", "deptid", "version"};
        // 定义表数据
        List<Map> roles = exportExcelMapper.selectExportExcel();

        try {
            long lc = System.currentTimeMillis();

            // 大数据量导出
            ExcelUtil.exportBigDataExcel(response, tableHeadKey, tableHeadValue, roles, "导出角色", "xlsx", "C:/excel/");
            long l2 = System.currentTimeMillis();

            System.out.println("导出数据时需要多少时间：" + (l2 -lc));
            // .导出1107258条数据。07版excel导出数据时需要时间：30539ms
        } catch (Exception e) {
            LOGGER.error("导出数据时出错{}", e);
        }

    }
}
