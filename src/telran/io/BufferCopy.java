package telran.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class BufferCopy extends Copy {
	private int bufferSize;

	public BufferCopy(String srcFilePath, String destFilePath, boolean overwrite, int bufferSize) {
		super(srcFilePath, destFilePath, overwrite);
		this.bufferSize = bufferSize;
	}

	@Override
	public long copy() {
		byte[] buffer = new byte[bufferSize];
		long res = 0L;
		try (InputStream input = new FileInputStream(srcFilePath);
				OutputStream output = new FileOutputStream(destFilePath)) {
			int length = input.read(buffer);
			while (length > 0) {
				output.write(buffer, 0, length);
				res += length;
				length = input.read(buffer);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		return res;
	}

	@Override
	public DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResultBuffer(fileSize, copyTime, bufferSize);
	}

}
