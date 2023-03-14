package telran.io;

public class FilesCopyAppl {
	private static final String FILES_COPY = "FilesCopy";

	public static void main(String[] args) {
		try {
			FilesCopyBuilder builder = new FilesCopyBuilder();
			Copy copy = builder.build(FILES_COPY, args);
			copy.copyRun();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
