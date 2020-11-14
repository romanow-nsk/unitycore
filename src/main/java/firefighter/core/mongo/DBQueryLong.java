package firefighter.core.mongo;

import com.mongodb.BasicDBObject;

public class DBQueryLong implements I_DBQuery{
    public final int cmpMode;
    public final String field;
    public final long value;
    public DBQueryLong(int cmpMode, String field, long value) {
        this.cmpMode = cmpMode;
        this.field = field;
        this.value = value; }
    public DBQueryLong(String field, long value) {
        this.cmpMode = ModeEQ;
        this.field = field;
        this.value = value; }
    @Override
    public BasicDBObject getQuery() {
        if (cmpMode==ModeEQ)
            return new BasicDBObject(field,value);
        else
            return new BasicDBObject(field, new BasicDBObject(cmpList[cmpMode], value));
        }
    @Override
    public String getWhere() {
        return field+I_DBQuery.sqlCmpList[cmpMode]+value;
        }
}
