package firefighter.core.entity.notifications;

import firefighter.core.constants.Values;
import firefighter.core.entity.Entity;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.users.User;
import firefighter.core.utils.OwnDateTime;

public class NTMessage extends Entity {
    private User user = new User();                     // Для техника, id по таблице USERов
    private Artifact artifact=null;                     // Связанный артефакт
    private String message="";                          // Текст сообщения
    private String header="";                           // Заголвок сообщения
    private int userSenderType=Values.UndefinedType;    // Тип (роль) отправителя
    private int userReceiverType=Values.UndefinedType;  // Тип (роль) приемника
    private long param=0;                               // id сущности или индекс формы для МК
    private OwnDateTime sndTime=new OwnDateTime();      // Дата/время отправки
    private OwnDateTime recTime=new OwnDateTime();      // Дата/время получения
    private int executeMode=Values.NMUserAck;           // Важное (не сбрасывается при просмотре, выполняется сразу)
    private int state=Values.NSSend;                    // Состояние приема
    private int type= Values.UndefinedType;             // Тип уведомления
    public NTMessage(NTMessage two){
        user = two.user;
        artifact = two.artifact;
        message = two.message;
        header = two.header;
        userSenderType = two.userSenderType;
        userReceiverType = two.userReceiverType;
        param = two.param;
        executeMode = two.executeMode;
        type = two.type;
        state = two.state;
        }
    public String getTitle(){
        String ss = Values.UserTypeList[userSenderType]+" "+user.getTitle()+" "+Values.NTypes[type]+": "+sndTime.timeToString()+" "+header;
        return ss;
        }
    public String toShortString(){
        return getOid()+" "+Values.NState[state]+" "+Values.NTypes[type];
        }
    public String toString(){
        String ss = toShortString()+ " "+Values.UserTypeList[userSenderType]+"-->"+Values.UserTypeList[userReceiverType];
        if (user.getOid()!=0) ss+=" "+user.shortUserName();
        ss+="\n"+sndTime.timeToString()+" "+header+": "+message;
        return ss;
        }
    public NTMessage(){}
    public NTMessage(int type0, int sndType0, int recType0, long userId, String head, String mes){
        type = type0;
        userSenderType = sndType0;
        userReceiverType = recType0;
        user.setOid(userId);
        header = head;
        message=mes;
        }
    public NTMessage(int type0, int sndType0, int recType0, User uu, String head, String mes){
        type = type0;
        userSenderType = sndType0;
        userReceiverType = recType0;
        user.setOid(uu.getOid());
        user.setMiddleName(uu.getMiddleName());
        user.setFirstName(uu.getFirstName());
        user.setLastName(uu.getLastName());
        header = head;
        message=mes;
    }
    public String getHeader() {
        return header; }
    public void setHeader(String header) {
        this.header = header; }
    public User getUser() {
        return user; }
    public NTMessage setUser(User user) {
        this.user = user;
        return this; }
    public Artifact getArtifact() {
        return artifact; }
    public NTMessage setArtifact(Artifact artifact) {
        this.artifact = artifact;
        return this; }
    public String getMessage() {
        return message; }
    public void setMessage(String message) {
        this.message = message; }
    public int getUserSenderType() {
        return userSenderType; }
    public void setUserSenderType(int userSenderType) {
        this.userSenderType = userSenderType; }
    public int getUserReceiverType() {
        return userReceiverType; }
    public void setUserReceiverType(int userReceiverType) {
        this.userReceiverType = userReceiverType; }
    public long getParam() {
        return param; }
    public NTMessage setParam(long dataOid) {
        this.param = dataOid;
        return this; }
    public OwnDateTime getSndTime() {
        return sndTime; }
    public void setSndTime(OwnDateTime sndTime) {
        this.sndTime = sndTime; }
    public OwnDateTime getRecTime() {
        return recTime; }
    public void setRecTime(OwnDateTime recTime) {
        this.recTime = recTime; }
    public int getExecuteMode() {
        return executeMode; }
    public NTMessage setExecuteMode(int mode) {
        executeMode = mode;
        return this; }
    public int getState() {
        return state; }
    public void setState(int state) {
        this.state = state; }
    public int getType() {
        return type; }
    public void setType(int type) {
        this.type = type; }
    //==========================================================================================

}
