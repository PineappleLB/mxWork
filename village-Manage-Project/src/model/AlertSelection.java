package model;

public class AlertSelection {
    private Integer id;

    private String alertname;

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
        this.alertname = alertname == null ? null : alertname.trim();
    }
}