package firefighter.core.mongo;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;

public class DBQuery {
    private List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    public final static int ModeAnd=0;
    public final static int ModeOr=1;
    public final static int ModeGTE=0;
    public final static int ModeLT=1;
    public final static int ModeLTE=2;
    private String modeList[]={"$and","$or"};
    private String cmpList[]={"$gte","$lt","$lte"};
    private int mode = ModeAnd;
    public DBQuery(){}
    public DBQuery(int mode0){
        mode = mode0;
        }
    public DBQuery add(DBQuery cc){
        obj.add(cc.getQuery());
        return this;
        }
    public DBQuery add(String field, int value){
        obj.add(new BasicDBObject(field, value));
        return this;
        }
    public DBQuery add(String field, long value){
        obj.add(new BasicDBObject(field, value));
        return this;
        }
    public DBQuery addCMP(int mode, String field, long value){
        obj.add(new BasicDBObject(new BasicDBObject(field, new BasicDBObject(cmpList[mode], value))));
        return this;
        }
    public DBQuery addCMP(int mode, String field, long value, long defValue){
        if (value!=defValue)
            obj.add(new BasicDBObject(new BasicDBObject(field, new BasicDBObject(cmpList[mode], value))));
        return this;
    }
    public DBQuery add(String field, boolean value){
        obj.add(new BasicDBObject(field, value));
        return this;
        }
    public DBQuery add(String field, String value){
        obj.add(new BasicDBObject(field, value));
        return this;
        }
    public DBQuery add(String field, int value, int defValue){
        if (value!=defValue)
            obj.add(new BasicDBObject(field, value));
        return this;
        }
    public DBQuery add(String field, long value, long defValue){
        if (value!=defValue)
            obj.add(new BasicDBObject(field, value));
        return this;
        }
    public DBQuery add(String field, boolean value, boolean defValue){
        if (value!=defValue)
            obj.add(new BasicDBObject(field, value));
        return this;
        }
    public DBQuery add(String field, String value, String defValue){
        if (value!=defValue)
            obj.add(new BasicDBObject(field, value));
        return this;
        }
    public BasicDBObject getQuery(){
        BasicDBObject query = new BasicDBObject();
        query.put(modeList[mode], obj);
        return query;
    }
}
