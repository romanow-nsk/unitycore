package firefighter.core.jdbc;

import firefighter.core.UniException;
import firefighter.core.constants.TableItem;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityField;
import firefighter.core.mongo.DAO;
import firefighter.core.mongo.I_DBQuery;
import org.bson.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLFieldList  extends ArrayList<SQLField> {
    private int oidIdx=0;
    public int getOidIdx() {
        return oidIdx;
        }
    public SQLFieldList(){}

    public String getFieldPrefix(String parent,EntityField ff){
        String key = parent+"."+ff.name;
        String out = ValuesBase.PrefixMap.get(key);
        return out;
        }
    public String createFields(Entity ent) {
        return createFields(ent.getClass().getSimpleName(),false);
        }
    public String createFields(String table) {
        return createFields(table,false);
        }
    public String createFields(String table, boolean second) {
        TableItem item = ValuesBase.EntityFactory.getItemForSimpleName(table);
        if (item == null)
            return "Entity не найден: " + table;
        if (!second && !item.isTable)
            return "Not table: " + table;
        try {
            DAO ent = (DAO) (item.clazz.newInstance());
            item.createFields();
            ArrayList<EntityField> fields = item.getFields();
            for (int i = 0; i < fields.size(); i++) {
                EntityField ff = fields.get(i);
                if (ff.name.equals("oid"))
                    oidIdx = size();                // Текущий новый индекс
                switch (ff.type) {
                    case DAO.dbInt:
                        add(new SQLField(ff.name, ff.type, "int"));
                        break;
                    case DAO.dbDouble:
                        add(new SQLField(ff.name, ff.type, "real"));
                        break;
                    case DAO.dbBoolean:
                        add(new SQLField(ff.name, ff.type, "tinyint(1)"));
                        break;
                    case DAO.dbShort:
                        add(new SQLField(ff.name, ff.type, "tinyint(2)"));
                        break;
                    case DAO.dbLong:
                    case DAO.dbLink:
                        add(new SQLField(ff.name, ff.type, "long"));
                        break;
                    case DAO.dbLinkList:                                    // LinkList  как строка id,id,...
                    case DAO.dbString2:
                    case DAO.dbString:
                        add(new SQLField(ff.name, ff.type, "mediumtext"));
                        break;
                    case DAO.dbDAOLink:
                        SQLFieldList two = new SQLFieldList();
                        String ss = two.createFields(ff.field.getType().getSimpleName(),true);
                        if (ss!=null)
                            return ss;
                        String pref = getFieldPrefix(table,ff);
                        if (pref == null)
                            return "Не найден префикс для "+table+"."+ff.name;
                        for(SQLField field : two)
                            add(new SQLField(pref,field));                 // Добавить вторичные с префиксом
                        break;
                    }
                }
            } catch (Exception ee) {
                return ee.toString();
                }
        return null;
        }
    Document createDocument(ResultSet set) throws UniException {
        Document out = new Document();
        try {
        for(SQLField ff : this){
            switch (ff.DAOType){
                case DAO.dbInt:
                    out.append(ff.name, set.getInt(ff.name));
                    break;
                case DAO.dbDouble:
                    out.append(ff.name, set.getDouble(ff.name));
                    break;
                case DAO.dbBoolean:
                    out.append(ff.name, set.getBoolean(ff.name));
                    break;
                case DAO.dbShort:
                    out.append(ff.name, set.getShort(ff.name));
                    break;
                case DAO.dbLong:
                case DAO.dbLink:
                    out.append(ff.name, set.getLong(ff.name));
                    break;
                case DAO.dbLinkList:                                    // LinkList  как строка id,id,...
                case DAO.dbString2:
                case DAO.dbString:
                    out.append(ff.name, set.getString(ff.name));
                    break;
                    }
                }
            }catch (SQLException ee){
                throw UniException.sql(ee);
                }
        return out;
        }

    public String createUpdateString(org.bson.Document set) throws UniException{
        return createUpdateString(set,null);
        }
    public String createUpdateString(org.bson.Document set, String only) throws UniException{
        StringBuffer out = new StringBuffer();
        boolean first=true;
        try {
            for(SQLField ff : this){
                if (only!=null && !ff.name.equals(only))
                    continue;
                if (ff.name.equals("oid"))
                    continue;
                if (!first) out.append(',');
                else
                    first=false;
                switch (ff.DAOType){
                    case DAO.dbInt:
                        out.append(ff.name+"="+((Integer)set.get(ff.name)).intValue());
                        break;
                    case DAO.dbDouble:
                        out.append(ff.name+"="+((Double)set.get(ff.name)).doubleValue());
                        break;
                    case DAO.dbBoolean:
                        out.append(ff.name+"="+((Boolean)set.get(ff.name)).booleanValue());
                        break;
                    case DAO.dbShort:
                        out.append(ff.name+"="+((Short)set.get(ff.name)).shortValue());
                        break;
                    case DAO.dbLong:
                    case DAO.dbLink:
                        out.append(ff.name+"="+((Long)set.get(ff.name)).longValue());
                        break;
                    case DAO.dbLinkList:                                    // LinkList  как строка id,id,...
                    case DAO.dbString2:
                    case DAO.dbString:
                        out.append(ff.name+"=\'"+(String) set.get(ff.name)+"\'");
                        break;
                    }
                }
            }catch (Exception ee){
                throw UniException.sql(ee);
                }
        return out.toString();
        }
    public String createInsertString(org.bson.Document set) throws UniException{
        StringBuffer out = new StringBuffer();
        boolean first=true;
        try {
            out.append('(');
            for(SQLField ff : this) {
                if (ff.name.equals("oid"))
                    continue;
                if (!first) out.append(',');
                else
                    first = false;
                out.append(ff.name);
                }
            out.append(") VALUES (");
            first = true;
            for(SQLField ff : this){
                if (ff.name.equals("oid"))
                    continue;
                if (!first) out.append(',');
                else
                    first=false;
                switch (ff.DAOType){
                    case DAO.dbInt:
                        out.append(ff.name+"="+((Integer)set.get(ff.name)).intValue());
                        break;
                    case DAO.dbDouble:
                        out.append(ff.name+"="+((Double)set.get(ff.name)).doubleValue());
                        break;
                    case DAO.dbBoolean:
                        out.append(ff.name+"="+((Boolean)set.get(ff.name)).booleanValue());
                        break;
                    case DAO.dbShort:
                        out.append(ff.name+"="+((Short)set.get(ff.name)).shortValue());
                        break;
                    case DAO.dbLong:
                    case DAO.dbLink:
                        out.append(ff.name+"="+((Long)set.get(ff.name)).longValue());
                        break;
                    case DAO.dbLinkList:                                    // LinkList  как строка id,id,...
                    case DAO.dbString2:
                    case DAO.dbString:
                        out.append(ff.name+"=\'"+(String) set.get(ff.name)+"\'");
                        break;
                }
            }
        out.append(')');
        }catch (Exception ee){
            throw UniException.sql(ee);
            }
        return out.toString();
        }
}
