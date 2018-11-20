package domain;

public class Item {

    private int start;
    private int length;
    private int status=0; // 默认未分配

    public Item() {

    }

    public Item(int start, int length) {
        this.start = start;
        this.length = length;
    }

    public Item(int start, int length, int status) {
        this.start = start;
        this.length = length;
        this.status = status;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
