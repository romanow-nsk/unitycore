package firefighter.core.reports;

import firefighter.core.UniException;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.base.StringList;

import java.util.ArrayList;

public abstract class ReportBase {
    protected long invertColumnMask=0;              // Инверсная маска столбцов
    public Artifact reportFile = new Artifact();
    public int reportType= ValuesBase.RepOther;
    abstract public void createReportFile(TableData table, String fspec) throws UniException;
    abstract public String getTitle();
    public ReportBase(int type){
        reportType = type;
        }
    public ReportBase(int type, long mask){
        invertColumnMask = mask;
        reportType = type;
        }
    public String getFileExt(){ return null; }
    public String testReportContent(){ return null; }
    public StringList getColNames(){
        StringList out = new StringList();
        ArrayList<TableCol> in = createHeader();
        for(TableCol ss : in)
            out.add(ss.name);
        return out;
        }
    abstract public ArrayList<TableCol> createHeader();
    }
