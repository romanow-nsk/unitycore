package firefighter.core.utils;

import firefighter.core.UniException;
import firefighter.core.constants.Values;
import firefighter.core.entity.EntityBack;
import firefighter.core.entity.EntityLink;
import firefighter.core.export.ExCellCounter;
import firefighter.core.mongo.I_MongoDB;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
//-------------------TODO --- тип компонент: ул., пр., пер. (пока это в самом названии home="д.13")
public class Address extends EntityBack {
    private EntityLink<Street> street=new EntityLink<>(Street.class);
    private String home="";
    private String office="";
    private int type=0;         // ул-д-офф
    private EntityLink<GPSPoint> location = new EntityLink<>(GPSPoint.class);
    public Address (Street str, String hh, String off, int type0){
        street.setOidRef(str);
        home=hh;
        office=off;
        type=type0;
        }
    public Address (Street str){
        street.setOidRef(str);
        }
    public Address (Street str, String hh, String off){
        street.setOidRef(str);
        home=hh;
        office=off;
        type= Values.HHome | Values.OOffice;
        }
    public Address (Street str, String hh){
        street.setOidRef(str);
        home=hh;
        type= Values.HHome;
    }
    public Address(){}
    public int getType() {
        return type; }
    public void setType(int type) {
        this.type = type; }
    public City getCity(){ return street.getRef().getCity().getRef(); }
    public int cityType(){return  street.getRef().getCity().getRef().getType()  & 0x0F; }
    public int streetType(){ return (street.getRef().getType() & 0x0F0)>>4; }
    public int homeType(){return  (type & 0x0F00)>>8; }
    public int officeType(){return  (type & 0x0F000)>>12; }
    public String toFullString(){
        return super.toFullString()+street.toFullString()+","+ownData()+","+location.toFullString();
        }
    public String ownData(){
        return Values.TypesHome[homeType()]+home+
                (office.length()==0 ? "" : (officeType()==0 ? "" : (", "+ Values.TypesOffice[officeType()]+office)));
        }
    public String toShortString(){
        return street.toShortString()+","+
                " "+ Values.TypesHome[homeType()]+home+
                (office.length()==0 ? "" : (officeType()==0 ? "" : (", "+ Values.TypesOffice[officeType()]+office)));
            }
    public String toStringFull(){
        return toString()+"["+location+"]";
        }
    public String toString(){
        if (street.getRef()==null)
            return "";
        return street.getTitle()+","+
                " "+ Values.TypesHome[homeType()]+home+
                (office.length()==0 ? "" : (officeType()==0 ? "" : (", "+ Values.TypesOffice[officeType()]+office)));
    }
    public String toStringHome(){
        return Values.TypesStreet[streetType()]+street.getRef().getName()+","+ " "+ Values.TypesHome[homeType()]+home;
        }
    public String getTitle(){
        return toString();
        }
    public String toStringCityHome(){
        return Values.TypesCity[cityType()]+street.getRef().getCity().getRef().getName()+","+ Values.TypesStreet[streetType()]+street.getRef().getName()+","+ " "+ Values.TypesHome[homeType()]+home;
    }
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        putDBValues(prefix,document,level,mongo);
        //location.putData(prefix,document,0,null);
        }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo) throws UniException {
        getDBValues(prefix,res,level,mongo);
        //location.getData(prefix,res, 0, null);
    }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    @Override
    public void getData(Row row, ExCellCounter cnt) throws UniException{
        getXMLValues(row, cnt);
        //location.getData(row, cnt);
    }
    @Override
    public void putData(Row row, ExCellCounter cnt) throws UniException{
        putXMLValues(row, cnt);
        //location.putData(row, cnt);
        }
    @Override
    public void putHeader(String prefix, ArrayList<String> list) throws UniException{
        putXMLHeader(prefix,list);
        //location.putHeader(prefix,list);
        }
    //-------------------------------------------------------------------------------------------------------------------
    public EntityLink<Street> getStreet() {
        return street; }
    public void setStreet(EntityLink<Street> street) {
        this.street = street; }
    public String getHome() {
        return home; }
    public void setHome(String home) {
        this.home = home; }
    public String getOffice() {
        return office; }
    public void setOffice(String office) {
        this.office = office; }
    public EntityLink<GPSPoint> getLocation() {
        return location; }
    public void setLocation(EntityLink<GPSPoint> location) {
        this.location = location; }
}
