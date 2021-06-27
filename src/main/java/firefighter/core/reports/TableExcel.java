package firefighter.core.reports;

import firefighter.core.UniException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TableExcel extends TableData{
    private DefaultFont defaultFont = new DefaultFont();
    private HSSFWorkbook workbook = new HSSFWorkbook();
    private HSSFSheet curSheet=null;
    private boolean isSheetCreated=false;
    private final static short HeadRowHight=500;
    private final static double midOver=1.5;
    private final static double midK=1.2;
    private final static double fontK=1.1;
    @Override
    public void openPage(String title0, ArrayList<TableCol> cols0, int nrow, boolean verticalHeader0) {
        super.openPage(title0.replace(":","-"),cols0,nrow,verticalHeader0);
        }
    @Override
    public void savePage() throws UniException {
        curSheet = workbook.createSheet(title);
        int ncol = cols();
        int nrow = rows();
        Workbook wb = curSheet.getWorkbook();
        CellStyle style1 = wb.createCellStyle();        // Тонкая рамка без выравнивания
        style1.setBorderTop   (CellStyle.BORDER_THIN);
        style1.setBorderRight (CellStyle.BORDER_THIN);
        style1.setBorderBottom(CellStyle.BORDER_THIN);
        style1.setBorderLeft  (CellStyle.BORDER_THIN);
        CellStyle style2 = wb.createCellStyle();        // Тонкая рамка, выравнивание по центру
        style2.setBorderTop   (CellStyle.BORDER_THIN);
        style2.setBorderRight (CellStyle.BORDER_THIN);
        style2.setBorderBottom(CellStyle.BORDER_THIN);
        style2.setBorderLeft  (CellStyle.BORDER_THIN);
        style2.setAlignment(CellStyle.ALIGN_CENTER);
        CellStyle style3 = wb.createCellStyle();        // Средняя рамка, выравнивание по центру
        style3.setBorderTop   (CellStyle.BORDER_MEDIUM);
        style3.setBorderRight (CellStyle.BORDER_MEDIUM);
        style3.setBorderBottom(CellStyle.BORDER_MEDIUM);
        style3.setBorderLeft  (CellStyle.BORDER_MEDIUM);
        style3.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setBoldweight((short) 1000);
        style3.setFont(font);
        //-----------------------------------------------------------------------------------------
        Row row = curSheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(title);
        row.setHeight(HeadRowHight);
        //------------------------- подсчет размерностей ячеек
        int ww[]=new int[ncol];
        int ws[]=new int[ncol];
        for(int i=0;i<ncol;i++){
            ww[i]=cols.get(i).name.length();
            ws[i] = ww[i];
            }
        for(int i=0;i<nrow;i++){
            for(int j=0;j<ncol;j++){
                int ss = data.get(i).get(j).value.length();
                ws[j]+=ss;
                if (ss > ww[j])
                    ww[j]=ss;
                }
            }
        for(int i=0;i<ncol;i++){            // Размерность в символах
            ws[i]/=nrow;                    // Средняя длина
            int sz = ww[i];                 // По максимуму
            //if (sz > ws[i]*midOver)
            //    sz = (int)(ws[i]*midK);     // По среднему
            if (cols.get(i).size>sz)
                sz = cols.get(i).size;      // Заявленный размер
            sz = (int)(sz * fontK * 256);
            curSheet.setColumnWidth(i,sz);
            }
        row = curSheet.createRow(1);
        for(int i=0;i<ncol;i++){
            cell = row.createCell(i);
            if (i!=0)
                cell.setCellValue(""+i);
            cell.setCellStyle(style2);
            }
        row = curSheet.createRow(2);
        for(int i=0;i<ncol;i++){
            cell = row.createCell(i);
            cell.setCellValue(cols.get(i).name);
            cell.setCellStyle(style3);
            }
        for(int i=0;i<nrow;i++){
            row = curSheet.createRow(i+3);
            for(int j=0;j<ncol;j++){
                cell = row.createCell(j);
                cell.setCellValue(data.get(i).get(j).value);
                CellStyle cc = cell.getCellStyle();
                cc.setFillBackgroundColor((short) data.get(i).get(j).hexBackColor);     //??????
                cc.setFillForegroundColor((short) data.get(i).get(j).hexTextColor);     //??????
                if (cols.get(j).align==TableCol.AlignCenter)
                    cell.setCellStyle(style2);
                else
                    cell.setCellStyle(style1);
                }
            }
        CellStyle style4 = wb.createCellStyle();
        font = wb.createFont();
        font.setFontHeightInPoints((short)8);
        font.setFontName("Arial");
        style4.setFont(font);
        style4.setAlignment(CellStyle.ALIGN_LEFT);
        for(int i=0;i<bottoms.size();i++){
            row = curSheet.createRow(i+nrow+4);
            cell = row.createCell(0);
            cell.setCellValue(bottoms.get(i));
            cell.setCellStyle(style4);
            }
        }

    @Override
    public String getFileExt() {
        return "xls"; }
    @Override
    public void saveReport() throws UniException {
        try {
            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
            } catch (IOException e) { UniException.io(e); }
        }
    public static void main(String ss[]){
        System.out.println(CellRangeAddress.valueOf("$A$2:$B$3"));
    }
}
