package xyz.n7mn.dev.earthquake.eew;

public class EEWData {
    private Result result;
    private String report_time;
    private String region_code;
    private String request_time;
    private String region_name;
    private String longitude;
    private Boolean is_cancel;
    private String depth;
    private String calcintensity;
    private Boolean is_final;
    private Boolean is_training;
    private String latitude;
    private String origin_time;
    private Security security;
    private String magunitude;
    private String  report_num;
    private String request_hypo_type;
    private String report_id;
    private String alertflg;

    public EEWData(Result result, String report_time, String region_code, String request_time, String region_name, String longitude, Boolean is_cancel, String depth, String calcintensity, Boolean is_final, Boolean is_training, String latitude, String origin_time, Security security, String magunitude, String report_num, String request_hypo_type, String report_id, String alertflg) {
        this.result = result;
        this.report_time = report_time;
        this.region_code = region_code;
        this.request_time = request_time;
        this.region_name = region_name;
        this.longitude = longitude;
        this.is_cancel = is_cancel;
        this.depth = depth;
        this.calcintensity = calcintensity;
        this.is_final = is_final;
        this.is_training = is_training;
        this.latitude = latitude;
        this.origin_time = origin_time;
        this.security = security;
        this.magunitude = magunitude;
        this.report_num = report_num;
        this.request_hypo_type = request_hypo_type;
        this.report_id = report_id;
        this.alertflg = alertflg;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Boolean getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(Boolean is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getCalcintensity() {
        return calcintensity;
    }

    public void setCalcintensity(String calcintensity) {
        this.calcintensity = calcintensity;
    }

    public Boolean getIs_final() {
        return is_final;
    }

    public void setIs_final(Boolean is_final) {
        this.is_final = is_final;
    }

    public Boolean getIs_training() {
        return is_training;
    }

    public void setIs_training(Boolean is_training) {
        this.is_training = is_training;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getOrigin_time() {
        return origin_time;
    }

    public void setOrigin_time(String origin_time) {
        this.origin_time = origin_time;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getMagunitude() {
        return magunitude;
    }

    public void setMagunitude(String magunitude) {
        this.magunitude = magunitude;
    }

    public String getReport_num() {
        return report_num;
    }

    public void setReport_num(String report_num) {
        this.report_num = report_num;
    }

    public String getRequest_hypo_type() {
        return request_hypo_type;
    }

    public void setRequest_hypo_type(String request_hypo_type) {
        this.request_hypo_type = request_hypo_type;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getAlertflg() {
        return alertflg;
    }

    public void setAlertflg(String alertflg) {
        this.alertflg = alertflg;
    }
}

