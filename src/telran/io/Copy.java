package telran.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

abstract public class Copy {
	protected String srcFilePath;
	protected String destFilePath;
	protected boolean overwrite;
	
	public Copy(String srcFilePath, String destFilePath, boolean overwrite) {
		this.srcFilePath = srcFilePath;
		this.destFilePath = destFilePath;
		this.overwrite = overwrite;
	}
	
	abstract public long copy();
	
	abstract DisplayResult getDisplayResult(long copyTime, long fileSize);
	
	public void copyRun() {
		try {
			canOverwrite();
			sourceExists();
			Instant timestamp = Instant.now();
			long size = copy();
			ChronoUnit unit = ChronoUnit.MILLIS;
			DisplayResult displayResult = getDisplayResult(unit.between(timestamp, Instant.now()),
					size);
			System.out.println(displayResult);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private void sourceExists() throws Exception {
		if(!Files.exists(Path.of(srcFilePath))) {
			throw new Exception(srcFilePath + " not found");
		}
		
	}

	private void canOverwrite() throws Exception  {
		if (!overwrite && Files.exists(Path.of(destFilePath))) {
			throw new Exception(destFilePath + " cannot be overwritten");
		}
	}
	
	
	
}
