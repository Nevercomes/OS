package global;

public enum Oper {

    CPU_SCHEDULE("CPU SCHEDULE"),
    JOB_SCHEDULE("JOB_SCHEDULE"),
    SUSPEND("PROCESS_SUSPEND"),
    REMOVE_SUSPENSION("PROCESS_REMOVE_SUSPENSION"),
    PROCESS_DONE("PROCESS_DONE"),
    PROCESS_TERMINATE("PROCESS_TERMINATE"),
    PROCESS_SUBMIT("PROCESS_SUBMIT");

    private String text;

    Oper() {

    }

    Oper(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
