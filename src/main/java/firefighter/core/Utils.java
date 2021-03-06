package firefighter.core;

import firefighter.core.constants.ValuesBase;
import firefighter.core.utils.Pair;
import firefighter.core.utils.wordwrap.WordWrap;
import retrofit2.Response;

import javax.swing.*;
import java.io.IOException;

public class Utils {
    public static boolean hasDigits(String ss){
        char cc[] = ss.toCharArray();
        for(char zz : cc)
            if (!(zz >= '0' && zz<='9'))
                return false;
         return true;
        }
    public static int nextMonth(int mnt){
        mnt++;
        if (mnt==13) mnt=1;
        return mnt;
        }
    public static int prevMonth(int mnt){
        mnt--;
        if (mnt==0) mnt=12;
        return mnt;
    }
    public static  int nextMonth(int mnt,int cnt){
        mnt+=cnt;
        if (mnt>12) mnt=mnt-12;
        return mnt;
    }
    public static String cutWithWords(String ss, int sz){
        if (ss.length()>sz)
            ss = ss.substring(0,sz);
        int idx = ss.lastIndexOf(" ");
        if (idx!=-1)
            ss = ss.substring(0,idx)+"...";
        return ss;
        }
     public static void toTextArea(String ss1, int sz, JTextArea mes){
        String ss = ss1;
        mes.setText("");
        while(true){
            if (ss.length()<sz){
                mes.append(ss);
                return;
                }
            String zz = ss.substring(0,sz);
            int idx = zz.lastIndexOf(" ");
            if (idx!=-1){
                mes.append(zz.substring(0,idx)+"\n");
                ss = ss.substring(idx+1);
                }
            else{
                mes.append(ss);
                return;
                }
            }
        }
    public static String generateToken(){
        return generateToken(ValuesBase.SessionTokenLength);
        }
    public static String generateToken(int sz){
        char cc[]=new char[sz];
        for(int i=0;i<cc.length;i++){
            int k=(int)(62*Math.random());
            if (k<26)
                cc[i]=(char)('A'+k);
            else
            if (k<52)
                cc[i]=(char)('a'+k-26);
            else
                cc[i]=(char)('0'+k-52);
        }
        return new String(cc);
        }
    public static String timeInMinToString(int time){
        if (time/(60*24)==0)
            return String.format("%2d:%2d",time/60,time%60);
        else
            return String.format("%d:%2d",time/60,time%60);
        }
    public static String timeInSecToString(int time){
        if (time/3600==0)
            return String.format("%2d:%2d",time/60,time%60);
        else
            return String.format("%2d:%2d:%2d",time/3600,(time/60)%60,time%60);
        }
    public static long getPID() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        if (processName != null && processName.length() > 0) {
            try {
                return Long.parseLong(processName.split("@")[0]);
            }
            catch (Exception e) { return 0; }
        }
        return 0;
        }
    public static String nDigits(int vv, int ndig){
        String out = "";
        while(ndig--!=0){
            out = (char)(vv%10+'0')+out;
            vv/=10;
            }
        return out;
        }
    public static String httpError(Response res){
        String ss =   res.message()+" ("+res.code()+") ";
        try {
            ss+=res.errorBody().string();
            } catch (IOException e) {}
        return ss;
        }
    public static String createFatalMessage(Throwable ee) {
        return createFatalMessage(ee,ValuesBase.FatalExceptionStackSize);
        }
    public static String createFatalMessage(Throwable ee, int stackSize) {
        String ss = ee.toString() + "\n";
        StackTraceElement dd[] = ee.getStackTrace();
        for (int i = 0; i < dd.length && i < stackSize; i++) {
            ss += dd[i].getClassName() + "." + dd[i].getMethodName() + ":" + dd[i].getLineNumber() + "\n";
        }
        String out = "Программная ошибка:\n" + ss;
        return out;
        }
    public static void printFatalMessage(Throwable ee) {
        System.out.println(createFatalMessage(ee));
        }
    public static String when(String ss){
        if (ss.length()==0) return ss;
        int idx=ss.length();
        char cc = ss.charAt(idx-1);
        switch (cc){
            case 'а': return ss.substring(0,idx-1)+'у';
            case 'о': return ss;
            case 'й': return ss.substring(0,idx-1)+'я';
            case 'я': return ss.substring(0,idx-1)+'ю';
            default:  return ss+'a';
            }
        }
    public static int lineCount(String ss){
        int idx=0,count=1;
        while(true){
            idx = ss.indexOf('\n',idx);
            if (idx==-1)
                return count;
            count++;
            idx++;
            }
        }
    public static String toHTML(String ss){
        String out="<html>";
        int idx=0,count=0;
        while(true){
            int idx1 = ss.indexOf('\n',idx);
            if (count!=0)
                out+="<br>";
            if (idx1==-1)
                return out+ss.substring(idx)+"</html>";
            else
                out+=ss.substring(idx,idx1);
            idx = idx1+1;
            count++;
            }
        }
    // https://github.com/davidmoten/word-wrap
    public static Pair<Integer,String> wordWrap(String ss, int size) throws IOException {
        if (ss.length()<=size)
            return new Pair(1,ss);
        String wrapped =  WordWrap.from(ss).maxWidth(size).insertHyphens(true).wrap();
        return new Pair(lineCount(wrapped),wrapped);
        }
    public static void main(String a[]) throws IOException {
        System.out.println(when("Иванов"));
        System.out.println(when("Зуева"));
        System.out.println(when("Евгений"));
        System.out.println(when("Петр"));
        System.out.println(when("Мария"));
        System.out.println(when("Семенович"));
        Pair ss = wordWrap("фффффффффф,фф,ффффф,фффф",20);
        System.out.println(ss.o1+" "+ss.o2);
    }
}
