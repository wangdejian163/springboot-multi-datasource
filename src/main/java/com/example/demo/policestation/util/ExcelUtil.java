package com.example.demo.policestation.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p></p>
 *
 * @author wangdejian
 * @since 2018/7/13
 */
public class ExcelUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);


    // 定义一个excel文件中最多可以放6w条数据
    private static final int ROW_MAX_COUNT = 30000;

    // 定义压缩包名字
    private static final String ZIP_NAME = "导出.zip";

    /**
     * 导出大数据量excel
     *
     * @param response
     * @param tableHeadKey   表头key
     * @param tableHeadValue 表头value
     * @param mapList        数据集合
     * @param excelName      导出excel的名字<>
     * @param excelSuffix     excel 后缀。区分excel版本03以及之前版本后缀为.xls 07版本以及之后为xlsx
     * @param filePath       导出路径
     */
    public static void exportBigDataExcel(HttpServletResponse response,
                                          String[] tableHeadKey,
                                          String[] tableHeadValue,
                                          List<Map> mapList,
                                          String excelName,
                                          String excelSuffix,
                                          String filePath) throws Exception {

        // 获取到当前查询结果集数据大小
        int allRowNums = mapList.size();

        excelName = excelName + "_" + System.currentTimeMillis();
        // 判断是否需要导出zip形式，如果allRowNums 值较小，则导出单个excel即可，否则需要导出多个excel临时文件，然后以压缩包的形式导出
        if (allRowNums > ROW_MAX_COUNT) {

            //1.设置相应头
            String filename = ZIP_NAME;
            filename = new String(filename.getBytes("GBK"), "iso-8859-1");
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");


            List<String> fileNames = new ArrayList<String>();  //存放生成的文件名称
            //上线后切换成linux服务器地址
            if (!new File(filePath).exists()) {
                new File(filePath).mkdirs();
            }


            File zip = new File(filePath + excelName + ".zip");  //压缩文件路径

            //3.分批次生成excel
            int tempSize = (allRowNums % ROW_MAX_COUNT) == 0 ? allRowNums / ROW_MAX_COUNT : allRowNums / ROW_MAX_COUNT + 1;
            for (int i = 0; i < tempSize; i++) {

                //生成excel
                String tempExcelFile = filePath + excelName + "[" + (i + 1) + "]." + excelSuffix;
                fileNames.add(tempExcelFile);
                OutputStream fos = new FileOutputStream(tempExcelFile);

                // 根据i值，截取当前excel导出数据
                int fromIndex = i * ROW_MAX_COUNT == 0 ? 0 : i * ROW_MAX_COUNT + 1;
                int endIndex = (i + 1) * ROW_MAX_COUNT > allRowNums ? allRowNums : (i + 1) * ROW_MAX_COUNT;

                // 截取出的subList写入到对应的excel中
                List<Map> subList = mapList.subList(fromIndex, endIndex);

                try {
                    Workbook wb = exportDataToExcel(tableHeadKey, tableHeadValue, excelSuffix, subList);
                    wb.write(fos);
                    fos.flush();
                    fos.close();
                } catch (RuntimeException runMsg) {
                    LOGGER.error("查询数据信息异常{}", runMsg);
                } finally {
                    fos.flush();
                    fos.close();
                }

            }

            // 导出压缩包
            exportZip(response, fileNames, zip);

        } else {

            // 导出文件到excel
            Workbook wb = exportDataToExcel(tableHeadKey, tableHeadValue, excelSuffix, mapList);
            // 导出excel文件名
            filePath = filePath + excelName + "." + excelSuffix;
            System.out.println("filePath:" + filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                try {
                    boolean newFile = file.createNewFile();
                    if (newFile) {
                        FileOutputStream fos = new FileOutputStream(filePath);
                        wb.write(fos);
                        fos.close();
                    }
                    //下载
                    downFile(response, filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    /**
     * 设置excel样式和数据
     *
     * @param tableHeadKey
     * @param tableHeadValue
     * @param subList
     * @return
     */
    private static Workbook exportDataToExcel(String[] tableHeadKey,
                                                  String[] tableHeadValue,
                                                  String excelSuffix,
                                                  List<Map> subList) {
        Workbook wb;
        // 判断使用HSSFWorkbook还是SXSSFWorkbook
        if ("xlsx".equals(excelSuffix)) {
            // 创建workbook
            wb = new SXSSFWorkbook(1000);
        } else {

            // 创建workbook
            wb = new HSSFWorkbook();
        }
        // 获取单元格样式对象
        CellStyle cellStyle = wb.createCellStyle();

        // 一、设置背景色：
        cellStyle.setFillForegroundColor((short) 10);// 设置背景色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 二、设置边框:
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        // 三、设置居中:
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 上下居中

        // 四、设置字体:
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示


        cellStyle.setFont(font);//选择需要用到的字体格式

        Cell cell;
        Row row;
        // 获取工作簿对象
        Sheet sheet = wb.createSheet("Sheet1");
        row = sheet.createRow(0);

        sheet.createFreezePane(0, 1, 0, 1);
        // 定义表头
        for (int i = 0; i < tableHeadValue.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(tableHeadValue[i]);
            sheet.setColumnWidth(i, (int) 7000);
        }

        // 存入数据
        int rowIndex = 1;

        // 设置数据的样式
        CellStyle hssfCellStyle = wb.createCellStyle();
        // 三、设置居中:
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        hssfCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 上下居中

        // 四、设置字体:
        Font font2 = wb.createFont();
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 11);//设置字体大小
        hssfCellStyle.setFont(font2);
        for (Map map : subList) {
            row = sheet.createRow(rowIndex++);
            int index = 0;
            for (int i = 0; i < tableHeadKey.length; i++) {
                cell = row.createCell(index++);
                cell.setCellStyle(hssfCellStyle);
                cell.setCellValue(map.get(tableHeadKey[i]) != null ? map.get(tableHeadKey[i]).toString() : "-");
            }
        }

        return wb;

    }

    /**
     * 下载文件
     *
     * @param response
     * @param filePath 文件路径
     * @author clj
     * @date 2018/3/31
     */
    public static void downFile(HttpServletResponse response, String filePath) {
        String name = filePath.split("/")[filePath.split("/").length - 1];
        //读取要下载的文件，保存到文件输入流
        try {
            // 设置头部信息
            response.setHeader(
                    "Content-disposition",
                    "attachment;filename="
                            + URLEncoder.encode(name, "UTF-8"));
            FileInputStream in = new FileInputStream(filePath);
            //创建输出流
            OutputStream out = response.getOutputStream();
            //创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区当中
            while ((len = in.read(buffer)) > 0) {
                //输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            //关闭文件输入流
            in.close();
            //关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件压缩并导出到客户端
     *
     * @param fileNames
     * @param zip
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void exportZip(HttpServletResponse response, List<String> fileNames, File zip)
            throws FileNotFoundException, IOException {
        OutputStream outPut = response.getOutputStream();

        //1.压缩文件
        File srcFile[] = new File[fileNames.size()];
        for (int i = 0; i < fileNames.size(); i++) {
            srcFile[i] = new File(fileNames.get(i));
        }
        byte[] byt = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        for (int i = 0; i < srcFile.length; i++) {
            FileInputStream in = new FileInputStream(srcFile[i]);
            out.putNextEntry(new ZipEntry(srcFile[i].getName()));
            int length;
            while ((length = in.read(byt)) > 0) {
                out.write(byt, 0, length);
            }
            out.closeEntry();
            in.close();
        }
        out.close();

        //2.删除服务器上的临时文件(excel)
        for (int i = 0; i < srcFile.length; i++) {
            File temFile = srcFile[i];
            if (temFile.exists() && temFile.isFile()) {
                temFile.delete();
            }
        }

        //3.返回客户端压缩文件
        FileInputStream inStream = new FileInputStream(zip);
        byte[] buf = new byte[4096];
        int readLenght;
        while ((readLenght = inStream.read(buf)) != -1) {
            outPut.write(buf, 0, readLenght);
        }
        inStream.close();
        outPut.close();

        //4.删除压缩文件
        if (zip.exists() && zip.isFile()) {
            zip.delete();
        }
    }


}
