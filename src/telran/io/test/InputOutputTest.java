package telran.io.test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class InputOutputTest {

	int offset = 4;
	String fileName = "myFile";
	String directoryName = "myDirectory1/myDirectory2";

	@BeforeEach
	void setUp() throws Exception {
		new File(fileName).delete();
		new File(directoryName).delete();
	}

	@Disabled
	@Test
	void test() throws IOException {
		File f1 = new File(fileName);
		// f1.createNewFile();
		assertTrue(f1.createNewFile());
		// System.out.println(f1.exists());
		File dir1 = new File(directoryName);
		assertTrue(dir1.mkdirs());
		System.out.println(dir1);
		System.out.println(dir1.getAbsolutePath());
	}

	@Test
	void printDirectoryFileTest() {
		String path = "C:\\IO test";
		int maxLevel = 3;
		System.out.println("-------------printDirectoryFile-------------");
		System.out.println();
		printDirectoryFile(path, maxLevel);
	}

	void printDirectoryFile(String path, int maxLevel) {
		if (maxLevel < 1) {
			maxLevel = Integer.MAX_VALUE;
		}
		File dir = new File(path);
		int depth = dir.toPath().toAbsolutePath().getNameCount();
		printFileName(depth, dir);
		filePrintPath(maxLevel, dir, depth);
	}

	private void filePrintPath(int maxLevel, File dir, int depth) {
		if (depth > maxLevel) {
			return;
		}
		File[] curentDir = dir.listFiles();

		for (int i = 0; i < curentDir.length; i++) {
			File current = curentDir[i];
			depth = current.toPath().toAbsolutePath().getNameCount();
			if (current.isDirectory()) {
				printFileName(depth, current);
				filePrintPath(maxLevel, current, depth);
			} else {
				printFileName(depth, current);
			}
		}
	}

	private void printFileName(int depth, File current) {
		System.out.print(" ".repeat(offset * depth));
		if (current.isDirectory()) {
			System.out.println("Directory name: " + current.getName());
		}
		if (current.isFile()) {
			System.out.println("File name: " + current.getName());
		}
		System.out.println();
	}

	@Disabled
	@Test
	void testFiles() {
		Path path = Path.of(".");
		System.out.println(path.toAbsolutePath().getNameCount());

	}

	@Test
	void printDirectoryFilesTest() throws IOException {
		String path = "C:\\IO test";
		int maxLevel = 3;
		System.out.println("-------------printDirectoryFiles-------------");
		System.out.println();
		printDirectoryFiles(path, maxLevel);
	}

	void printDirectoryFiles(String path, int maxLevel) throws IOException {
		if (maxLevel < 1) {
			maxLevel = Integer.MAX_VALUE;
		}
		Path path1 = Path.of(path);
		Files.walk(path1, maxLevel).forEach(pathStream -> filesPrintPath(pathStream));
	}

	private void filesPrintPath(Path path) {

		int depth = path.toAbsolutePath().getNameCount();
		printFileName(depth, path.toFile());

	}
}
