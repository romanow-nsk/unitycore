package firefighter.core.reports;

public class TableCol {
    public final static int AlignLeft=0;
    public final static int AlignCenter=1;
    public final static int AlignRight=2;
    String name="";
    int size=0;
    int midSize=0;              // Среднее кол-во символов в строке
    int maxSize=0;              // Макс. кол-во символов в строке
    int finSize=0;              // Расчетное кол-во символов
    int hexBackColor=0;
    int align=AlignLeft;
    int linkIndex=0;            // Индекс сноски
    String linkText="";         // Пояснение к столбцу
    boolean multiString=false;  // строка с переносом
    public TableCol(String name, int size) {
        this.name = name;
        this.size = size;
        }
    public TableCol setAlign(int align0){
        align = align0;
        return this;
        }
    public TableCol center(){
        align = TableCol.AlignCenter;
        return this;
    }
    public TableCol setLinkIndex(int index0){
        linkIndex = index0;
        return this;
        }
    public TableCol setLinkText(String txt){
        linkText = txt;
        return this;
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
