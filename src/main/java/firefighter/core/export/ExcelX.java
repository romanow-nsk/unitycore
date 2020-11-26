package firefighter.core.export;

import firefighter.core.UniException;
import firefighter.core.Utils;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.Entity;
import firefighter.core.mongo.I_MongoDB;
import firefighter.core.utils.FileNameExt;



import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelX implements I_Excel{
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;
    private ExCellCounter cnt;

    public void exportData(ArrayList<Entity> data) throws UniException {
        for(Entity ee : data){
            Row row = sheet.createRow(cnt.getIdx());
            ee.putData(row,new ExCellCounter());
            }
        }
    public void exportHeader(Entity ent) throws UniException {
        sheet = workbook.createSheet(ent.getClass().getSimpleName());
        cnt = new ExCellCounter();
        ExCellCounter hdCnt = new ExCellCounter();
        Row hd = sheet.createRow(cnt.getIdx());
        ArrayList<String> head = new ArrayList<>();
        ent.putHeader("",head);
        for(String ss : head)
            hd.createCell(hdCnt.getIdx()).setCellValue(ss);
        }

    public int sheetCount(){
        return workbook.getNumberOfSheets();
        }
    public String testSheetHeader(Entity proto,int idx)  {
        String pp;
        String ss1 = workbook.getSheetAt(idx).getSheetName();
        String ss2 = proto.getClass().getSimpleName();
        if (!ss1.equals(ss2)) {
            pp = "Разные таблицы: " + ss1 + " " + ss2;
            System.out.println(pp);
            return pp+"\n";
            }
        Row hd = workbook.getSheetAt(idx).getRow(0);
        ArrayList<String> list = new ArrayList<>();
        try {
            proto.putHeader("",list);
            } catch (UniException e) {
                System.out.println(e.toString());
                return e.toString()+"\n";
                }
        int cc =0;
        String xx ="";
        for(int i=0;i<list.size();i++){
            String s1 = "";
            try {
                s1 = hd.getCell(i).getStringCellValue();
                } catch (Exception ee){}
            String s2 = list.get(i);
            if (!s1.equals(s2)){
                pp = "Столбец:"+i+" "+s1+" "+s2;
                System.out.println(pp);
                xx+=pp+"\n";
                cc++;
                }
            }
        return cc==0 ? null : xx;
        }
    public String importSheet(Entity proto,int idx,I_MongoDB mongo) throws UniException{
        try {
            String zz = testSheetHeader(proto,idx);
            if (zz!=null)
                return zz;
            Sheet sh = workbook.getSheetAt(idx);
            ArrayList<Entity> out = new ArrayList<>();
            int sz = sh.getLastRowNum();
            if (sz<=0){
                return "Пустая таблица "+proto.getClass().getSimpleName()+"\n";
                }
            for (int i = 1; i <= sz; i++) {     // Пропустить пустую
                Entity xx = proto.getClass().newInstance();
                Row row = sh.getRow(i);
                xx.getData(row,new ExCellCounter());
                mongo.add(xx,0,true);
                }
            return "Импортирован класс:"+sh.getSheetName() +" "+(sz-1)+" записей\n";
            } catch (Exception ee){
                String ss = Utils.createFatalMessage(ee);
                System.out.println(ss);
                return ss+"\n";
                }
            }
    public void save(FileNameExt fspec) throws UniException{
        save(fspec.fullName());
        }
    public void save(String fullName) throws UniException{
        try (FileOutputStream out = new FileOutputStream(new File(fullName))) {
            workbook.write(out);
            } catch (IOException e) { UniException.io(e); }
        }
    public String load(FileNameExt fspec, I_MongoDB mongo) {
        return load(fspec.fullName(),mongo);
        }
    public String load(String fullName, I_MongoDB mongo) {
        String xx ="",pp;
        try (FileInputStream out = new FileInputStream(new File(fullName))) {
            workbook = new XSSFWorkbook(out);
            int ns = workbook.getNumberOfSheets();
            for(int i=0;i<ns;i++){
                Sheet sh = workbook.getSheetAt(i);
                String ss = sh.getSheetName();
                try {
                    Class zz = ValuesBase.EntityFactory.getClassForSimpleName(ss);
                    if (zz==null){
                        pp = "Класс не найден "+ss;
                        xx+=pp+"\n";
                        System.out.println(pp);
                        continue;
                        }
                    Entity proto = (Entity)zz.newInstance();
                    mongo.dropTable(proto);
                    String vv = importSheet(proto,i,mongo);
                    xx+=vv;
                    } catch (Exception e2) {
                        System.out.println(e2.toString());
                        xx+=e2.toString()+"\n";
                        }
                }
            } catch (Exception e) {
                pp = e.toString();
                xx+=pp+"\n";
                System.out.println(pp);
                }
        return xx;
        }
    }
