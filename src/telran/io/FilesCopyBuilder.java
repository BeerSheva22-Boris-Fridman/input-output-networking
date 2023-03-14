package telran.io;

public class FilesCopyBuilder {
	
	private static final String FILES_COPY = "FilesCopy";
	private static final String TRANSFER_COPY = "TransferCopy";
	private static final String BUFFER_COPY = "BufferCopy";
	private static final String OVERWRITE = "overwrite";

	public Copy build(String type, String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("too few arguments");
		}
		String srcFilePath = args[0];
		String destFilePath = args[1];
		boolean overwrite = getOverwriteValue(args);
		
		
		return switch (type) {
			case FILES_COPY -> new FilesCopy(srcFilePath, destFilePath, overwrite); 
			case TRANSFER_COPY -> new TransferCopy(srcFilePath, destFilePath, overwrite);
			case BUFFER_COPY ->new BufferCopy(srcFilePath, destFilePath, overwrite,
					getBufferSizeValue(args));
			default -> throw new IllegalArgumentException(type + " is not supported copying implementation");
		};
	}

	private int getBufferSizeValue(String[] args) {
		int res = 1_000_000;
		if (args.length > 3) {
			try {
				res = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Buffer Size must be an integer number");
			}
		}
		
		return res;
	}

	private boolean getOverwriteValue(String[] args) {
		
		return args.length > 2 && args[2].equalsIgnoreCase(OVERWRITE);
	}
}
