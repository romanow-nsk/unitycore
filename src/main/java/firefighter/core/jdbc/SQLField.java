package firefighter.core.jdbc;

public class SQLField {
    public final String name;
    public final String type;
    public final int DAOType;
    public SQLField(String name, int DAOType, String type) {
        this.name = name;
        this.type = type;
        this.DAOType = DAOType;
        }
    public SQLField(String prefix, SQLField src){
        name = prefix+"_"+src.name;
        type = src.type;
        DAOType = src.DAOType;
    }
}
