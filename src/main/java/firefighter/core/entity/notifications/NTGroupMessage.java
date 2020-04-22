package firefighter.core.entity.notifications;

import com.google.gson.Gson;
import firefighter.core.I_SelectObject;
import firefighter.core.I_TextFile;
import firefighter.core.UniException;
import firefighter.core.constants.Values;
import firefighter.core.entity.users.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class NTGroupMessage extends ArrayList<NTMessage> implements I_TextFile {
    public NTGroupMessage(){}
    public void forEach(I_SelectObject fun){
        for(NTMessage mes : this)
            fun.onSelect(mes);
        }
    public void save(BufferedWriter out) throws UniException {
        try {
            Gson gson = new Gson();
            out.write(""+size());
            out.newLine();
            for(NTMessage mes : this){
                out.write(gson.toJson(mes));
                out.newLine();
                }
            } catch (Exception ee){ throw UniException.io(ee); }
        }
    public void load(BufferedReader out) throws UniException{
        try {
            clear();
            Gson gson = new Gson();
            int sz = Integer.parseInt(out.readLine());
            while(sz--!=0){
                add((NTMessage) gson.fromJson(out.readLine(),NTMessage.class));
                }
            } catch (Exception ee){ throw UniException.io(ee); }
        }
    public boolean addToOld(NTMessage mes){
        for(NTMessage mm : this)
            if (mm.getType()==mes.getType() && mm.getExecuteMode()==mes.getExecuteMode()){
                if (mm.getType()== Values.NTTechnicianAction && mm.getParam()!=mes.getParam())      // Разные действия
                    continue;
                add(mes);
                return true;
            }
        return false;
        }
    public int getType(){ return get(0).getType(); }
    public User getUser(){ return get(0).getUser(); }
    public int getExecuteMode(){ return get(0).getExecuteMode(); }

}
