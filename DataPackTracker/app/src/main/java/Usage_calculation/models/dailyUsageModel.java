package Usage_calculation.models;

import java.util.Calendar;

public class dailyUsageModel {

    private String type;
    private Calendar start;
    private Calendar end;

    public Calendar getStart() {
        return this.start;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setStart(Calendar calendar) {
        this.start = calendar;
    }

    public Calendar getEnd() {
        return this.end;
    }

    public void setEnd(Calendar calendar) {
        this.end = calendar;
    }

    public String getType() {
        return this.type;
    }
}
