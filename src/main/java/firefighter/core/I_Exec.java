package firefighter.core;

import firefighter.core.entity.Entity;

public interface I_Exec<T extends Entity> {
    public void exec(T vv);
}
