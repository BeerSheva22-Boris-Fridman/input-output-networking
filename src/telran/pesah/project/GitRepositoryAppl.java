package telran.pesah.project;

import telran.view.InputOutput;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class GitRepositoryAppl {
	
	static String menuName = "Git Repository App.";
	static InputOutput io = new StandardInputOutput();

	public static void main(String[] args) {
		String currentDir = "C:\\Users\\asus\\eclipse-workspace\\input-output-networking\\myDirectory1\\Git\\.mygit";
		GitRepositoryImpl	git = GitRepositoryImpl.init(currentDir);
		Menu menu = new Menu(menuName, GitControllerItems.getItems(git));
		menu.perform(io);
	}
}
