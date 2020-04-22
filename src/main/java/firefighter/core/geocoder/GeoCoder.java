package firefighter.core.geocoder;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import firefighter.core.utils.Address;
import firefighter.core.utils.City;
import firefighter.core.utils.Street;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GeoCoder {
    final static String yandexGeoCoderUrl="https://geocode-maps.yandex.ru/1.x/";
    public String foreCoding(){ return ""; }
    // http://geocode-maps.yandex.ru/1.x/?geocode=37.611,55.758&format=json&results=3 - обратное декодирование


    private int connectionTimeoutMillis	= 10*1000;
    private int readTimeoutMillis = 10*1000*2;
    public BackCodeData backCoding(double xx, double yy) throws Throwable {
        String ss="?geocode="+xx+","+yy+"&format=json&results=3";
        URLConnection connection = null;
        URL url = new URL(yandexGeoCoderUrl+ss);
        URLConnection connect=url.openConnection();
        InputStream in = connect.getInputStream();
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        StringBuffer data = new StringBuffer();
        int c;
        while ((c = isr.read()) != -1){
            data.append((char) c);
        }
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        JsonNode node=mapper.readTree(data.toString());
        BackCodeData back=new BackCodeData(node);
        return back;
        }
    //https://geocode-maps.yandex.ru/1.x/?geocode=г.Новосибирск,ул.Экваторная, 18&format=json
    public ForeCodeData foreCoding(Address addr) throws Throwable {
        String ss=  foreCoding(addr.toStringCityHome());
        ObjectMapper mapper = new ObjectMapper();               // can reuse, share globally
        JsonNode node=mapper.readTree(ss);
        ForeCodeData fore=new ForeCodeData(node,addr);
        return fore;
        }
    public String foreCoding(String str) throws Throwable {
        str = URLEncoder.encode(str, "utf-8");
        String ss = "?geocode=" + str + "&format=json";
        URLConnection connection = null;
        String zz = yandexGeoCoderUrl + ss;
        URL url = new URL(zz);
        System.out.println(zz);
        URLConnection connect = url.openConnection();
        InputStream in = connect.getInputStream();
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        StringBuffer data = new StringBuffer();
        int c;
        while ((c = isr.read()) != -1) {
            data.append((char) c);
            }
        return data.toString();
        }
    public static void main(String a[]) throws Throwable {
        GeoCoder gg = new GeoCoder();
        BackCodeData bb = gg.backCoding(37.611,55.758);
        System.out.println(bb);
        System.out.println(gg.foreCoding("%D0%B3.%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D0%B8%D0%B1%D0%B8%D1%80%D1%81%D0%BA,%D1%83%D0%BB.%D0%AD%D0%BA%D0%B2%D0%B0%D1%82%D0%BE%D1%80%D0%BD%D0%B0%D1%8F,%2018"));
        ForeCodeData ff = gg.foreCoding(new Address(new Street("Экваторная").setCity(new City("Новосибирск")), "18","",0));
        System.out.println(ff);
        ff = gg.foreCoding(new Address(new Street("Немировича-Данченко").setCity(new City("Новосибирск")),"136","",0));
        System.out.println(ff);
        ff = gg.foreCoding(new Address(new Street("Экваторная").setCity(new City("Новосибирск")), "","",0));
        System.out.println(ff);
        ff = gg.foreCoding(new Address(new Street("Экспериментаторная").setCity(new City("Новосибирск")), "","",0));
        System.out.println(ff);
    }
}
