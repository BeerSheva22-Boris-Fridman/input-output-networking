package telran.pesah.project;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class Commit implements Serializable {
   
	private static final long serialVersionUID = 1L;
	private final String id;
    private final CommitMessage commitMessage;
    private final Instant commitTime;

    private final String parentId; // идентификатор родительского коммита
    private final List <FileToCommit> commitedFiles;
    
    public Commit(String id, CommitMessage commitMessage, Instant commitTime, String parentId, List <FileToCommit> commitedFiles) {
        this.id = id;
        this.commitMessage = commitMessage;
        this.commitTime = commitTime;
    
        this.parentId = parentId;
		this.commitedFiles = commitedFiles;
    }

	public String getId() {
		return id;
	}

	public CommitMessage getCommitMessage() {
		return commitMessage;
	}

	public Instant getDateTime() {
		return commitTime;
	}

	public String getParentId() {
		return parentId;
	}

	public List<FileToCommit> getCommitedFiles() {
		return commitedFiles;
	}

}
