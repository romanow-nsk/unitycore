package firefighter.core.entity.baseentityes;

import firefighter.core.mongo.DAO;

public class JInt extends DAO {
    private int value=0;
    public JInt(int val){
        value = val;
        }
    public int getValue(){ return value; }
    public void setValue(int value) {
        this.value = value;
        }
    public String toString(){ return ""+value; }
    public JInt(){}
}
