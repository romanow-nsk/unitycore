package firefighter.core.entity;

import firefighter.core.UniException;

public interface I_TypeName {
    public String typeName();
    public int typeId();
    public Class typeClass() throws UniException;
}
