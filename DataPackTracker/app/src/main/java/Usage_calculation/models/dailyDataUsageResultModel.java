package Usage_calculation.models;

public class dailyDataUsageResultModel {

    private long sendBytes;
    private long receiveBytes;
    private long totalBytes;

    public long getSendBytes(){
        return this.sendBytes;
    }

    public long getReceiveBytes(){
        return this.receiveBytes;
    }

    public long getTotalBytes(){
        return this.totalBytes;
    }

    public void setSendBytes(long sBytes){
        this.sendBytes = sBytes;
    }

    public void setReceiveBytes(long rBytes){
        this.receiveBytes = rBytes;
    }

    public void setTotalBytes(long tBytes){
        this.totalBytes = tBytes;
    }
}
