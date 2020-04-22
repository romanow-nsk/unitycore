package firefighter.core.entity.subjectarea;


import firefighter.core.constants.Values;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityLink;
import firefighter.core.utils.GPSPoint;
import firefighter.core.utils.OwnDateTime;

public class WorkSettings extends Entity {
    private int gpsOnWayPeriod= Values.WSgpsOnWayPeriod;                // Период GPS на пути к объекту (мин)
    private int gpsOnFacilityPeriod=Values.WSgpsOnFacilityPeriod;       // Период GPS на  объекте (мин)
    private int minDistanceToFacility=Values.WSminDistanceToFacility;   // Мин. расстояние до объекта для активации
    private int workDayBegin=Values.WSworkDayBegin;                     // Начало рабочего дня
    private int workDayEnd=Values.WSworkDayEnd;                         // Конец рабочего дня
    private int gpsValidDelay=Values.WSgpsValidDelay;                   // Интервал УСТАРЕВАНИЯ последних координат (мин)
    private OwnDateTime lastReglamentMonth=new OwnDateTime(0);      // Последний месяц, для которого сгенерирован регламент
    private boolean needToConfirm=false;                                // Имеется неутвержденный сгенерированный регламент
    private int hourToProc = 23;                                        // Время автоматического обслуживания (час)
    private int minuteToProc = 00;                                      // Время автоматического обслуживания (мин)
    private long orderSingleOid=0;                  // 28.06 id регламента разовой заявки
    private long orderCloseOid=0;                    // 28.06 id регламента завершения работ
    private int movingSpeed=30;                     // 28.06 Средняя скорость перемещения между объектами (км/ч)
    private int movingInterval0=20;                 // 28.06 Начальный интервал перемещения ( мин )
    private double movindKoeff=1.3;                 // 28.06 Коэффициент замедления времени перемещения
    private int defaultDuration=30;                 // Продолжительность обслуживания по умолчанию
    private EntityLink<GPSPoint> officeGPS = new EntityLink<>(GPSPoint.class);    // 28.06 Координаты центрального офиса
    private int photoSizeInPics=1200;               // Размер фотографий
    private String MKVersion="1.0.01";
    private String asteriskHost="mx.mail-ok.ru";
    private String asteriskMail="apiorder@mail-ok.ru";
    private String asteriskPass="uoYe8Oov11";
    private String asteriskSecur="starttls";
    private int asteriskPort=143;
    private String dataServerFileDir="d:/temp";
    private boolean dataServerFileDirDefault=true;  // Используется текущий каталог запуска
    private boolean technicianMode=false;           // Режим дежурного техника
    private long technicianId=0;                    // id дежурного техника = ПО ТАБЛИЦЕ USER-ов
    private OwnDateTime technicianModeStartTime=new OwnDateTime(false);  // Время окончания режима
    private OwnDateTime technicianModeStopTime=new OwnDateTime(false);   // Время окончания режима
    private int technicianAnswerTime = Values.TechnicianAnswerTime;         // Время ответа техника
    private int asteriskDialPeriod = Values.AsteriskDialPeriod;             // Периодичность звонка технику (мин)
    private String asteriskDialTarget="rsync://asterisk.mail-ok.ru:873/calls/";
    private String asteriskDialCallFile="/var/lib/asterisk/sounds/ru/firefighter";
    private boolean asteriskChefCall=false;         // Звонок начальнику ТО однократный
    private boolean convertAtrifact=false;
    //----------------------------------------------------------------------------------------------------------
    /*
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        putDBValues(prefix,document,level,mongo);
        //officeGPS.putData("a_",document,0,null);
        lastReglamentMonth.putData("m_",document,0,null);
        technicianModeStartTime.putData("b_",document,0,null);
        technicianModeStopTime.putData("e_",document,0,null);
    }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo) throws UniException {
        getDBValues(prefix,res,level,mongo);
        //officeGPS.getData("a_",res, 0, null);
        lastReglamentMonth.getData("m_",res, 0, null);
        technicianModeStartTime.getData("b_",res, 0, null);
        technicianModeStopTime.getData("e_",res, 0, null);
        }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    @Override
    public void getData(Row row, ExCellCounter cnt) throws UniException{
        getXMLValues(row, cnt);
        //officeGPS.getData(row, cnt);
        lastReglamentMonth.getData(row, cnt);
        technicianModeStartTime.getData(row, cnt);
        technicianModeStopTime.getData(row, cnt);
    }
    @Override
    public void putData(Row row, ExCellCounter cnt) throws UniException{
        putXMLValues(row, cnt);
        //officeGPS.putData(row, cnt);
        lastReglamentMonth.putData(row, cnt);
        technicianModeStartTime.putData(row, cnt);
        technicianModeStopTime.putData(row, cnt);
    }
    @Override
    public void putHeader(String prefix, ArrayList<String> list) throws UniException{
        putXMLHeader(prefix,list);
        //officeGPS.putHeader("a_",list);
        lastReglamentMonth.putHeader("m_",list);
        technicianModeStartTime.putHeader("b_",list);
        technicianModeStartTime.putHeader("e_",list);
    }
    */
    //------------------------------------------------------------------------------------------------------------------
    public int createMovingTime(GPSPoint p1, GPSPoint p2){
        if (p1==null || p2==null) return movingInterval0;
        double diff = p1.diff(p2);      // В метрах
        if (diff==-1) return movingInterval0;
        int delay = (int)(movingInterval0 + movindKoeff*diff/(movingSpeed*1000/60));
        return delay;
        }
    //------------------------------------------------------------------------------------------------------------------
    public boolean isDataServerFileDirDefault() {
        return dataServerFileDirDefault; }
    public void setDataServerFileDirDefault(boolean dataServerFileDirDefault) {
        this.dataServerFileDirDefault = dataServerFileDirDefault; }
    public int getTechnicianAnswerTime() {
        return technicianAnswerTime; }
    public void setTechnicianAnswerTime(int technicianAnswerTime) {
        this.technicianAnswerTime = technicianAnswerTime; }
    public String getMKVersion() {
        return MKVersion; }
    public void setMKVersion(String MKVersion) {
        this.MKVersion = MKVersion; }
    public int getPhotoSizeInPics() {
        return photoSizeInPics; }
    public void setPhotoSizeInPics(int photoSizeInPics) {
        this.photoSizeInPics = photoSizeInPics; }
    public EntityLink<GPSPoint> getOfficeGPS() {
        return officeGPS; }
    public void setOfficeGPS(EntityLink<GPSPoint> office) {
        this.officeGPS = office; }
    public long getOrderSingleOid() {
        return orderSingleOid; }
    public void setOrderSingleOid(long orderSingleOid) {
        this.orderSingleOid = orderSingleOid; }
    public long getOrderCloseOid() {
        return orderCloseOid; }
    public void setOrderCloseOid(long orderCloseId) {
        this.orderCloseOid = orderCloseId; }
    public int getMovingSpeed() {
        return movingSpeed; }
    public void setMovingSpeed(int movingSpeed) {
        this.movingSpeed = movingSpeed; }
    public int getMovingInterval0() {
        return movingInterval0; }
    public void setMovingInterval0(int movingInterval0) {
        this.movingInterval0 = movingInterval0; }
    public double getMovindKoeff() {
        return movindKoeff; }
    public void setMovindKoeff(double movindKoeff) {
        this.movindKoeff = movindKoeff; }
    public OwnDateTime getLastReglamentMonth() {
        return lastReglamentMonth; }
    public void setLastReglamentMonth(OwnDateTime lastReglamentMonth) {
        this.lastReglamentMonth = lastReglamentMonth; }
    public int getGpsValidDelay() { return gpsValidDelay; }
    public void setGpsValidDelay(int gpsValidDelay) { this.gpsValidDelay = gpsValidDelay; }
    public int getGpsOnFacilityPeriod() {
        return gpsOnFacilityPeriod;
        }
    public void setGpsOnFacilityPeriod(int gpsOnFacilityPeriod) {
        this.gpsOnFacilityPeriod = gpsOnFacilityPeriod;
        }
    public int getGpsOnWayPeriod() {
        return gpsOnWayPeriod;
        }
    public void setGpsOnWayPeriod(int gpsOnWayPeriod) {
        this.gpsOnWayPeriod = gpsOnWayPeriod;
        }
    public int getMinDistanceToFacility() {
        return minDistanceToFacility;
        }
    public void setMinDistanceToFacility(int minDistanceToFacility) {
        this.minDistanceToFacility = minDistanceToFacility;
        }
    public int getWorkDayBegin() {
        return workDayBegin;
        }
    public void setWorkDayBegin(int workDayBegin) {
        this.workDayBegin = workDayBegin;
        }
    public int getWorkDayEnd() {
        return workDayEnd;
        }
    public void setWorkDayEnd(int workDayEnd) {
        this.workDayEnd = workDayEnd;
        }
    public boolean isNeedToConfirm() {
        return needToConfirm; }
    public void setNeedToConfirm(boolean needToConfirm) {
        this.needToConfirm = needToConfirm; }
    public int getHourToProc() {
        return hourToProc; }
    public void setHourToProc(int hourToProc) {
        this.hourToProc = hourToProc; }
    public int getMinuteToProc() {
        return minuteToProc; }
    public void setMinuteToProc(int minuteToProc) {
        this.minuteToProc = minuteToProc; }
    public String getAsteriskHost() {
        return asteriskHost; }
    public void setAsteriskHost(String asteriskHost) {
        this.asteriskHost = asteriskHost; }
    public String getAsteriskMail() {
        return asteriskMail; }
    public void setAsteriskMail(String asteriskMail) {
        this.asteriskMail = asteriskMail; }
    public String getAsteriskPass() {
        return asteriskPass; }
    public void setAsteriskPass(String asteriskPass) {
        this.asteriskPass = asteriskPass; }
    public String getAsteriskSecur() {
        return asteriskSecur; }
    public void setAsteriskSecur(String asteriskSecur) {
        this.asteriskSecur = asteriskSecur; }
    public int getAsteriskPort() {
        return asteriskPort; }
    public void setAsteriskPort(int asteriskPort) {
        this.asteriskPort = asteriskPort; }
    public String getDataServerFileDir() {
        return dataServerFileDir; }
    public void setDataServerFileDir(String datServerFileDir) {
        this.dataServerFileDir = datServerFileDir; }
    public boolean isTechnicianMode() {
        return technicianMode; }
    public void setTechnicianMode(boolean technicianMode) {
        this.technicianMode = technicianMode; }
    public long getTechnicianId() {
        return technicianId; }
    public void setTechnicianId(long technicianId) {
        this.technicianId = technicianId; }
    public OwnDateTime getTechnicianModeStartTime() {
        return technicianModeStartTime; }
    public void setTechnicianModeStartTime(OwnDateTime technicianModeStartTime) {
        this.technicianModeStartTime = technicianModeStartTime; }
    public OwnDateTime getTechnicianModeStopTime() {
        return technicianModeStopTime; }
    public void setTechnicianModeStopTime(OwnDateTime technicianModeStopTime) {
        this.technicianModeStopTime = technicianModeStopTime; }
    public int getDefaultDuration() {
        return defaultDuration; }
    public void setDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration; }
    public boolean isTechnicianModeActive(){
        long ll = new OwnDateTime().timeInMS();
        return technicianMode && ll >= technicianModeStartTime.timeInMS() && ll<=technicianModeStopTime.timeInMS();
        }
    public boolean isWorkTime(){
        if (workDayBegin==0 || workDayEnd==0) return true;
        OwnDateTime tt = new OwnDateTime();
        int hour = tt.hour();
        return hour>=workDayBegin && hour<=workDayEnd;
        }
    public int getAsteriskDialPeriod() {
        return asteriskDialPeriod; }
    public void setAsteriskDialPeriod(int asteriskDialPeriod) {
        this.asteriskDialPeriod = asteriskDialPeriod; }
    public String getAsteriskDialTarget() {
        return asteriskDialTarget; }
    public void setAsteriskDialTarget(String asteriskDialTarget) {
        this.asteriskDialTarget = asteriskDialTarget; }
    public String getAsteriskDialCallFile() {
        return asteriskDialCallFile; }
    public void setAsteriskDialCallFile(String asteriskDialCallFile) {
        this.asteriskDialCallFile = asteriskDialCallFile; }
    public boolean isAsteriskChefCall() {
        return asteriskChefCall; }
    public void setAsteriskChefCall(boolean asteriskChefCall) {
        this.asteriskChefCall = asteriskChefCall; }
    public boolean isConvertAtrifact() {
        return convertAtrifact; }
    public void setConvertAtrifact(boolean convertAtrifact) {
        this.convertAtrifact = convertAtrifact; }
}
