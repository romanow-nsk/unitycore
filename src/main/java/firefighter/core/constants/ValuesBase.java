package firefighter.core.constants;

import firefighter.core.ServerState;
import firefighter.core.entity.EntityIndexedFactory;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.artifacts.ReportFile;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.entity.base.HelpFile;
import firefighter.core.entity.base.WorkSettingsBase;
import firefighter.core.entity.contacts.Contact;
import firefighter.core.entity.contacts.Mail;
import firefighter.core.entity.contacts.Phone;
import firefighter.core.entity.contacts.PhoneList;
import firefighter.core.entity.notifications.NTMessage;
import firefighter.core.entity.users.Account;
import firefighter.core.entity.users.Person;
import firefighter.core.entity.users.User;
import firefighter.core.help.HelpFactory;
import firefighter.core.utils.*;

import java.util.HashMap;

public class ValuesBase {
    public final static int ReleaseNumberBase=1;                // номер сборки сервера
    private final static String ApkName = "Unity.apk";           // Имя файла
    private final static String mongoDBName = "unity";
    private final static String mongoDBUser = "unity";
    private final static String ServerName="UnityDataserver.jar";
    private final static String mongoDBPassword = "unity";
    private final static User superUser=new User(ValuesBase.UserSuperAdminType, "Система", "", "", "UnityDataserver", "pi31415926","9130000000");
    public static String getMongoDBName() {
        return mongoDBName; }
    public static String getMongoDBUser() {
        return mongoDBUser; }
    public static String getMongoDBPassword() {
        return mongoDBPassword; }
    public static String getApkName() {
        return ApkName; }
    public static String getServerName() {
        return ServerName; }
    public static User getSuperUser() {
        return superUser; }
    //------------------------------------------------------------------------------------------------------
    public final static String week[] = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
    public final static String mnt[] = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    public final static String dataServerIP = "localhost";
    public final static int dataServerPort = 4567;
    public final static String ClientVersion = "1.0.01";        // Версия ПО
    public final static String ServerVersion = "1.0.01";        // Версия ПО
    public final static String dataServerFileDir = "d:/temp";
    public final static String webServerWebLocation = "d:/temp/webserver";
    public final static String mongoStartCmd = "\"c:/Program Files/MongoDB/Server/3.0/bin/mongod\" --dbpath " + dataServerFileDir + "/mongo";
    public final static int FileBufferSize = 8192;              // РАзмер буфера передачи файла
    public final static int FTPFileNotFound = -1;
    public final static int mongoServerPort = 27017;
    public final static String mongoServerIP = "127.0.0.1";
    public final static String DeployScriptName="deploy";

    public final static String GeoCoderCity = "Новосибирск";
    public final static int ConsoleLogSize = 1000;              // Количество строк лога
    public final static int CKeepALiveTime=10;                  // Интервал времени проверки соединения
    public final static String DebugTokenPass="pi31415926";     // Пароль отладочного токена
    public final static int PopupListMaxSize=25;                // Максимальный размер выпадающего списка
    public final static int ServerRebootDelay=10;               // Задержка сервера при перезагрузке
    public final static int HTTPTimeOut=60;                     // Тайм-аут клиента
    public final static int BackgroundOperationMaxDelay=300;    //
    public final static int MongoDBType=0;
    public final static int MongoDBType36=1;
    public final static int FatalExceptionStackSize=20;         // Стек вызовов при исключении

    public static void init(){}

