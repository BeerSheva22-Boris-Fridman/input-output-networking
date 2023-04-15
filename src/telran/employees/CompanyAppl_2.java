package telran.employees;


import java.util.ArrayList;
import java.util.HashSet;

import java.util.Set;


import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CompanyAppl_2 {
	static String mainName = "Company Application";
	static String AdminName = "Admin";
	static String UserName = "User";
	static Company dataBase = new CompanyImpl();
	static CompanyControllerItems items = new CompanyControllerItems(dataBase);
	

	static Item exit = Item.exit();
	static Item close = Item.close();
	static InputOutput io = new StandardInputOutput();

	public static void main(String[] args) {
		
		dataBase.restore("test.data");
		
		ArrayList<Item> itemsAdmin = CompanyControllerItems.itemsAdminFill();
		ArrayList<Item> itemsUser = CompanyControllerItems.itemsUserFill();

		Item[] items = new Item[3];
		items[0] = new Menu(AdminName, itemsAdmin);
		items[1] = new Menu(UserName, itemsUser);
		items[2] = close;

		Menu menu = new Menu(mainName, items);
		menu.perform(io);
	}

}