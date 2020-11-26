package firefighter.core.export;

import firefighter.core.UniException;
import firefighter.core.entity.Entity;
import firefighter.core.mongo.I_MongoDB;
import firefighter.core.utils.FileNameExt;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;

public interface I_Excel {
    public void exportData(ArrayList<Entity> data) throws UniException;
    public void exportHeader(Entity ent) throws UniException;
    public int sheetCount();
    public String testSheetHeader(Entity proto, int idx);
    public String importSheet(Entity proto, int idx, I_MongoDB mongo) throws UniException;
    public void save(FileNameExt fspec) throws UniException;
    public void save(String fullName) throws UniException;
    public String load(FileNameExt fspec, I_MongoDB mongo);
    public String load(String fullName, I_MongoDB mongo);
}
