package firefighter.core.geocoder;


import com.fasterxml.jackson.databind.JsonNode;
import firefighter.core.utils.Address;
import firefighter.core.utils.GPSPoint;

import java.util.ArrayList;


public class ForeCodeData {
    private JsonNode node2=null;
    private JsonNode node3=null;
    private ArrayList<String> data=new ArrayList<>();
    public ForeCodeData(JsonNode node, Address addr){
        boolean isStreet = addr.getHome().length()==0;
        node=node.get("response").get("GeoObjectCollection");
        node2=node.get("metaDataProperty").get( "GeocoderResponseMetaData").get("request");
        node3=node.get("featureMember");
        int sz=node3.size();
        int j=0;
        for(int i=0;i<sz;i++){
            JsonNode node4 = node3.get(i).get("GeoObject");
            String s1 = node4.get("description").asText();
            String s2 = node4.get("name").asText();
            String cname = addr.getCity().getName();
            String sname = addr.getStreet().getRef().getName();
            boolean find1 = s1.contains(cname) || s2.contains(cname);
            boolean find2 = s1.contains(sname) || s2.contains(sname);
            if (!(find1 && find2))
                continue;
            if (node4.get("metaDataProperty").get("GeocoderMetaData").get("precision").asText().equals(isStreet ?  "street" : "exact"))
                data.add(node4.get("Point").get("pos").asText());
            }
        }
    public String toString(){
        String ss="";
        for(String zz : data) ss+=zz+"\n";
        return ss;
        }
    public GPSPoint getFirstGPSPoint(){
        if (data.size()==0)
            return new GPSPoint();
        return new GPSPoint(data.get(0),true);
        }
    /*
 {
  "response": {
    "GeoObjectCollection": {
      "metaDataProperty": {
        "GeocoderResponseMetaData": {
          "request": "Москва, улица Новый Арбат, дом 24",
          "found": "1",
          "results": "10"
        }
      },
      "featureMember": [
        {
          "GeoObject": {
            "metaDataProperty": {
              "GeocoderMetaData": {
                "kind": "house",
                "text": "Россия, Москва, улица Новый Арбат, 24",
                "precision": "exact",
                "AddressDetails": {
                  "Country": {
                    "AddressLine": "Москва, улица Новый Арбат, 24"
                    "CountryNameCode": "RU",
                    "CountryName": "Россия",
                    "AdministrativeArea":{
                      "AdministrativeAreaName": "Московская область",
                      "SubAdministrativeArea": {
                        "SubAdministrativeAreaName": "Москва",
                        "Locality": {
                          "LocalityName": "Москва",
                          "Thoroughfare": {
                            "ThoroughfareName": "улица Новый Арбат",
                            "Premise": {
                              "PremiseNumber": "24"
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            },
            "description": "Москва, Россия",
            "name": "новый арбат, 24",
            "boundedBy": {
              "Envelope": {
                "lowerCorner": "37.583490 55.750778",
                "upperCorner": "37.591701 55.755409"
              }
            },
            "Point": {
              "pos": "37.587596 55.753093"
            }
          }
        }
      ]
    }
  }
}
     */
}
