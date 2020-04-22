package firefighter.core.mongo;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import firefighter.core.API.InnerAPIFace;
import firefighter.core.DBRequest;
import firefighter.core.UniException;
import firefighter.core.Utils;
import firefighter.core.constants.Values;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.EntityNamed;
import firefighter.core.entity.baseentityes.JBoolean;
import firefighter.core.entity.baseentityes.JEmpty;
import firefighter.core.entity.baseentityes.JLong;
import firefighter.core.entity.baseentityes.JString;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;


public class MongoDBProxy extends I_MongoDB{
    Retrofit localRetrofit=null;
    InnerAPIFace proxyService=null;
    Gson gson = new Gson();
    //-----------------------------------------------------------------------------------------
    public MongoDBProxy(){
        }
    @Override
    public boolean openDB(int port){
        localRetrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:"+(port+ Values.ProxyServerPortOffset))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        proxyService = localRetrofit.create(InnerAPIFace.class);
        return true;
        }
    @Override
    public boolean isOpen() {
        return true;
        }
    @Override
    public void closeDB() {
        }
    @Override
    public String clearDB() throws UniException {
        try {
            Response<JString> res = proxyService.clearDB().execute();
            if (!res.isSuccessful())
                throw UniException.net(Utils.httpError(res));
            else
                return res.body().getValue();
            } catch (IOException e) {  throw UniException.io(e); }
        }
    @Override
    public String clearTable(String table) throws UniException{
        try {
            Response<JString> res = proxyService.clearTable(table).execute();
            if (!res.isSuccessful())
                throw UniException.net(Utils.httpError(res));
            else
                return res.body().getValue();
            } catch (IOException e) {  throw UniException.io(e); }
        }
    @Override
    public void createIndex(Entity entity, String name) throws UniException {
        }
    @Override
    public int getCountByQuery(Entity ent, BasicDBObject query) throws UniException {
        return 0;
        }
    @Override
    public EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query, int level) throws UniException {
        try {
            String queryString = query.toString();
            //System.out.println("!!!! "+queryString);
            Response<ArrayList<DBRequest>> res = proxyService.getByQuery(ent.getClass().getSimpleName(),queryString,level).execute();
            if (!res.isSuccessful()){
                throw UniException.net(Utils.httpError(res));
                }
            else{
                EntityList<Entity> out = new EntityList<>();
                for (DBRequest rr : res.body()){
                    out.add(rr.get(gson));
                    }
                return out;
                }
            } catch (IOException e) {
                throw UniException.io(e);
                }
            }
    @Override
    public boolean getByIdAndUpdate(Entity ent, long id, I_ChangeRecord todo) throws UniException {
        return false;
        }
    @Override
    public boolean getById(Entity ent, long id, int level, boolean mode) throws UniException {
        try {
            Response<DBRequest> res = proxyService.getEntity(ent.getClass().getSimpleName(),id,mode,level).execute();
            if (!res.isSuccessful()){
                throw UniException.net(Utils.httpError(res));
                }
            else{
                Entity ent2 = res.body().get(gson);
                ent.copyDBValues(ent2);
                }
            } catch (IOException e) {
                throw UniException.io(e);
                }
        return true;
        }
    @Override
    public void dropTable(Entity ent) throws UniException {
        try {
            Response<JEmpty> res = proxyService.dropTable(ent.getClass().getSimpleName()).execute();
            if (!res.isSuccessful()){
                throw UniException.net(Utils.httpError(res));
                }
            } catch (IOException e) {  throw UniException.io(e); }
        }
    @Override
    public long add(Entity ent, int level,boolean ownOid) throws UniException {
        try {
            Response<JLong> res = proxyService.addEntity(new DBRequest(ent,gson),level,ownOid).execute();
            if (!res.isSuccessful()){
                throw UniException.net(Utils.httpError(res));
                }
            else{
                return res.body().getValue();
                }
            } catch (IOException e) {  throw UniException.io(e); }
        }
    @Override
    public void update(Entity ent, int level) throws UniException {
        try {
            Response<JEmpty> res = proxyService.updateEntity(new DBRequest(ent,gson),level).execute();
            if (!res.isSuccessful()){
                throw UniException.net(Utils.httpError(res));
                }
            } catch (IOException e) {  throw UniException.io(e); }
        }
    @Override
    public EntityList<Entity> getAll(Entity ent, int mode, int level) throws UniException {
        try {
            Response<ArrayList<DBRequest>> res = proxyService.getEntityList(ent.getClass().getSimpleName(),mode,level).execute();
            if (!res.isSuccessful()){
                throw UniException.net(Utils.httpError(res));
                }
            else{
                EntityList<Entity> out = new EntityList<>();
                for (DBRequest rr : res.body()){
                    out.add(rr.get(gson));
                    }
                return out;
                }
            } catch (IOException e) {
                throw UniException.io(e);
                }
        }
    @Override
    public EntityList<Entity> getAllRecords(Entity ent, int level) throws UniException {
        return getAll(ent,Values.GetAllModeActual,level);
        }
    @Override
    public EntityList<EntityNamed> getListForPattern(Entity ent, String pattern) throws UniException {
        return null;
        }
    @Override
    public long nextOid(Entity ent, boolean fromEntity) throws UniException {
        return 0;
        }
    @Override
    public void remove(Entity entity, long id) throws UniException {
        }

    @Override
    public boolean updateField(Entity src, long id, String fname, String prefix) throws UniException {
        return false;
    }

    @Override
    public boolean delete(Entity entity, long id, boolean mode) throws UniException {
        try {
            Response<JBoolean> res = proxyService.removeEntity(entity.getClass().getSimpleName(),id,mode).execute();
            if (!res.isSuccessful()){
                throw UniException.net(Utils.httpError(res));
                }
            return res.body().value();
        } catch (IOException e) {  throw UniException.io(e); }
    }
}
