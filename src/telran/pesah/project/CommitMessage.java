package telran.pesah.project;

import java.io.Serializable;

public class CommitMessage implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private final String commitId;
    private final String message;

    public CommitMessage(String name, String message) {
        this.commitId = name;
        this.message = message;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getMessage() {
        return message;
    }
    @Override
	public String toString() {
		return "commitName - " + commitId + ", commitMsg - " + message ;
	}
}
