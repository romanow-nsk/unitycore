package firefighter.core.mongo;

import com.mongodb.BasicDBObject;

public class DBQueryBoolean implements I_DBQuery{
    public final int cmpMode;
    public final String field;
    public final boolean value;
    public DBQueryBoolean(String field, boolean value) {
        this.cmpMode = ModeEQ;
        this.field = field;
        this.value = value; }

    @Override
    public BasicDBObject getQuery() {
        return new BasicDBObject(field,value);
        }
    @Override
    public String getWhere() {
        return field+I_DBQuery.sqlCmpList[cmpMode]+value;
        }
}
