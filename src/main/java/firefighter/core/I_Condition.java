package firefighter.core;

import firefighter.core.entity.Entity;

public interface I_Condition<T extends Entity> {
    public boolean test(T vv);
}
