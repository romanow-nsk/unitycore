package firefighter.core.API;

import firefighter.core.DBRequest;
import firefighter.core.entity.baseentityes.JBoolean;
import firefighter.core.entity.baseentityes.JEmpty;
import firefighter.core.entity.baseentityes.JLong;
import firefighter.core.entity.baseentityes.JString;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.ArrayList;

public interface InnerAPIFace {
    //========================== Универсальный интерфейс бизнес-объектов БД ===================
    @POST("/api/entity/add")
    Call<JLong> addEntity(@Body DBRequest body, @Query("level") int level, @Query("ownOid") boolean ownOid);
    @POST("/api/entity/update")
    Call<JEmpty> updateEntity(@Body DBRequest body, @Query("level") int level);
    @GET("/api/entity/get")
    Call<DBRequest> getEntity(@Query("classname") String classname, @Query("id") long id,@Query("mode") boolean mode,@Query("level") int level);
    @GET("/api/entity/list")
    Call<ArrayList<DBRequest>> getEntityList(@Query("classname") String classname, @Query("mode") int mode, @Query("level") int level);
    @POST("/api/entity/remove")
    Call<JBoolean> removeEntity(@Query("classname") String classname, @Query("id") long id,@Query("mode") boolean mode);
    @GET("/api/entity/query")
    Call<ArrayList<DBRequest>> getByQuery(@Query("classname") String classname, @Query("query") String query, @Query("level") int level);
    @GET("/api/admin/droptable")
    Call<JEmpty> dropTable(@Query("classname") String classname);
    @GET("/api/debug/ping")
    Call<JEmpty> ping();
    @GET("/api/admin/cleardb")
    Call<JString> clearDB();
    @GET("/api/admin/cleartable")
    Call<JString> clearTable(@Query("table") String table);
    }
