package firefighter.core.geocoder;


import com.fasterxml.jackson.databind.JsonNode;

public class BackCodeData {
    private JsonNode node2=null;
    private JsonNode node3=null;
    private String data[]=new String[0];
    public BackCodeData(JsonNode node){
        node=node.get("response").get("GeoObjectCollection");
        node2=node.get("metaDataProperty").get( "GeocoderResponseMetaData").get("request");
        node3=node.get("featureMember");
        int sz=node3.size();
        data=new String[sz];
        for(int i=0;i<sz;i++)
            data[i]=node3.get(i).get("GeoObject").get("metaDataProperty").get("GeocoderMetaData").get("text").asText();
        }
    public String toString(){
        String ss="";
        for(int i=0;i<data.length;i++) ss+=data[i]+"\n";
        return ss;
        }
    /*
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
                    "CountryNameCode": "RU",
                    "CountryName": "Россия",
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
            },
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
     *
     */
}
