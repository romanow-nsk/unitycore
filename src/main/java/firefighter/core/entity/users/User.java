package firefighter.core.entity.users;

import firefighter.core.API.RestAPIBase;
import firefighter.core.UniException;
import firefighter.core.constants.Values;
import firefighter.core.entity.EntityLink;
import firefighter.core.entity.I_Name;
import firefighter.core.entity.Option;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.contacts.Phone;
import firefighter.core.mongo.MongoDB;
import retrofit2.Response;

import java.io.IOException;

public class User extends Person implements I_Name {
    private int typeId= Values.UndefinedType;
    //private Account account=null;
    private EntityLink<Account>  accountData = new EntityLink<>();           // Загружается по API
    private EntityLink<Artifact> photo=new EntityLink<>(Artifact.class);    // Фотография
    private long secondTableId=0;                                           // ID для производного класса
    private String sessionToken="";
    //------------------------------------------------------------------------------------------------------------------
    /*
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        super.putData(prefix,document,level,mongo);
        //putDBValues(prefix,document,level,mongo);
        account.putData("a_",document,0,null);
        }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo) throws UniException {
        super.getData(prefix,res,level,mongo);
        //getDBValues(prefix,res,level,mongo);
        account.getData("a_",res, 0, null);
        }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    @Override
    public void getData(Row row, ExCellCounter cnt) throws UniException{
        super.getData(row, cnt);
        //getXMLValues(row, cnt);
        account.getData(row, cnt);
        }
    @Override
    public void putData(Row row, ExCellCounter cnt) throws UniException{
        super.putData(row, cnt);
        //putXMLValues(row, cnt);
        account.putData(row, cnt);
        }
    @Override
    public void putHeader(String prefix, ArrayList<String> list) throws UniException{
        super.putHeader(prefix,list);
        //putXMLHeader(prefix,list);
        account.putHeader("a_",list);
        }

    */
    //--------------------------------------------------------------------------------------------------
    public User(){
        this(Values.UndefinedType,"","","","","","");
    }
    public String typeName() { return Values.UserTypeList[typeId]; }
    public User(String log, String loginPh, String pass){
        this(Values.UndefinedType,"","","",log,pass,loginPh);
        }
    public User(int typeId0, String nm1, String nm2, String nm3, String log, String pass,String loginPh){
        super(nm1,nm2,nm3);
        typeId = typeId0;
        setPhone(new Phone(loginPh));
        //account = new Account(log,loginPh,pass);
        accountData.setRef(new Account(log,loginPh,pass));
        }
    public Account getAccount(){
        return accountData.getRef()!=null ? accountData.getRef() : new Account();   //638
        //return account;   //637
        }
    public void load(User proto){
        setOid(proto.getOid());
        //account = new Account(proto.getAccount());
        accountData.setRef(new Account(proto.getAccount()));
        setLastName(proto.getLastName());
        setFirstName(proto.getFirstName());
        setMiddleName(proto.getMiddleName());
        }
    public String getTitle() {
        return shortUserName();
        }
    public String getHeader() {
        return shortUserName()+" ["+Values.UserTypeList[typeId]+"]";
        }
    public EntityLink<Artifact >getPhoto() {
        return photo; }
    public void setPhoto(EntityLink<Artifact> photo0) {
        photo=photo0; }
    public String toString(){
        return typeName()+" "+super.toString()+photo+" "+getAccount(); }
    public String toFullString(){
        return super.toFullString()+typeName()+" "+super.toString()+getAccount()+" "+photo; }
    public String getLogin(){
        return getAccount().getLogin(); }
    public String getLoginPhone(){
        return getAccount().getLoginPhone(); }
    public boolean loginPhoneValid(){
        return getAccount().loginPhoneValid(); }
    public int getTypeId() {
        return typeId; }
    public void setTypeId(int typeId) {
        this.typeId = typeId; }
    public void setAccount(Account account0) {
        //this.account = account0;
        accountData.setRef(new Account(account0));
        }
    public void setPhoto(Artifact photo0) {
        this.photo.setRef(photo0); }
    public long getSecondTableId() {
        return secondTableId; }
    public void setSecondTableId(long secondTableId) {
        this.secondTableId = secondTableId; }
    public String getSessionToken() {
        return sessionToken; }
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken; }
    public String getPassword(){ return getAccount().getPassword(); }
    public void setPassword(String ss){ getAccount().setPassword(ss); }
    public void setLoginPhone(String ss){ getAccount().setLoginPhone(ss);}
    public void setLogin (String ss){ getAccount().setLogin(ss);}
    public EntityLink<Account> getAccountData() {
        return accountData; }
    //----------------- Код полиморфного вызова для загрузки нужного класса
    public Option<User> apiUser(final RestAPIBase service, long id){
        Response<User> res4=null;
        try {
            res4 = service.getUserById("",id,0).execute();
            if (!res4.isSuccessful())
                return new Option<User>(res4.message());
            else
                return new Option<User>(res4.body());
            } catch (IOException ee) { return new Option<User>(ee.getMessage()); }
        }
    public static void main(String ss[]) throws UniException {
        MongoDB db = new MongoDB();
        db.openDB(Values.dataServerPort);
        User uu = new User(Values.UserTechnicianType,"Романов","Евгений","Леонидович","root","1234","89139449081");
        db.add(uu,0);
        System.out.println(uu.getOid());
        User xx = new User();
        db.getById(xx,uu.getOid());
        System.out.println(xx);
    }
}
