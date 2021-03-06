package firefighter.core.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import firefighter.core.UniException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TablePDF extends TableData{
    public static final String fontFile="arial.ttf";
    public final static int colors[]={
        0xFFFFFFFF, //ColorNone=0;
        0xFFFFC0C0, //ColorRed=1;
        0xFFC0FFC0, //ColorGreen=2;
        0xFFC0C0FF, //ColorBlue=3;
        0xFFFFFFC0, //ColorYellow=4;
        0xFFDDDDDD, //ColorGrayDark=5;
        0xFFA0A0A0, //ColorGrayLight=6;
        0XFFFF6633  //int ColorBrown=7;
    };
    //protected String fontPath="c:/windows/fonts/"+fontFile;
    protected String fontPath=fontFile;     // 657-2 - в каталоге запуска сервера
    protected Font titleFont;
    protected Font cellHeaderFont;
    protected Font cellRegularFont;
    protected Font cellBottomFont;
    protected Font cellLinkFont;
    protected int cellHeight=26;
    protected int cellWidth=20;
    protected int cellWidthFirst=120;
    protected int cellWidthLast=30;
    private final static int LandscapeStringSize = 150;
    private final static int VerticalStringSize = 100;
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
            cellBottomFont = new Font(russianFont, 6, Font.NORMAL);
            cellLinkFont = new Font(russianFont, 6, Font.NORMAL);
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
            int symStringSize = paramList.isLandscape() ? LandscapeStringSize : VerticalStringSize;
            table = new PdfPTable(ncol);
            table.setWidthPercentage(100);
            int ww[] = new int[ncol];
            int size[] = new int[ncol];                 // Количество символов в столбце - реальное
            int sumWW=0;
            for (int i = 0; i < ww.length; i++){        //681 - приведение к процентам
                sumWW += cols.get(i).size;
                }
            for (int i = 0; i < ww.length; i++){
                int sz = cols.get(i).size;
                ww[i] = sz*100/sumWW;
                size[i] = symStringSize * ww[i] / 100;    // Реальное кол-во символов в строке
                }
            table.setWidths(ww);
            for (int j = 0; j < ncol; j++) {
                addColNumCell(table, j);
                }
            for (int j = 0; j < ncol; j++) {
                addHeaderCell(table, cols.get(j));
                }
            for (int i = 0; i < nrow; i++) {
                int nLines=1;
                for (int j = 0; j < ncol; j++) {
                    int nl = data.get(i).get(j).value.length()/size[j]+1;
                    if (nl > nLines)
                        nLines = nl;
                    }
                for (int j = 0; j < ncol; j++) {
                    addRegularCell(table, cols.get(j),nLines, data.get(i).get(j));
                    }
                }
            document.add(table);
            addEmptyLine(document);
            for(int i=0;i<cols.size();i++)
                if (cols.get(i).linkText.length()!=0)
                    document.add(new Paragraph(""+i+". "+cols.get(i).linkText, cellRegularFont));
            for(String ss : bottoms)
                document.add(new Paragraph(ss, cellRegularFont));
            } catch (Exception ee) {
                UniException.user(ee);
                }
        }

    private void addColNumCell(PdfPTable table, int num) {
        Phrase phrase = new Phrase(num==0 ? "":""+num, cellHeaderFont);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        }
    private void addHeaderCell(PdfPTable table, TableCol col) {
        Phrase phrase = new Phrase(col.name, cellHeaderFont);
        if (col.linkIndex!=0)
            phrase.add(new Chunk(""+col.linkIndex,cellLinkFont).setTextRise(5f));
        PdfPCell cell = new PdfPCell(phrase);
        if (verticalHeader) cell.setRotation(90);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (col.hexBackColor!=0)
            cell.setBackgroundColor(new BaseColor(colors[col.hexBackColor]));
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

    protected void addRegularCell(PdfPTable table, TableCol col, int nLines, TableCell tcell) {
        PdfPCell cell = new PdfPCell(new Phrase(tcell.value, cellRegularFont));
        cell.setFixedHeight(nLines*10+3);
        //cell.setPaddingBottom(3);
        int align=TableCol.AlignLeft;
        switch (col.align){
            case TableCol.AlignCenter: align = Element.ALIGN_CENTER; break;
            case TableCol.AlignLeft: align = Element.ALIGN_LEFT; break;
            case TableCol.AlignRight: align = Element.ALIGN_RIGHT; break;
            }
        cell.setHorizontalAlignment(align);

        if (tcell.selected)
            cell.setBackgroundColor(new BaseColor(0xe0e0e0));
        else
        if (tcell.hexBackColor!=0){
            cell.setBackgroundColor(new BaseColor(colors[tcell.hexBackColor]));
            }
        table.addCell(cell);
        }

    private void addMetaData(Document document) {
        document.addTitle(title);
        document.addSubject("Огнезащитная корпорация");
        document.addAuthor("Певзнер С.Э.");
        document.addCreator("Певзнер С.Э.");
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
    public  String getPdfContent(String pdfFile) throws IOException{
        PdfReader reader = new PdfReader(pdfFile);
        StringBuffer sb = new StringBuffer();
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            sb.append(strategy.getResultantText());
            }
        reader.close();
        return sb.toString();
        }
    public static void main(String sss[]) throws Exception {
        //generate(new TablePDF(),"pdf");
        //generate(new TableExcel(),"xls");
        //generate(new TableHTML(),"html");
        String ss = new TablePDF().getPdfContent("акт.pdf");
        System.out.println(ss);
    }

}
