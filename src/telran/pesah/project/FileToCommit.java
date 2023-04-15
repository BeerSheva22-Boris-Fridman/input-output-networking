package telran.pesah.project;

import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.List;

public class FileToCommit implements Serializable{
	
private static final long serialVersionUID = 1L;
private	String fileName ;
private	String filePath;
private	List<String> storeFileContent;
private	Instant TimeOfCommit;
private Instant fileModificationTime;

	public String getFileName() {
	return fileName;
}

public List<String> getStoreFileContent() {
	return storeFileContent;
}

public Instant getTimeOfCommit() {
	return TimeOfCommit;
}

	public String getFilePath() {
	return filePath;
}

	public FileToCommit(String fileName, String filePath, List<String> storeFileContent, Instant TimeOfCommit, Instant fileModificationTime) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.storeFileContent = storeFileContent;
		this.TimeOfCommit = TimeOfCommit;	
		this.fileModificationTime = fileModificationTime;
	}

	public Instant getFileModificationTime() {
		return fileModificationTime;
	}

}
