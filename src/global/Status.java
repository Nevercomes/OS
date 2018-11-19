package global;

public enum Status {

    Ready(0, "Ready"),
    Running(1, "Running"),
    Suspending(2, "Suspending"),
    Terminated(3, "Terminated"),
    Submit(4, "Submit");

    private int code;
    private String text;

    Status() {

    }

    Status(int code, String text) {
        this.code = code;
        this.text = text;
    }

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
