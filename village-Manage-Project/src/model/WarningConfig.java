package model;

public class WarningConfig {
    private Integer id;

    private String alertname;

    private Integer safemin;

    private Integer safemax;

    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlertname() {
        return alertname;
    }

    public void setAlertname(String alertname) {
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

    public String getUserid() {
        return username;
    }

    public void setUserid(String username) {
        this.username = username;
    }
}