package Usage_calculation.models;

public class dataUsageIntervalTypeModel {

    private String type;
    private long receivedSIM1;
    private long sentSIM1;
    private long receivedSIM2;
    private long sentSIM2;
    private long receivedWifi;
    private long sentWifi;

    public String getType() {
        return this.type;
    }

    public void setReceivedSIM1(long val) {
        this.receivedSIM1 = val;
    }

    public void setType(String str) {
        this.type = str;
    }

    public long getReceivedSIM1() {
        return this.receivedSIM1;
    }

    public void setSentSIM1(long val) {
        this.sentSIM1 = val;
    }

    public long getSentSIM1() {
        return this.sentSIM1;
    }

    public void setReceivedSIM2(long val) {
        this.receivedSIM2 = val;
    }

    public long getReceivedSIM2() {
        return this.receivedSIM2;
    }

    public void setSentSIM2(long val) {
        this.sentSIM2 = val;
    }

    public long getSentSIM2() {
        return this.sentSIM2;
    }

    public void setReceivedWifi(long val) {
        this.receivedWifi = val;
    }

    public long getReceivedWifi() {
        return this.receivedWifi;
    }

    public void setSentWifi(long val) {
        this.sentWifi = val;
    }

    public long getSentWifi() {
        return this.sentWifi;
    }
}
