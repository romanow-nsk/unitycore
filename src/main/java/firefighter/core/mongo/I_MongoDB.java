package firefighter.core.mongo;

import com.mongodb.BasicDBObject;
import firefighter.core.UniException;
import firefighter.core.constants.Values;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.EntityNamed;

import java.util.HashMap;

public abstract class I_MongoDB {
    public abstract boolean openDB(int port) throws UniException;
    public abstract boolean isOpen();
    public abstract void closeDB();
    public abstract String clearDB() throws UniException;
    public abstract String clearTable(String table) throws UniException;
    public abstract void createIndex(Entity entity, String name) throws UniException;
    public abstract int getCountByQuery(Entity ent, BasicDBObject query) throws UniException;
    public abstract EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query, int level) throws UniException;
    public abstract boolean delete(Entity entity, long id, boolean mode) throws UniException;
    public abstract boolean getByIdAndUpdate(Entity ent, long id, I_ChangeRecord todo) throws UniException;
    public abstract boolean getById(Entity ent, long id, int level, boolean mode) throws UniException;
    public abstract void dropTable(Entity ent) throws UniException;
    public abstract long add(Entity ent, int level,boolean ownOid) throws UniException;
    public abstract void update(Entity ent, int level) throws UniException;
    public abstract EntityList<Entity> getAllRecords(Entity ent, int level) throws UniException;
    public abstract EntityList<EntityNamed> getListForPattern(Entity ent, String pattern) throws UniException;
    public abstract long nextOid(Entity ent,boolean fromEntity) throws UniException;
    public abstract void remove(Entity entity, long id) throws UniException;
    //------------------------ Синхронизированное обновление поля ПО ВСЕЙ БД 628
    public abstract boolean updateField(Entity src, long id, String fname, String prefix) throws UniException;
    public boolean updateField(Entity src, String fname, String prefix) throws UniException{
        return updateField(src,src.getOid(),fname,prefix);
        }
    public boolean updateField(Entity src, String fname) throws UniException{
        return updateField(src,src.getOid(),fname,"");
    }
    //------------------------ Умолчания ----------------------------------
    public void remove(Entity entity) throws UniException{
        remove(entity,entity.getOid());
        }
    public synchronized long nextOid(Entity ent) throws UniException{
        return nextOid(ent,false);
        }
    public EntityList<Entity> getAll(Entity ent, int mode, int level) throws UniException{
        switch (mode){
            case Values.GetAllModeTotal:
                return getAllRecords(ent,level);
            case Values.GetAllModeActual:
                return getAllByQuery(ent,new BasicDBObject("valid", true),level);
            case Values.GetAllModeDeleted:
                return getAllByQuery(ent,new BasicDBObject("valid", false),level);
            default:
                throw UniException.bug("MongoDB:Illegal get mode="+mode);
            }
        }
    public long add(Entity ent, int level) throws UniException{
        return add(ent,level,false);
        }
    public long add(Entity ent) throws UniException{
        return add(ent,0);
        }
    public void update(Entity ent) throws UniException{
        update(ent,0);
        }
    public EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query) throws UniException{
        return getAllByQuery(ent,query,0);
        }
    public boolean delete(Entity entity) throws UniException{
        return delete(entity,entity.getOid(),false);
        }
    public boolean delete(Entity entity,long id) throws UniException{
        return delete(entity,id,false);
    }
    public EntityList<Entity> getAll(Entity ent) throws UniException{
        return getAll(ent, Values.GetAllModeActual,0);
        }
    public boolean getById(Entity ent, long id) throws UniException{
        return getById(ent,id,0);
        }
    public boolean getById(Entity ent, long id, int level) throws UniException{
        return  getById(ent, id, level,Values.DeleteMode);
        }
    //-------------------------------------- КЭШ объектов ------------------------------------------
    private int totalGetCount=0;            // Общее количество чтений
    private int cashGetCount=0;             // Количество чтений из кэша
    private volatile boolean cashOn=false;  // Кэширование включено
    private HashMap<String,HashMap<Long,Entity>> cash=new HashMap<>();  // Двухуровневая хэш-таблица
    //-----------------------------------------------------------------------------------------------
    public int getTotalGetCount() {
        return totalGetCount; }
    public int getCashGetCount() {
        return cashGetCount; }
    public void incCashCount(boolean success){
        if (success)
            cashGetCount++;
        totalGetCount++;
        }
    public synchronized boolean isCashOn() {
        return cashOn; }
    public synchronized void setCashOn(boolean cashOn) {
        this.cashOn = cashOn;
        clearCash();
        }
    public synchronized void clearCash(){           // Полная очистка кэша
        totalGetCount=0;
        cashGetCount=0;
        cash = new HashMap<>();
        System.gc();                                // с вызовом сборщика мусора
        }
    public synchronized void clearCash(Entity ent){ // Очистка кэша класса
        cash.remove(ent.getClass().getSimpleName());
        System.gc();
    }
    public  synchronized Entity getCashedEntity(Entity proto, long id){
        if (!cashOn) return null;
        HashMap<Long,Entity> map = cash.get(proto.getClass().getSimpleName());
        if (map==null)
            return null;
        return map.get(id);                        // Извлечение из кэша
        }
    public  synchronized void removeCashedEntity(Entity proto){
        removeCashedEntity(proto,proto.getOid());
        }
    public  synchronized void removeCashedEntity(Entity proto, long oid){
        if (!cashOn) return;
        HashMap<Long,Entity> map = cash.get(proto.getClass().getSimpleName());
        if (map==null)
            return;
        map.remove(oid);                        // Удаление из кэша
        }
    public  synchronized void updateCashedEntity(Entity proto){
        if (!cashOn) return;
        String name = proto.getClass().getSimpleName();
        HashMap<Long,Entity> map = cash.get(name);
        if (map==null){
            map = new HashMap();
            cash.put(name,map);                     // Обновление в кэше
            }
        else
            map.remove(proto.getOid());
        map.put(proto.getOid(),proto);
        }
}
