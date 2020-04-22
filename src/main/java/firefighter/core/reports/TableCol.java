package firefighter.core.reports;

public class TableCol {
    String name="";
    int size;
    public TableCol(String name, int size) {
        this.name = name;
        this.size = size;
        }
    public String getName() {
        return name;
        }
    public int getSize() {
        return size;
        }
}
