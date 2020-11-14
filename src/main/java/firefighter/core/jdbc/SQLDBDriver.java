package firefighter.core.jdbc;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import firefighter.core.ParamList;
import firefighter.core.UniException;
import firefighter.core.constants.TableItem;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityField;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.EntityNamed;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.mongo.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SQLDBDriver extends I_MongoDB {
    private I_JDBCConnector jdbc=null;
    public SQLDBDriver(I_JDBCConnector connector){
        jdbc = connector;
        }
    private boolean testDB(int port){
        return true;
        }
    @Override
    public boolean openDB(int port) throws UniException {
        clearCash();
        try {
            ParamList paramList = new ParamList().add("DBUser",ValuesBase.SQLDBUser).add("DBPass",ValuesBase.SQLDBPass)
                    .add("DBProxyOn",false).add("DBIP",ValuesBase.SQLDBIP).add("DBPort",ValuesBase.SQLDBPort)
                    .add("DBName",ValuesBase.env().mongoDBName()+port);
            jdbc.connect(paramList);
            } catch (Exception ee){
                System.out.println(ee);
                return false;
                }
        return isOpen();
        }

    @Override
    public boolean isOpen(){
        return  jdbc!=null && jdbc.isConnected();
        }
    @Override
    public void closeDB(){
        clearCash();
        if (!isOpen()) return;
        try {
            jdbc.close();
            } catch (UniException e) {}
        }
    public void showTables(){
        }

    @Override
    public void createIndex(Entity entity, String name) throws UniException {
        String tbl = table(entity);
        String sql = "CREATE  INDEX "+name+" ON "+tbl+"("+name+");";
        jdbc.execSQL(sql);
        }

    @Override
    public int getCountByQuery(Entity ent, BasicDBObject query) throws UniException {
        throw UniException.sql("BasicDBObject не поддерживается");
        }

    @Override
    public EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query, int level, String pathList, RequestStatistic statistic) throws UniException {
        throw UniException.sql("BasicDBObject не поддерживается");
        }

    private Document findOne(MongoCollection table, BasicDBObject query){
        MongoCursor<Document> cursor = table.find(query).iterator();
        return cursor.hasNext() ? cursor.next() : null;
        }


    @Override
    public int getCountByQuery(Entity ent, I_DBQuery query) throws UniException{
        SQLFieldList flist = new SQLFieldList();
        String ss = flist.createFields(ent);
        String sql = "SELECT COUNT(*) FROM "+table(ent)+" WHERE "+flist.createWhere(query)+";";
        ResultSet res = jdbc.selectOne(sql);
        try {
            return res.getInt(0);
            } catch (SQLException e) {
                throw UniException.sql(e);
                }
        }
    @Override
    public EntityList<Entity> getAllByQuery(Entity ent, I_DBQuery query, int level, String pathsList, RequestStatistic statistic) throws UniException{
        HashMap path = pathsList.length()!=0 ? parsePaths(pathsList) : null;
        SQLFieldList flist = new SQLFieldList();
        String ss = flist.createFields(ent);
        StringBuffer err = new StringBuffer();
        if (ss!=null)
            throw UniException.sql(ss);
        final EntityList<Entity> out = new EntityList<>();
        String sql = "SELECT * FROM "+table(ent)+(query==null ? "" : " WHERE "+flist.createWhere(query))+" ORDER BY oid;";
        jdbc.selectMany(sql, new I_OnRecord(){
            @Override
            public void onRecord(ResultSet rs) {
                if (statistic!=null)
                    statistic.entityCount++;
                try {
                    Document obj = flist.createDocument(rs);
                    Entity xx = (Entity) ent.getClass().newInstance();
                    xx.getData("", obj, level, SQLDBDriver.this,path,statistic);
                    out.add(xx);
                } catch (Exception e) {
                    err.append(e.toString()+"\n");
                }
            }
        });
        String zz = err.toString();
        if (zz!=null && zz.length()!=0)
            throw UniException.sql(zz);
        return out;
    }
    //----------------------------------------------------------------------------------------
    @Override
    public boolean delete(Entity entity, long id, boolean mode) throws UniException{
        if (!getById(entity,id,0,mode,null,null))
            return false;
        entity.setValid(mode);
        update(entity,0);
        return true;
        }
    @Override
    synchronized public boolean getByIdAndUpdate(Entity ent, long id, I_ChangeRecord todo) throws UniException{
        boolean bb = getById(ent,id,0,null);
        if (!bb) return false;
        bb = todo.changeRecord(ent);
        if (!bb) return false;
        update(ent,0);
        return true;
        }

    @Override
    public boolean getById(Entity ent, long id, int level, boolean mode, HashMap<String,String> path,RequestStatistic statistic) throws UniException{
        if (isCashOn()){
            Entity src = getCashedEntity(ent,id);
            if (src!=null) {
                ent.loadDBValues(src,level,this);
                incCashCount(true);
                return ent.isValid()!=mode;
                }
            incCashCount(false);
            }
        SQLFieldList flist = new SQLFieldList();
        String ss = flist.createFields(ent);
        if (ss!=null)
            throw UniException.sql(ss);
        String sql="SELECT ";
        for (int i=0;i<flist.size();i++) {
            if (i!=0) sql+=",";
            sql+=flist.get(i).name;
            }
        sql+=" FROM "+table(ent)+" WHERE oid="+id+";";
        ResultSet set = jdbc.selectOne(sql);
        Document result = flist.createDocument(set);
        ent.getData("", result,level,this,path,statistic);
        updateCashedEntity(ent);
        if (statistic!=null)
            statistic.entityCount++;
        return ent.isValid()!=mode;
        }
    @Override
    public synchronized long nextOid(Entity ent,boolean fromEntity) throws UniException {
        return jdbc.newRecord(table(ent));
        }

    @Override
    public synchronized long lastOid(Entity ent) throws UniException {
        ResultSet ss = jdbc.selectOne("SELECT MAX(oid) FROM "+table(ent)+";");
        try {
            if (ss != null)
                return ss.getLong("oid") + 1;
            } catch (Exception ee){
                throw UniException.sql(ee);
                }
        return 1;           // Нет записей
        }

    @Override
    public void remove(Entity entity, long id) throws UniException {
        String sql="DELETE FROM "+table(entity)+" WHERE oid="+id+";";
        jdbc.execSQL(sql);
        removeCashedEntity(entity);
        }

    @Override
    public synchronized boolean updateField(Entity ent, long id, String fname, String prefix) throws UniException {
        removeCashedEntity(ent,id);
        SQLFieldList flist = new SQLFieldList();
        String ss = flist.createFields(ent);
        if (ss!=null)
            throw UniException.sql(ss);
        Document document = new Document();
        ent.putData("", document,0,this);
        String sql="UPDATE "+table(ent)+" SET "+flist.createUpdateString(document,prefix+fname);
        sql+=" WHERE oid="+ent.getOid()+";";
        jdbc.execSQL(sql);
        return true;
        }

    private String table(Entity ent){
        return ent.getClass().getSimpleName();
        }

    @Override
    public void dropTable(Entity ent) throws UniException {
        clearCash(ent);
        String sql="DROP TABLE `"+table(ent)+"`;";
        jdbc.execSQL(sql);
        }

    @Override
    public long add(Entity ent, int level, boolean ownOid) throws UniException {
        long id;
        if (!ownOid){
            id = nextOid(ent);
            ent.setOid(id);
            update(ent,level);
            }
        else{
            id = ent.getOid();
            SQLFieldList flist = new SQLFieldList();
            String ss = flist.createFields(ent);
            if (ss!=null)
                throw UniException.sql(ss);
            Document document = new Document();
            ent.putData("", document,level,this);
            String sql="INSERT INTO "+table(ent)+" "+flist.createInsertString(document)+";";
            jdbc.execSQL(sql);
            }
        return id;
        }

    @Override
    public void update(Entity ent, int level) throws UniException {
        SQLFieldList flist = new SQLFieldList();
        String ss = flist.createFields(ent);
        if (ss!=null)
            throw UniException.sql(ss);
        Document document = new Document();
        ent.putData("", document,level,this);
        String sql="UPDATE "+table(ent)+" SET "+flist.createUpdateString(document);
        sql+=" WHERE oid="+ent.getOid()+";";
        jdbc.execSQL(sql);
        updateCashedEntity(ent);
        }

    public EntityList<Entity> getAllRecords(Entity ent, int level, String pathsList,RequestStatistic statistic) throws UniException{
        return getAllByQuery(ent,(I_DBQuery) null,level,pathsList,statistic);
        }
    @Override
    public EntityList<EntityNamed> getListForPattern(Entity ent, String pattern) throws UniException {
        throw UniException.noFunc("Чтение по шаблону не поддерживается");
        }
    @Override
    public String clearDB(){
        clearCash();
        Object olist[] = ValuesBase.EntityFactory.classList().toArray();
        String out="";
        TableItem item=null;
        for(int i=0;i<olist.length;i++){
            try {
                item = (TableItem)olist[i];
                if (!item.isTable)
                    continue;
                Entity ent = (Entity)(item.clazz.newInstance());
                dropTable(ent);
                ent.setOid(1);
                ent.setValid(false);
                add(ent,0,true);
                createIndex(ent,"oid");
                createIndex(ent,"valid");
                for(String ss : item.indexes)
                    createIndex(ent,ss);
            } catch (Exception ee){
                String ss = "Не могу создать "+ ValuesBase.EntityFactory.get(item.clazz.getSimpleName())+"\n"+ee.toString();
                System.out.println(ss);
                out+=ss;
                }
            }
    //    try {
    //        add(ValuesBase.superUser,0,false);
    //        } catch (UniException e) {
    //            String ss = "Не могу создать суперадмина \n"+e.toString()+"\n";
    //            System.out.print(ss);
    //            out+=ss;
    //            }
        return out;
        }
    @Override
    public String clearTable(String table) throws UniException {
        try {
            TableItem item = ValuesBase.EntityFactory.getItemForSimpleName(table);
            if (item==null)
                return "Entity не найден: "+table;
            if (!item.isTable)
                return "Not table: "+table;
            Entity ent = (Entity)(item.clazz.newInstance());
            clearCash(ent);
            dropTable(ent);
            SQLFieldList flist = new SQLFieldList();
            String ss = flist.createFields(table);
            if (ss!=null)
                return ss;
            String sql="CREATE TABLE "+table(ent)+" (";
            for (SQLField ff : flist){
                if (ff.name.equals("oid")){
                    sql+="oid INT NOT NULL AUTO_INCREMENT ,";
                    continue;
                    }
                sql += " "+ff.name+" "+ff.type+",";
                }
            sql+="PRIMARY KEY (oid));";
            jdbc.execSQL(sql);
            createIndex(ent,"oid");
            createIndex(ent,"valid");
            for(String zz : item.indexes)
                createIndex(ent,zz);
            } catch (Exception ee){
                String ss = "Не могу создать "+ ValuesBase.EntityFactory.get(table+"\n"+ee.toString());
                System.out.println(ss);
                return ss;
                }
        return "";
        }
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String ss[]){
        I_MongoDB db = new SQLDBDriver(new MySQLJDBC());
        try {
            db.openDB(4567);
            BugMessage bb = new BugMessage();
            bb = new BugMessage("aaaaaaaaaaaaaaaaaaaaaa");
            db.add(bb);
            bb = new BugMessage("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            db.add(bb);
            System.out.println(db.getAll(bb));
            /*
            EntityList<EntityNamed> zz = db.getListForPattern(new Facility(),"кор");
            for(EntityNamed ee : zz)
                System.out.println(ee.getOid()+" "+ee.getName());
            User us = new User();
            Technician tc = new Technician();
            db.dropTable(us);
            db.dropTable(tc);
            db.dropTable(new Shift());
            db.dropTable(new Artifact());
            long id1 = db.add(new User(ValuesBase.UserBossType,"Турков","Александр","Иванович","boss","1234",""));
            long id2 = db.add(new User(0,"Иванов","Иван","Иванович","","1234","9139999999"));
            long id3 = db.add(new User(ValuesBase.UserBookKeeperType,"Юдина","Наталья","Семеновна","","1234","89131234567"));
            System.out.println(db.getAll(us));
            db.deleteById(new User(),id2,false);
            System.out.println(db.getAll(us));
            System.out.println(db.getAll(tc));
            db.showTables();
            */
            db.closeDB();
        } catch (UniException e) { System.out.println(e.toString()); }
    }

}
