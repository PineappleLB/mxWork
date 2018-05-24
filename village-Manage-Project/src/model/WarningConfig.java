package model;

public class WarningConfig {
    private Integer id;

    private Integer alertname;

    private Integer safemin;

    private Integer safemax;

    private Integer userid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlertname() {
        return alertname;
    }

    public void setAlertname(Integer alertname) {
        this.alertname = alertname;
    }

    public Integer getSafemin() {
        return safemin;
    }

    public void setSafemin(Integer safemin) {
        this.safemin = safemin;
    }

    public Integer getSafemax() {
        return safemax;
    }

    public void setSafemax(Integer safemax) {
        this.safemax = safemax;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}