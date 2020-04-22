package firefighter.core.reports;

public class DocumentParamList {
    private boolean landscape=false;
    private boolean full=false;
    public boolean isLandscape() { return landscape; }
    public DocumentParamList setLandscape() {
        landscape = true;
        return this;
        }
    public boolean isFull() { return full; }
    public DocumentParamList setFull() {
        full = true;
        return this;
        }
}
