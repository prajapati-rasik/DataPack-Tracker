package Usage_calculation.models;

public class ForegroundBackgroundModel {

    private long ForegroundRx;
    private long ForegroundTx;
    private long BackgroundRx;
    private long BackgroundTx;

    public void setForegroundRx(long val){
        this.ForegroundRx = val;
    }

    public long getForegroundRx(){
        return this.ForegroundRx;
    }

    public void setForegroundTx(long val){
        this.ForegroundTx = val;
    }

    public long getForegroundTx() {
        return this.ForegroundTx;
    }

    public void setBackgroundRx(long BackgroundRx) {
        this.BackgroundRx = BackgroundRx;
    }

    public long getBackgroundRx() {
        return this.BackgroundRx;
    }

    public void setBackgroundTx(long BackgroundTx) {
        this.BackgroundTx = BackgroundTx;
    }

    public long getBackgroundTx() {
        return this.BackgroundTx;
    }
}
