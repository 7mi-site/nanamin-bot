package xyz.n7mn.dev.earthquake.eew;


public class KyoushinMonitorJson {

    private Security security;
    private String latest_time;
    private String request_time;
    private Result result;

    public KyoushinMonitorJson(Security security, String latest_time, String request_time, Result result){
        this.security = security;
        this.latest_time = latest_time;
        this.request_time = request_time;
        this.result = result;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getLatest_time() {
        return latest_time;
    }

    public void setLatest_time(String latest_time) {
        this.latest_time = latest_time;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}

