package firefighter.core.settings;

import firefighter.core.constants.Values;

public class Settings {
    private String dataServerIP = Values.dataServerIP;
    private int dataServerPort = Values.dataServerPort;
    private String dataServerFileDir = Values.dataServerFileDir;
    private String webServerWebLocation = Values.webServerWebLocation;
    private String mongoStartCmd = Values.mongoStartCmd;
    private String ClientVersion= Values.ClientVersion;
    private String ServerVersion= Values.ServerVersion;
    //-----------------------------------------------------------------------------------------
    public String clientVersion() { return ClientVersion; }
    public void clientVersion(String clientVersion) { ClientVersion = clientVersion; }
    public String getServerVersion() { return ServerVersion; }
    public void setServerVersion(String serverVersion) { ServerVersion = serverVersion; }
    public String mongoStartCmd() { return mongoStartCmd; }
    public void mongoStartCmd(String mongoStartCmd) { this.mongoStartCmd = mongoStartCmd; }
    public String dataServerIP() {
        return dataServerIP;
        }
    public void dataServerIP(String dataServerIP) {
        this.dataServerIP = dataServerIP;
        }
    public int dataServerPort() {
        return dataServerPort;
        }
    public void dataServerPort(int dataServerPort) {
        this.dataServerPort = dataServerPort;
        }
    public String dataServerFileDir() {
        return dataServerFileDir;
        }
    public void dataServerFileDir(String dataServerFileDir) {
        this.dataServerFileDir = dataServerFileDir;
        }
    public String webServerWebLocation() {
        return webServerWebLocation;
        }
    public void webServerWebLocation(String webServerWebLocation) {
        this.webServerWebLocation = webServerWebLocation;
        }
}
