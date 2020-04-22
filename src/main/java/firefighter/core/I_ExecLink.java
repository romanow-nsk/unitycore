package firefighter.core;

import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityLink;

public interface I_ExecLink<T extends EntityLink<? extends Entity>>  {
    public void exec(T vv);
}
