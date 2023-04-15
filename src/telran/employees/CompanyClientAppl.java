package telran.employees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import telran.employees.net.CompanyNetProxy;
import telran.net.TcpClient;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CompanyClientAppl {
	static String mainName = "Company Application";
	static String AdminName = "Admin";
	static String UserName = "User";
	static String hostname = "localhost";
	static int port = 4000;
	static Item exit = Item.exit();
	static Item close = Item.close();
	static InputOutput io = new StandardInputOutput();

	public static void main(String[] args) throws Exception {
		TcpClient tcpClient = new TcpClient(hostname, port);
		Company dataBase = new CompanyNetProxy(tcpClient);
		CompanyControllerItems controller = new CompanyControllerItems(dataBase);
		dataBase.restore("test.data");
		ArrayList<Item> itemsAdmin = CompanyControllerItems.itemsAdminFill();
		ArrayList<Item> itemsUser = CompanyControllerItems.itemsUserFill();

		Item[] items = new Item[3];
		items[0] = new Menu(AdminName, itemsAdmin);
		items[1] = new Menu(UserName, itemsUser);
		items[2] = controller.closeConnection();

		Menu menu = new Menu(mainName, items);
		menu.perform(io);
	}

}
