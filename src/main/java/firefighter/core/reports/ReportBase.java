package firefighter.core.reports;

import firefighter.core.UniException;
import firefighter.core.constants.Values;
import firefighter.core.entity.artifacts.Artifact;

public abstract class ReportBase {
    public Artifact reportFile = new Artifact();
    public int reportType= Values.RepOther;
    abstract public void createReportFile(TableData table, String fspec) throws UniException;
    abstract public String getTitle();
    public ReportBase(int type){
        reportType = type;
        }
    public String getFileExt(){ return null; }
    public String testReportContent(){ return null; }
    }
