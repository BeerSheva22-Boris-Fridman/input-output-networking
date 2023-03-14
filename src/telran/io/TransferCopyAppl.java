package telran.io;

public class TransferCopyAppl {
	private static final String TRANSFER_COPY = "TransferCopy";

	public static void main(String[] args) {
		try {
			FilesCopyBuilder builder = new FilesCopyBuilder();
			Copy copy = builder.build(TRANSFER_COPY, args);
			copy.copyRun();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
