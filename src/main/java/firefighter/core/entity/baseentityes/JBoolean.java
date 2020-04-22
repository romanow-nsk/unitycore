package firefighter.core.entity.baseentityes;

import firefighter.core.mongo.DAO;

public class JBoolean extends DAO {
    public JBoolean(){}
    private boolean value=false;
    public JBoolean(boolean val){
        value = val;
        }
    public boolean value(){ return value; }
}
