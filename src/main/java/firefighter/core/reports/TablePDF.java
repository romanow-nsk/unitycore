package firefighter.core.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import firefighter.core.UniException;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class TablePDF extends TableData{
    public static final String fontFile="arial.ttf";
    protected String fontPath="c:/windows/fonts/"+fontFile;
    protected Font titleFont;
    protected Font cellHeaderFont;
    protected Font cellRegularFont;
    protected static int cellHeight=13;
    protected static int cellWidth=20;
    protected static int cellWidthFirst=120;
    protected static int cellWidthLast=30;
    private PdfPTable table;
    private Document document;
    public void setFontPath(String path){
        fontPath = path;
        }
    @Override
    public String getFileExt() {
        return "pdf"; }
    public TablePDF(){}
    public TablePDF(DocumentParamList ss){
        super(ss);
        }
    @Override
    public void openReport(String titile, String fname) throws UniException {
        super.openReport(title,fname);
        try {
            BaseFont russianFont = BaseFont.createFont(fontPath, "cp1251", BaseFont.EMBEDDED);
            titleFont = new Font(russianFont, 12, Font.BOLD);
            cellHeaderFont = new Font(russianFont, 10, Font.NORMAL);
            cellRegularFont = new Font(russianFont, 8, Font.NORMAL);
            if (paramList.isLandscape())
                document = new Document(PageSize.A4.rotate());
            else
                document = new Document();
            document.setMargins(50, 20, 20, 20);
            PdfWriter.getInstance(document, new FileOutputStream(fname));
            document.open();
            addMetaData(document);
           } catch (Exception ee) { UniException.user(ee); }
        }

    @Override
    public void openPage(String title0, ArrayList<TableCol> cols0, int nrow0, boolean verticalHeader0) {
        super.openPage(title0, cols0, nrow0, verticalHeader0);
        }
    @Override
    public void savePage(){
        try {
            int ncol = cols();
            int nrow = rows();
            document.add(new Paragraph(title, titleFont));
            addEmptyLine(document);
            table = new PdfPTable(ncol);
            table.setWidthPercentage(100);
            int ww[] = new int[ncol];
            for (int i = 0; i < ww.length; i++)
                ww[i] = cols.get(i).size;
            table.setWidths(ww);
            for (int j = 0; j < ncol; j++) {
                addHeaderCell(table, cols.get(j));
                }
            for (int i = 0; i < nrow; i++) {
                for (int j = 0; j < ncol; j++) {
                    addRegularCell(table, data.get(i).get(j));
                }
            }
            document.add(table);
            addEmptyLine(document);
            for(String ss : bottoms)
                document.add(new Paragraph(ss, cellRegularFont));
            } catch (Exception ee) {
                UniException.user(ee);
                }
        }

    private void addHeaderCell(PdfPTable table, TableCol col) {
        PdfPCell cell = new PdfPCell(new Phrase(col.name, cellHeaderFont));
        if (verticalHeader) cell.setRotation(90);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (col.hexBackColor!=0)
            cell.setBackgroundColor(new BaseColor(col.hexBackColor));
        //cell.setPaddingBottom(3);
        table.addCell(cell);
        }

    @Override
    public void saveReport() throws UniException {
        try{
            document.close();
            } catch (Exception ee) {
                UniException.user(ee);
                }
        }

    protected void addRegularCell(PdfPTable table, TableCell tcell) {
        PdfPCell cell = new PdfPCell(new Phrase(tcell.value, cellRegularFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setFixedHeight(cellHeight);
        //cell.setPaddingBottom(3);
        if (tcell.selected)
            cell.setBackgroundColor(new BaseColor(0xe0e0e0));
        else
        if (tcell.hexBackColor!=0)
            cell.setBackgroundColor(new BaseColor(tcell.hexBackColor));
        table.addCell(cell);
        }

    private void addMetaData(Document document) {
        document.addTitle(title);
        document.addSubject("Огнезащитная корпорация");
        document.addAuthor("Евгений Романов");
        document.addCreator("Евгений Романов");
        }
    private void addEmptyLine(Document document) throws DocumentException {
        document.add(new Paragraph(" "));
        }
    public static void generate(TableData generator,String ext) throws UniException {
        ArrayList<TableCol> cols = new ArrayList<>();
        for(int i=0;i<10;i++){
            cols.add(new TableCol("name"+i,20+i*4));
            }
        generator.openReport("Тест","aaa."+ext);
        generator.openPage("Тест 1",cols,10,true);
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                generator.setCellValue(i,j,i*j);
                generator.setCellBackColor(i,j,0xFFFFFFFF);
                }
            }
        generator.savePage();
        generator.openPage("Тест 2",cols,20,false);
        for(int i=0;i<20;i++){
            for(int j=0;j<10;j++){
                generator.setCellValue(i,j,i+j);
                if ((i+j)%2==0)
                    generator.setCellBackColor(i,j,0xFFFFFFFF);
                else
                    generator.setCellBackColor(i,j,0xFFE0E0E0);
                //generator.setCellSelected(i,j,(i+j)%2==0);
                }
            }
        generator.savePage();
        generator.saveReport();
        }
    public static void main(String sss[]) throws UniException {
        generate(new TablePDF(),"pdf");
        generate(new TableExcel(),"xls");
        generate(new TableHTML(),"html");
    }

}
