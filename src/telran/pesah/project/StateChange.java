package telran.pesah.project;

public class StateChange {
    private final Status status;
    private final String commitName;

    public StateChange(Status status, String commitName) {
        this.status = status;
        this.commitName = commitName;
    }

    public Status getStatus() {
        return status;
    }

    public String getCommitName() {
        return commitName;
    }
}
