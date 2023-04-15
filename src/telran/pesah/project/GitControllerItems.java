package telran.pesah.project;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import telran.employees.Employee;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class GitControllerItems {

	private static GitRepositoryImpl GitRepository;
	private static Item exit = Item.exit();

	static InputOutput io = new StandardInputOutput();

	public GitControllerItems() {

	}

	public static Item[] getItems(GitRepositoryImpl gitRepository) {
		GitRepository = gitRepository;
		Item[] items = new Item[7];
		items[0] = fileState();
		items[1] = commit();
		items[2] = log();
		items[3] = commitContent();
		items[4] = getHead();
		items[5] = createBranch();
		items[6] = close();
//	items[7] = ;
//	items[8] = ;
//	items[9] = ;
//	items[10] = ;
//	items[11] = close;

		return items;
	}

	public static Item commit() {
		Consumer<InputOutput> commit = x -> {
			String msg = io.readString("Enter commit message");
			String res = GitRepository.commit(msg);
			io.writeLine(res);
		};
		return Item.of("Commit", commit);
	}

	public static Item close() {
		Consumer<InputOutput> close = x -> {
			GitRepository.save();
			String res = "project sucsesfully saved in .mygit file";
			io.writeLine(res);
		};

		return Item.of("Save & exit", close, true);
	}

	public static Item fileState() {
		Consumer<InputOutput> fileState = x -> {
			List<FileState> list = GitRepository.info();
			if (list.isEmpty()) {
				io.writeLine("empty directory");
			} else {
				list.stream().forEach(i -> io.writeLine(i.toString()));
			}
		};
		return Item.of("Info (get current files state)", fileState);
	}

	public static Item log() {
		Consumer<InputOutput> log = x -> {
			List<CommitMessage> logList = GitRepository.log();
			if (logList.isEmpty()) {
				io.writeLine("No any commits yet");
			} else {
				logList.stream().forEach(i -> io.writeLine(i.toString()));
			}
		};
		return Item.of("Log", log);
	}

	public static Item commitContent() {
		Consumer<InputOutput> commitContent = x -> {
			String commitName = io.readString("Enter commit name");
			List<Path> pathList = GitRepository.commitContent(commitName);
			if (pathList.isEmpty()) {
				io.writeLine("No any commits with such a name");
			} else {
				pathList.stream().forEach(p -> io.writeLine(p.toString()));
			}
		};
		return Item.of("CommitContent", commitContent);
	}

	public static Item getHead() {
		Consumer<InputOutput> getHead = x -> {
			String head = GitRepository.getHead();
			if (head == null) {
				io.writeLine("Head is not exist yet");
			} else {
				io.writeLine("Head name is - " + "\"" + head + "\"");
			}
		};
		return Item.of("Get head", getHead);
	}
	
	public static Item createBranch() {
		Consumer<InputOutput> createBranch = x -> {
			String branchName = io.readString("Enter branch name"); 
			String res = GitRepository.createBranch(branchName);
			io.writeLine(res);
		};
		return Item.of("Create new branch", createBranch);
	}

}
