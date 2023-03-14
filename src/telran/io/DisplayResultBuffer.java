package telran.io;

public class DisplayResultBuffer extends DisplayResult {
	private final int bufferSize;

	public DisplayResultBuffer(long fileSize, long copyTime, int bufferSize) {
		super(fileSize, copyTime);
		this.bufferSize = bufferSize;
	}


	@Override
	public String toString() {
		return super.toString() + "buffer size: " + bufferSize;
	}
}
