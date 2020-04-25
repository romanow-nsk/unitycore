package firefighter.core.API;

import firefighter.core.DBRequest;
import firefighter.core.ServerState;
import firefighter.core.constants.ConstList;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.EntityNamed;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.artifacts.ArtifactList;
import firefighter.core.entity.base.WorkSettingsBase;
import firefighter.core.entity.baseentityes.*;
import firefighter.core.entity.notifications.NTMessage;
import firefighter.core.entity.base.HelpFile;
import firefighter.core.entity.base.StringList;
import firefighter.core.entity.users.Account;
import firefighter.core.entity.users.User;
import firefighter.core.utils.Address;
import firefighter.core.utils.GPSPoint;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface RestAPIBase {
    /** Получить настройки - МК Техник и бизнес-процессы) */
    @GET("/api/worksettings")
    Call<WorkSettingsBase> workSettings(@Header("SessionToken") String token);
    @POST("/api/worksettings/update")
    Call<JEmpty> updateWorkSettings(@Header("SessionToken") String token,@Body WorkSettingsBase ws);
    //========================== Универсальный интерфейс бизнес-объектов БД ===================
    @POST("/api/entity/add")
    Call<JLong> addEntity(@Header("SessionToken") String token, @Body DBRequest body, @Query("level") int level);
    @POST("/api/entity/update")
    Call<JEmpty> updateEntity(@Header("SessionToken") String token,@Body DBRequest body);
    @POST("/api/entity/update/field")
    Call<JEmpty> updateEntityField(@Header("SessionToken") String token,@Query("name") String fieldName, @Body DBRequest body);
    @GET("/api/entity/get")
    Call<DBRequest> getEntity(@Header("SessionToken") String token, @Query("classname") String classname, @Query("id") long id,@Query("level") int level);
    @GET("/api/entity/list")
    Call<ArrayList<DBRequest>> getEntityList(@Header("SessionToken") String token, @Query("classname") String classname, @Query("mode") int mode,@Query("level") int level);
    @POST("/api/entity/remove")
    Call<JBoolean> removeEntity(@Header("SessionToken") String token,@Query("classname") String classname, @Query("id") long id);
    @GET("/api/entity/number")
    Call<JInt> getEntityNumber(@Header("SessionToken") String token,@Query("classname") String classname);
    //---------------------------- Администрирование ---------------------------------------------------------------------------
    @GET("/api/debug/ping")
    Call<JEmpty> ping();
    @GET("/api/debug/token")
    Call<JString> debugToken(@Query("pass") String pass);
    @GET("/api/admin/cleardb")
    Call<JString> clearDB(@Header("SessionToken") String token,@Query("pass") String pass);
    @GET("/api/admin/cleartable")
    Call<JString> clearTable(@Header("SessionToken") String token,@Query("table") String table, @Query("pass") String pass);
    @GET("/api/keepalive")
    Call<JInt> keepalive(@Header("SessionToken") String token);
    @GET("/api/debug/consolelog")
    Call<StringList> getConsoleLog(@Header("SessionToken") String token,@Query("count") int count);
    @POST("/api/bug/add")
    Call<JLong> sendBug(@Header("SessionToken") String token, @Body BugMessage bug);
    @GET("/api/bug/list")
    Call<EntityList<BugMessage>> getBugList(@Header("SessionToken") String token,@Query("mode") int mode);
    @GET("/api/bug/get")
    Call<BugMessage> getBug(@Header("SessionToken") String token,@Query("id") long id);
    @GET("/api/admin/exportdb")
    Call<Artifact> exportDBxls(@Header("SessionToken") String token);
    @POST("/api/admin/reboot")
    Call<JEmpty> rebootServer(@Header("SessionToken") String token, @Query("pass") String pass);
    @POST("/api/admin/importdb")
    Call<JString> importDBxls(@Header("SessionToken") String token, @Query("pass") String pass,@Query("id") long id);
    @POST("/api/admin/deploy")
    Call<JString> deploy(@Header("SessionToken") String token, @Query("pass") String pass);
    @POST("/api/admin/execute")
    Call<JString> execute(@Header("SessionToken") String token, @Query("pass") String pass,@Query("cmd") String cmd);
    @POST("/api/admin/shutdown")
    Call<JString> shutdown(@Header("SessionToken") String token, @Query("pass") String pass);
    @GET("/api/admin/preparedb")
    Call<JString> prepareDB(@Header("SessionToken") String token,@Query("operation") int operation,@Query("pass") String pass);
    @GET("/api/admin/longpoll")
    Call<JString> longPolling(@Header("SessionToken") String token,@Query("pass") String pass);
    @POST("/api/admin/lock")
    Call<JEmpty> lock(@Header("SessionToken") String token,@Query("pass") String pass, @Query("on") boolean on);
    @GET("/api/admin/testcall")
    Call <JString> testCall(@Header("SessionToken") String token,@Query("operation") int operation,@Query("value") String value);
    @POST("/api/admin/logfile/reopen")
    Call <JEmpty> reopenLogFile(@Header("SessionToken") String token,@Query("pass") String pass);
    @GET("/api/admin/files/list")
    Call <StringList> getFolder(@Header("SessionToken") String token,@Query("pass") String pass, @Query("folder") String folder);
    //------------------------------------------------------------------------------------------------------------------
    /** Список имен из таблицы по шаблону */
    @GET("/api/names/get")
    Call<EntityList<EntityNamed>> getNamesByPattern(@Header("SessionToken") String token,@Query("entity") String entity,@Query("pattern") String pattern);
    /** Получить текущую версию системы */
    @GET("/api/version")
    Call<JString> currentVersion(@Header("SessionToken") String token);
    /** Получить состояние сервера */
    @GET("/api/serverstate")
    Call<ServerState> serverState(@Header("SessionToken") String token);
    /** удаление по имени сущности и id */
    @POST("/api/entity/delete")
    Call<JBoolean> deleteById(@Header("SessionToken") String token,@Query("entity") String entity, @Query("id") long id);
    /** восстановление по имени сущности и id */
    @POST("/api/entity/undelete")
    Call<JBoolean> undeleteById(@Header("SessionToken") String token,@Query("entity") String entity, @Query("id") long id);
    @GET("/api/const/all")
    Call<ConstList> getConstAll(@Header("SessionToken") String token);
    @GET("/api/const/bygroups")
    Call<ArrayList<ConstList>> getConstByGroups(@Header("SessionToken") String token);
    @POST("/api/gps/address")
    Call<GPSPoint> getGPSByAddress(@Header("SessionToken") String token, @Body Address addr);
    //=========================APIUser=========================================================
    @GET("/api/user/logoff")
    Call<JEmpty> logoff(@Header("SessionToken") String token);
    /** Авторизация по Account в теле запроса (тест) = ТЕЛО МОЖЕТ СОДЕРЖАТЬ ТОЛЬКО POST */
    @POST("/api/user/login")
    Call<User> login(@Body Account body);
    /** Авторизация по номеру телефона и паролю */
    @GET("/api/user/login/phone")
    Call<User> login(@Query("phone") String phone, @Query("pass") String pass);
    /** -------------- Стандартные операции с User --------------------- */
    @GET("/api/user/list")
    Call<EntityList<User>> getUserList(@Header("SessionToken") String token,  @Query("mode") int mode,@Query("level") int level);
    @POST("/api/user/add")
    Call<JLong> addUser(@Header("SessionToken") String token,@Body User user);
    @POST("/api/user/update")       // Не менять связанный ключ Техника/Заказчика
    Call<JEmpty> updateUser(@Header("SessionToken") String token,@Body User user);
    @GET("/api/user/get")
    Call<User> getUserById(@Header("SessionToken") String token,@Query("id") long id,@Query("level") int level);
    //=============================APIArtifact (артефакты и файлы) ================================
    /** Объект-artifact по id  */
    @GET("/api/artifact/get")
    Call<Artifact> getArtifactById(@Header("SessionToken") String token,@Query("id") long id,@Query("level") int level);
    /** Список артефактов  */
    @GET("/api/artifact/list")
    Call<ArtifactList> getArtifactList(@Header("SessionToken") String token, @Query("mode") int mode,@Query("level") int level);
    //----------------------------- файлы --------------------------
    /** Записать файл как артефакт  */
    @Streaming
    @Multipart
    @POST("/api/file/upload")
    Call<Artifact> upload(@Header("SessionToken") String token,@Query("description") String description,@Query("origname") String origName, @Part MultipartBody.Part file);
    //Call<Artifact> upload(@Part("description") RequestBody description, @Part MultipartBody.Part file);
    /** Записать файл по имени в корень  root=true или в каталог БД=порт */
    @Streaming
    @Multipart
    @POST("/api/file/uploadByName")
    Call<JEmpty> uploadByName(@Header("SessionToken") String token,@Query("fname") String description, @Part MultipartBody.Part file, @Query("root") boolean root);
    /** Читать файл по id артефакта  */
    @Streaming
    @GET("/api/file/load")
    Call<ResponseBody> downLoad(@Header("SessionToken") String token,@Query("id") long id);
    /** Читать файл по имени из корня  */
    @Streaming
    @GET ("/api/file/loadByName")
    Call<ResponseBody> downLoadByName(@Header("SessionToken") String token,@Query("fname") String fname, @Query("root") boolean root);
    /** Установить имя файла-ориганиала в артефакте -  Не нужно, имя в отдельном part */
    @POST("/api/artifact/setname")
    Call<Artifact> setArtifactName(@Header("SessionToken") String token,@Query("id") long id,@Query("name") String name);
    /* 642 - создать объект-артефакт */
    @POST("/api/artifact/create")
    Call<Artifact> createArtifact(@Header("SessionToken") String token,@Query("description") String description,@Query("origname") String origName, @Query("filesize") long filesize);
    /* 645 Конвертация артефакта */
    @POST("/api/artifact/convert")
    Call<JEmpty> convertArtifact(@Header("SessionToken") String token,@Query("id") long id);
    //----------------- Для Example ----------------------------------------------------------------------
    @Streaming
    @GET ("/api/file/load2")
    Call<ResponseBody> downLoad2(@Header("SessionToken") String token,@Body JString body);
    //-------------------------- Уведомления -----------------------------------------------------------------------------
    @POST("/api/notification/setstate")
    Call<JEmpty> setNotificationState(@Header("SessionToken") String token,@Query("id") long id,@Query("state") int state);
    /**  Селекция по типу пользователя (=0-нет), по id пользователя, по состоянию уведомления*/
    @GET("/api/notification/user/list")
    Call<EntityList<NTMessage>> getNotificationUserList(@Header("SessionToken") String token, @Query("userid") long id, @Query("usertype") int type, @Query("state") int state);
    /*** Стандартные ------------ */
    @GET("/api/notification/count")
    Call<JInt> getNotificationCount(@Header("SessionToken") String token);
    @POST("/api/notification/add")
    Call<JLong> addNotification(@Header("SessionToken") String token,@Body NTMessage body);
    @POST("/api/notification/add/broadcast")
    Call<JInt> addNotificationBroadcast(@Header("SessionToken") String token,@Body NTMessage body);
    @POST("/api/notification/update")
    Call<JEmpty> updateNotification(@Header("SessionToken") String token,@Body NTMessage body);
    @GET("/api/notification/get")
    Call<NTMessage> getNotification(@Header("SessionToken") String token, @Query("id") long id);
    @GET("/api/notification/list")
    Call<EntityList<NTMessage>> getNotificationList(@Header("SessionToken") String token);
    @POST("/api/notification/remove")
    Call<JBoolean> removeNotification(@Header("SessionToken") String token, @Query("id") long id);
    /** Поучить список артефактов помощи - без авторизации */
    @GET("/api/helpfile/list")
    Call<EntityList<HelpFile>> getHelpFileList(@Query("question") String question );
    @GET("/api/artifact/condition/list")
    Call<ArtifactList> getArtifactConditionList(@Header("SessionToken") String token,
        @Query("type") int type, @Query("owner") String owner, @Query("namemask") String nameMask, @Query("filenamemask") String fileNameMask,
        @Query("size1") long size1, @Query("size2") long size2,
        @Query("dateInMS1") long dateMS1,@Query("dateInMS2") long dateMS2);
    @POST("/api/entity/artifactlist/add")
    Call<JEmpty> addArtifactToList(@Header("SessionToken") String token,@Query("classname") String className,@Query("fieldname") String fieldName,@Query("id") long id,@Query("artifactid") long artifactid);
    /** Удалить артефакт из EntityLinkList класса  */
    @POST("/api/entity/artifactlist/remove")
    Call<JEmpty> removeArtifactFromList(@Header("SessionToken") String token, @Query("classname") String className,@Query("fieldname") String fieldName,@Query("id") long id,@Query("artifactid") long artifactid);
    /** Заместить артефакт в EntityLink класса  */
    @POST("/api/entity/artifact/replace")
    Call<JEmpty> replaceArtifact(@Header("SessionToken") String token,@Query("classname") String className,@Query("fieldname") String fieldName,@Query("id") long id,@Query("artifactid") long artifactid);
    /** Удалить артефакт вместе с файлом */
    @POST("/api/artifact/remove")
    Call<JEmpty> removeArtifact(@Header("SessionToken") String token,@Query("id") long id);
    /** Режим кэширования сервера */
    @POST("/api/admin/cashmode")
    Call<JEmpty> setCashMode(@Header("SessionToken") String token,@Query("mode") boolean mode,@Query("pass") String pass);
    /** Изменены параметры - добавляется вручную (без Яндекса) */
    @POST("/api/address/setgps")
    Call<JLong> setAddressGPS(@Header("SessionToken") String token,  @Query("id") long id, @Body GPSPoint gps);
}
