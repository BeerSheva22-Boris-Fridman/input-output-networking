package telran.pesah.project;

import java.io.Serializable;

public class FileState implements Serializable{
   
	private static final long serialVersionUID = 1L;
	
	final String fileName;
	final String filePath;
    final Status status;


    public FileState(String filePath, Status status, String fileName) {
        this.filePath = filePath;
        this.status = status;
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }



	public Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return fileName +  " - " + status;
	}

}