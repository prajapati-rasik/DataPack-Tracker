package Usage_calculation.models;

import android.net.Uri;

public class applicationUIDModel {

    private String formattedData;
    private String percentage;
    private Uri uri;
    private String label;
    private int percent;
    private String packageName;
    private int uid;
    private long data;
    private String foregroundData;
    private String backgroundData;

    public String getBackgroundData() {
        return this.backgroundData;
    }

    public String getForegroundData() {
        return this.foregroundData;
    }

    public void setBackgroundData(String backgroundData) {
        this.backgroundData = backgroundData;
    }

    public void setForegroundData(String foregroundData) {
        this.foregroundData = foregroundData;
    }

    public long getData() {
        return this.data;
    }

    public void setUid(int id) {
        this.uid = id;
    }

    public void setData(long j) {
        this.data = j;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setPercent(int i) {
        this.percent = i;
    }

    public void setFormattedData(String str) {
        this.formattedData = str;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPercentage(String str) {
        this.percentage = str;
    }

    public int getUid() {
        return this.uid;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public String getFormattedData() {
        return this.formattedData;
    }

    public String getPercentage() {
        return this.percentage;
    }

    public String getLabel() {
        return this.label;
    }

    public int getPercent() {
        return this.percent;
    }
}
