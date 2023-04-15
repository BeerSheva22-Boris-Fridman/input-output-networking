package telran.pesah.project;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class GitRepositoryTests {
	String dirPath = "C:\\Users\\asus\\eclipse-workspace\\input-output-networking\\myDirectory1";
	GitRepositoryImpl gitImpl = new GitRepositoryImpl();
	
	@Test
	void test() {
	List <FileState> test = gitImpl.info();
	test.forEach(t -> System.out.println(t));
	gitImpl.commit("msg");
		
	}
	@Test
	void test2() {
	gitImpl.commit(dirPath);
	}
}