    public final static EntityIndexedFactory EntityFactory = new EntityIndexedFactory();
    static {
        EntityFactory.put(new TableItem("Настройки_0", WorkSettingsBase.class));
        EntityFactory.put(new TableItem("Метка GPS", GPSPoint.class));
        EntityFactory.put(new TableItem("Адрес", Address.class));
        EntityFactory.put(new TableItem("Артефакт", Artifact.class));
        EntityFactory.put(new TableItem("Пользователь", User.class));
        EntityFactory.put(new TableItem("Уведомление", NTMessage.class)
                .add("type").add("state").add("param").add("r_timeInMS").add("s_timeInMS")
                .add("recType").add("sndType"));   // 651 - в БД
        EntityFactory.put(new TableItem("Улица", Street.class));
        EntityFactory.put(new TableItem("Нас.пункт", City.class));
        EntityFactory.put(new TableItem("Ошибка", BugMessage.class));
        EntityFactory.put(new TableItem("Персоналия", Person.class));
        EntityFactory.put(new TableItem("Отчет", ReportFile.class));
        EntityFactory.put(new TableItem("Состояние", ServerState.class));
        EntityFactory.put(new TableItem("Подсказка", HelpFile.class));                     // Сборка 586
        EntityFactory.put(new TableItem("Контакт", Contact.class,false));          // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Спец.файла", FileNameExt.class,false));   // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Дата/время", OwnDateTime.class,false));   // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("E-mail", Mail.class,false));              // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Телефон", Phone.class,false));            // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Телефоны", PhoneList.class,false));       // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Сумма", MoneySum.class,false));           // Сборка 636
        EntityFactory.put(new TableItem( "Аккаунт", Account.class));                         // Сборка 637
        }
    //------------- Префикся для встроенных объектов ------------------------------------------
    public final static HashMap<String,String> PrefixMap = new HashMap<>();
    static  {
        PrefixMap.put("BugMessage.date","d");               //636
        PrefixMap.put("Artifact.date","d");                 //636
        PrefixMap.put("Artifact.original","f");             //636
        PrefixMap.put("Person.phone","p");                  //636
        PrefixMap.put("Person.mail","m");                   //636
        PrefixMap.put("User.phone","p");                    //636
        PrefixMap.put("User.mail","m");                     //636
        PrefixMap.put("GPSPoint.gpsTime","a");              //636
        PrefixMap.put("Street.location","s");               //636
        PrefixMap.put("Account.loginPhone","p");            //636
        PrefixMap.put("AccountData.loginPhone","p");        //637
        PrefixMap.put("NTMessage.sndTime","s");             //651
        PrefixMap.put("NTMessage.recTime","r");             //651
        PrefixMap.put("Street.location","s");               //636
        }
    //------------- Аутентификация и сессия ---------------------------------------------------
    public final static int SessionTokenLength = 32;                // Размер сессионного ключа
    public final static int SessionSilenceTime = 30 * 60;           // Время молчания до разрыва сессии (сек)
    public final static int SessionCycleTime = 30;                  // Цикл проверки сессией (сек)
    public final static int ClockCycleTime = 30;                    // Цикл проверки событий (процессы) (сек)
    public final static String SessionHeaderName = "SessionToken";  // Параметр заголовка - id сессии
    public final static String JWTSessionSecretKey = "FireFighterTopSecret";
    public final static boolean JWTSessionMode = true;
    //------------  Режим чтения таблиц ----------------------------------------------------
    public final static int GetAllModeActual = 0;                   // Только актуальные
    public final static int GetAllModeTotal = 1;                    // Все
    public final static int GetAllModeDeleted = 2;                  // Удаленные
    public final static int DefaultNoAutoAPILevel = 3;              // Уровень загрузки по умолчанию для функций API (не автоматические)
    //------------------------ Типы улиц и нас пунктов -------------------------------------------------
    @CONST(group = "TownType", title = "Город")
    public final static int CTown = 1;
    @CONST(group = "TownType", title = "Поселок")
    public final static int CCountry = 2;
    public final static String TypesCity[] = {"", "г.", "пос."};
    @CONST(group = "StreetType", title = "Улица")
    public final static int SStreet = 0x10;
    @CONST(group = "StreetType", title = "Проспект")
    public final static int SProspect = 0x20;
    @CONST(group = "StreetType", title = "Шоссе")
    public final static int SWay = 0x30;
    @CONST(group = "StreetType", title = "Площадь")
    public final static int SPlace = 0x40;
    public final static String TypesStreet[] = {"", "ул.", "пр.", "ш.","пл."};
    @CONST(group = "HomeType", title = "Дом")
    public final static int HHome = 0x100;
    @CONST(group = "HomeType", title = "Корпус")
    public final static int HCorpus = 0x200;
    public final static String TypesHome[] = {"", "д.", "корп."};
    @CONST(group = "OfficeType", title = "Офис")
    public final static int OOffice = 0x1000;
    @CONST(group = "OfficeType", title = "Кабинет")
    public final static int OCabinet = 0x2000;
    @CONST(group = "OfficeType", title = "Квартира")
    public final static int OQuart = 0x3000;
    public final static String TypesOffice[] = {"", "оф.", "к.", "кв."};
    //----------------------- Операции над EntityLink при Update (Put)---------------------------
    @CONST(group = "Operation", title = "Нет операции")
    public final static int OperationNone = 0;
    @CONST(group = "Operation", title = "Добавить")
    public final static int OperationAdd = 1;     // Добавить ref и записать oid
    @CONST(group = "Operation", title = "Изменить")
    public final static int OperationUpdate = 2;  // Обновить по ref
    public final static String Operations[] = {"", "Append", "Update"};
    public final static boolean DeleteMode = false;
    public final static boolean UndeleteMode = true;
    //------------- Типы пользователей -----------------------------------------------------
    @CONST(group = "User", title = "Гость")
    public final static int UserGuestType = 0;
    @CONST(group = "User", title = "Суперадмин")
    public final static int UserSuperAdminType = 1;
    @CONST(group = "User", title = "Администратор")
    public final static int UserAdminType = 2;
    public final static String UserTypeList[] = {"Гость", "Суперадмин", "Администратор"};
    //------------------- Вид уведомления  -------------------------------------------------------------------------
    @CONST(group = "NotificationType", title = "Не определено")
    public final static int NTUndefined = 0;
    @CONST(group = "NotificationType", title = "Действие за клиента")
    public final static int NTUserAction = 1;
    public final static String NTypes[] = {"Не определено","Действие за клиента"};
    //------------------- Состояние уведомлнния  -------------------------------------------------------------------------
    @CONST(group = "NotificationState", title = "Не определено")
    public final static int NSUndefined = 0;
    @CONST(group = "NotificationState", title = "Передано")
    public final static int NSSend = 1;
    @CONST(group = "NotificationState", title = "Просмотрено")
    public final static int NSReceived = 2;
    @CONST(group = "NotificationState", title = "В работе")
    public final static int NSInProcess = 3;
    @CONST(group = "NotificationState", title = "Закрыто")
    public final static int NSClosed = 4;
    public final static String NState[] = {"Не определено", "Передано", "Просмотрено", "В работе", "Закрыто"};
    //--------------------------------------------------------------------------------------------------
    public final static int GeoNone = 0;                  // Координаты недоступны
    public final static int GeoNet = 1;                   // Координаты от сети (вышек)
    public final static int GeoGPS = 2;                   // Координаты от GPS
    //--------------------------------------------------------------------------------------------------
    public final static int HTTPOk = 200;
    public final static int HTTPAuthorization = 401;
    public final static int HTTPNotFound = 404;
    public final static int HTTPRequestError = 400;
    public final static int HTTPException = 500;
    public final static int HTTPServiceUnavailable = 503;  // Сервис недоступен
    //----------------------------------------------------------------------
    public final static int UndefinedType = 0;            // Неопределенный тип
    //----------------- Типы артефактов-------------------------------------
    @CONST(group = "ArtifactType", title = "Не определен")
    public final static int ArtifactNoType = 0;
    @CONST(group = "ArtifactType", title = "Фото")
    public final static int ArtifactImageType = 1;
    @CONST(group = "ArtifactType", title = "Видео")
    public final static int ArtifactVideoType = 2;
    @CONST(group = "ArtifactType", title = "Аудио")
    public final static int ArtifactAudioType = 3;
    @CONST(group = "ArtifactType", title = "Текст")
    public final static int ArtifactTextType = 4;
    @CONST(group = "ArtifactType", title = "Документ")
    public final static int ArtifactDocType = 5;
    @CONST(group = "ArtifactType", title = "Прочее")
    public final static int ArtifactOtherType = 6;
    public final static String ArtifactTypeNames[] = {"-----", "Фото", "Видео", "Аудио", "Текст", "Документ", "Прочее"};
    public final static String ArtifactDirNames[] = {"-----", "Image", "Video", "Audio", "Text", "Document", "Other"};
    public final static HashMap<String,String> ConvertList = new HashMap<String,String>();
        static {
            ConvertList.put("3gp","mpg");
            }
    //-------------------- Тип сохраняемого отчета --------------------------------------------------------------------
    @CONST(group = "ReportType", title = "Нет")
    public final static int ReportNO = 0;
    @CONST(group = "ReportType", title = "pdf")
    public final static int ReportPDF = 1;
    @CONST(group = "ReportType", title = "xms")
    public final static int ReportXML = 2;
    @CONST(group = "ReportType", title = "html")
    public final static int ReportHTML = 3;
    public final static String ReportTypes[] = {"Нет", "pdf", "xls","html"};
    //----------------------- Типы выполнения уведомлений ---------------------------------------------
    @CONST(group = "NotificationMode", title = "Принудительно")
    public final static int NMHard = 0;
    @CONST(group = "NotificationMode", title = "После завершения операции")
    public final static int NMDeffered = 1;
    @CONST(group = "NotificationMode", title = "С подтверждением")
    public final static int NMUserAck = 2;
    public final static String NModes[] = {"Принудительно", "После завершения операции", "С подтверждением"};
    //-------------------- Словарь подсказок ------------------------------------------------------------------------
    public final static HelpFactory Glossary = new HelpFactory();
    static    {
        Glossary.put("Уведомление");
        Glossary.put("Аудио");
        Glossary.put("Видео");
        Glossary.put("Фото");
        Glossary.put("GPS");
        Glossary.put("Общее");
        Glossary.put("Сеть");
        Glossary.put("Настройки");
        }
    //----------------------- Отчеты  ---------------------------------------------
    @CONST(group = "Report", title = "Прочее")
    public final static int RepOther = 0;
    public final static String Reports[] = {"Прочее"};
    //------------------------ Препарирования БД --------------------------------------
    @CONST(group = "DBOperation", title = "Очистка контента (заявки)")
    public final static int DBOClearContent1 = 0;
    @CONST(group = "DBOperation", title = "Обратные ссылки")
    public final static int DBOBackLinks = 1;
    @CONST(group = "DBOperation", title = "Сжать таблицы")
    public final static int DBOSquezzyTables = 2;
    @CONST(group = "DBOperation", title = "Обновить поля")
    public final static int DBORefreshFields = 3;
    @CONST(group = "DBOperation", title = "Сбор мусора")
    public final static int DBOCollectGarbage = 4;
    @CONST(group = "DBOperation", title = "Наличие артефактов")
    public final static int DBOTestArtifacts = 5;
    @CONST(group = "DBOperation", title = "Пересчет задолженностей")
    public final static int DBORecalcDepts = 6;
    @CONST(group = "DBOperation", title = "Тест - задержка 60 сек")
    public final static int DBOTestDelay = 7;
    @CONST(group = "DBOperation", title = "637:Аккаунт")
    public final static int DBOCopyAccount = 8;
    public final static String DBOperationList[] = {"Очистка контента (заявки)","Обратные ссылки",
            "Сжать таблицы","Обновить поля","Сбор мусора","Наличие артефактов",
            "Тест - задержка 60 сек","Пересчет задолженностей","637:Аккаунт"};
    //------------------------ Источники артефактов ------------------------------------------------------
    public final static String ArtifactParentList[] = {"ReportFile", "User"};
}
