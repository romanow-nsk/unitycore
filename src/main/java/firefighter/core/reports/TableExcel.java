package firefighter.core.reports;

import firefighter.core.UniException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TableExcel extends TableData{
    private DefaultFont defaultFont = new DefaultFont();
    private HSSFWorkbook workbook = new HSSFWorkbook();
    private HSSFSheet curSheet=null;
    private boolean isSheetCreated=false;
    @Override
    public void openPage(String title0, ArrayList<TableCol> cols0, int nrow, boolean verticalHeader0) {
        super.openPage(title0,cols0,nrow,verticalHeader0);
        curSheet = workbook.createSheet(title0);
        Row row = curSheet.createRow(0);
        int ncol = cols();
        for(int i=0;i<ncol;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(cols0.get(i).name);
            }
        }
    @Override
    public void savePage() throws UniException {
            int ncol =cols();
            int nrow = rows();
            for(int i=0;i<nrow;i++){
                Row row = curSheet.createRow(i+1);
                for(int j=0;j<ncol;j++){
                    Cell cell = row.createCell(j);
                    cell.setCellValue(data.get(i)[j].value);
                    CellStyle cc = cell.getCellStyle();
                    cc.setFillBackgroundColor((short) data.get(i)[j].hexBackColor);
                    cc.setFillForegroundColor((short) data.get(i)[j].hexTextColor);
                    }
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
}
