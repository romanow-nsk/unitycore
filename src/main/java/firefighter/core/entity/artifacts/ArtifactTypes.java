package firefighter.core.entity.artifacts;

import firefighter.core.constants.Values;

import java.net.URLConnection;
import java.util.HashMap;

public class ArtifactTypes {
    private static HashMap<String,Integer> extTypes = new HashMap<>();
    static {
        extTypes.put("mp3",new Integer(Values.ArtifactAudioType));
        extTypes.put("wav",new Integer(Values.ArtifactAudioType));
        extTypes.put("3gpp",new Integer(Values.ArtifactAudioType));
        extTypes.put("doc",new Integer(Values.ArtifactDocType));
        extTypes.put("docx",new Integer(Values.ArtifactDocType));
        extTypes.put("pdf",new Integer(Values.ArtifactDocType));
        extTypes.put("txt",new Integer(Values.ArtifactTextType));
        extTypes.put("mpg",new Integer(Values.ArtifactVideoType));
        extTypes.put("mp4",new Integer(Values.ArtifactVideoType));
        extTypes.put("3gp",new Integer(Values.ArtifactVideoType));
        extTypes.put("jpg",new Integer(Values.ArtifactImageType));
        extTypes.put("png",new Integer(Values.ArtifactImageType));
        }
    public static String getMimeType(String ext){
        String type = URLConnection.getFileNameMap().getContentTypeFor("a."+ext);
        return type!=null ? type : "application/"+ext;
        }
    public static int getArtifactType(String ext){
        Integer type = extTypes.get(ext.toLowerCase());
        return type!=null ? type.intValue() : Values.ArtifactOtherType;
        }
    }
