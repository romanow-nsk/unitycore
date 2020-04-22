package firefighter.core.mongo;

import firefighter.core.UniException;

public interface I_MongoRW {
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException;
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo) throws UniException;
}
