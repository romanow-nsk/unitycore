package firefighter.core.entity;

import firefighter.core.API.RestAPIBase;

public interface I_PolyAPI<T extends Entity> {
    public Option<T> apiUser(final RestAPIBase service, long id);
}
