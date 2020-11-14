package firefighter.core.mongo;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;

public class DBQueryList implements I_DBQuery{
    private List<I_DBQuery> obj = new ArrayList<I_DBQuery>();
    private int mode = ModeAnd;
    public DBQueryList(){}
    public DBQueryList(int mode0){
        mode = mode0;
        }
    public DBQueryList add(I_DBQuery cc){
        obj.add(cc);
        return this;
        }
    public DBQueryList add(String field, String value){
        add(new DBQueryString(field,value));
        return this;
        }
    public DBQueryList add(String field, int value){
        add(new DBQueryInt(field,value));
        return this;
        }
    public DBQueryList add(String field, long value){
        add(new DBQueryLong(field,value));
        return this;
        }
    public DBQueryList add(String field, boolean value){
        add(new DBQueryBoolean(field,value));
        return this;
        }
    public DBQueryList add(int mode, String field, String value){
        add(new DBQueryString(mode, field,value));
        return this;
        }
    public DBQueryList add(int mode,String field, int value){
        add(new DBQueryInt(mode,field,value));
        return this;
        }
    public DBQueryList add(int mode, String field, long value){
        add(new DBQueryLong(mode,field,value));
        return this;
        }
    public DBQueryList add(int mode, String field, long value, long defValue){
        if (value!=defValue)
            add(new DBQueryLong(mode,field,value));
        return this;
        }
    @Override
    public BasicDBObject getQuery() {
        BasicDBObject query = new BasicDBObject();
        ArrayList<BasicDBObject> list = new ArrayList<>();
        for(I_DBQuery vv : obj)
            list.add(vv.getQuery());
        query.put(modeList[mode], list);
        return query;
        }
}
