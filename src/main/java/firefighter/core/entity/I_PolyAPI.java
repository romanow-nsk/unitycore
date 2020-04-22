package firefighter.core.entity;

import firefighter.core.API.RestAPIFace;

public interface I_PolyAPI<T extends Entity> {
    public Option<T> apiUser(final RestAPIFace service, long id);
}
