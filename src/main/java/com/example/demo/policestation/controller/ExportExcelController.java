package com.example.demo.policestation.controller;

import com.example.demo.policestation.service.ExportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 导出excel文件demo
 * </p>
 *
 * @author wangdejian
 * @since 2018/7/14
 */
@RestController
public class ExportExcelController {


    @Autowired
    private ExportExcelService exportExcelService;

    /**
     * 测试导出大数据到excel
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "exportExcel")
    public String exportExcelDemo(HttpServletResponse response) {

        exportExcelService.exportExcel(response);

        return "导出结果............";
    }
}
