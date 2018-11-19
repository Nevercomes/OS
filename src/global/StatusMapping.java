package global;

public class StatusMapping {
    public static Status getStatus(int code) {
        switch (code) {
            case 0:
                return Status.Ready;
            case 1:
                return Status.Running;
            case 2:
                return Status.Suspending;
            case 3:
                return Status.Terminated;
            case 4:
                return Status.Submit;
            default:
                return Status.Submit;
        }
    }

    public static Status getStatus(String text) {
        switch (text) {
            case "Ready":
                return Status.Ready;
            case "Running":
                return Status.Running;
            case "Suspending":
                return Status.Suspending;
            case "Terminated":
                return Status.Terminated;
            case "Submit":
                return Status.Submit;
            default:
                return Status.Submit;
        }
    }
}
