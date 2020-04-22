package firefighter.core.entity.baseentityes;

import firefighter.core.mongo.DAO;

public class JString extends DAO {
    private String value="";
    public JString(String val){
        value = val;
        }
    public JString(){}
    public String getValue(){ return value; }
    public void setValue(String value) {
        this.value = value;
        }
    public String toString(){ return value; }
}
