package telran.io;

public class BufferCopyAppl {
	private static final String BUFFER_COPY = "BufferCopy";

	public static void main(String[] args) {
		try {
			FilesCopyBuilder builder = new FilesCopyBuilder();
			Copy copy = builder.build(BUFFER_COPY, args);
			copy.copyRun();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
