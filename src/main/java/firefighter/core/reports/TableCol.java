package firefighter.core.reports;

public class TableCol {
    String name="";
    int size;
    int hexBackColor=0;
    public TableCol(String name, int size) {
        this.name = name;
        this.size = size;
        }
    public TableCol(String name, int size, int backColor) {
        this.name = name;
        this.size = size;
        hexBackColor = backColor;
    }
    public String getName() {
        return name;
        }
    public int getSize() {
        return size;
        }
}
