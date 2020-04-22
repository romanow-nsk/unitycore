package firefighter.core.API;

import firefighter.core.entity.artifacts.ArtifactTypes;
import firefighter.core.utils.FileNameExt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

public class RestAPICommon {
    //--------------------- Выгрузка файла - общая часть
    public static MultipartBody.Part createMultipartBody(FileNameExt fname){
        if (fname==null) return null;
        File file = new File(fname.fullName());
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = ArtifactTypes.getMimeType(fname.getExt());
        System.out.println(type);
        MediaType mType = MediaType.parse(type);
        System.out.println(mType);
        RequestBody requestFile = RequestBody.create(mType, file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "", requestFile);
        return body;
    }
}
