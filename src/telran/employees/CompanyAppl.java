package telran.employees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CompanyAppl {
	static String mainName = "Company Application";
	static String AdminName = "Admin";
	static String UserName = "User";
	static CompanyImpl dataBase = new CompanyImpl();

	static Item exit = Item.exit();
	static Item close = Item.close();
	static InputOutput io = new StandardInputOutput();
	static	Set <String> departments = new HashSet<String>();

	public static void main(String[] args) {
		departments.add("department1");
		departments.add("department2");
		dataBase.restore("test.data");
		ArrayList<Item> itemsAdmin = itemsAdminFill();
		ArrayList<Item> itemsUser = itemsUserFill();

		Item[] items = new Item[3];
		items[0] = new Menu(AdminName, itemsAdmin);
		items[1] = new Menu(UserName, itemsUser);
		items[2] = close;

		Menu menu = new Menu(mainName, items);
		menu.perform(io);
	}

	private static ArrayList<Item> itemsUserFill() {
		ArrayList<Item> itemsUser = new ArrayList<>();

		Consumer<InputOutput> getAllEmployees = x -> {
			String res = "List of employees: ";
			List<Employee> listEmployee = dataBase.getAllEmployees();
			for (Employee employee : listEmployee) {
				res = res + "\n" + employee.toString();
			}
			io.writeLine(res);
		};
		
		Consumer<InputOutput> getEmployeesByMonthBirth = x -> {
			int month = io.readInt("Enter the month of birth (month number from 1 to 12):", "Wrong value of month? please try again", 1, 12);
			String res = "List of employees that born in " + month;
			List<Employee> listEmployee = dataBase.getEmployeesByMonthBirth(month);
			for (Employee employee : listEmployee) {
				res = res + "\n" + employee.toString();
			}
			io.writeLine(res);
		};
		
		Consumer<InputOutput> getEmployeesBySalary = x -> {
			int salaryFrom = io.readInt("Enter value of 'Salary from'", "Wrong value, try again", 0, Integer.MAX_VALUE);
			int salaryTo = io.readInt("Enter value of 'Salary to'", "Wrong value, try again", 0, Integer.MAX_VALUE);
			String res = "List of employees with requested salary range:";
			List<Employee> listEmployee = dataBase.getEmployeesBySalary(salaryFrom, salaryTo);
			for (Employee employee : listEmployee) {
				res = res + "\n" + employee.toString();
			}
			io.writeLine(res);
		};
		
		Consumer<InputOutput> getEmployeesByDepartment = x -> {
			String department = io.readStringOptions("Enter department name", "Such a department is not exist, try again", departments);
			String res = "List of employees with requested salary range:";
			List<Employee> listEmployee = dataBase.getEmployeesByDepartment(department);
			for (Employee employee : listEmployee) {
				res = res + "\n" + employee.toString();
			}
			io.writeLine(res);
		};
		
		Consumer<InputOutput> getEmployee = x -> {
			String res;
			long id = getId(io);
			if (dataBase.getEmployee(id) == null) {
				res = "Employee with such an id is not exist, try to enter correct id value";
				io.writeLine(res);
			} else {
			res = "Employees with requested id is:";
			String employee = dataBase.getEmployee(id).toString();
			res = res + "\n" + employee;
			io.writeLine(res);
			}
			};
		
		Item listOfImpl = Item.of("Get list of all employees", getAllEmployees);
		Item listByMonth = Item.of("Get list of employees by month of birth", getEmployeesByMonthBirth);
		Item listBySalary = Item.of("Get employees by salary range", getEmployeesBySalary);
		Item listByDepartment = Item.of("Get employees by department", getEmployeesByDepartment);
		Item getEmployeeById = Item.of("Get Employee by id", getEmployee);

		itemsUser.add(listOfImpl);
		itemsUser.add(listByMonth);
		itemsUser.add(listBySalary);
		itemsUser.add(listByDepartment);
		itemsUser.add(getEmployeeById);
		itemsUser.add(exit);
		
		
		return itemsUser;
	}

	private static ArrayList<Item> itemsAdminFill() {
		
		ArrayList<Item> itemsAdmin = new ArrayList<>();
		
		Consumer<InputOutput> addEmployee = x -> {
			String res;
			long id = getId(io);
			while (!(dataBase.getEmployee(id) == null)) {
				res = "Such an employee already exist, try another id";
				io.writeLine(res);
			    id = getId(io);
			}
				String name = getName(io);
				LocalDate birthDate = getBirthDate(io);
				String department = getDepartment(io);
				while (!departments.contains(department)) {
					res = "Such an department is not exist";
					io.writeLine(res);	
					department = getDepartment(io);
				} 
				int salary = getSalary(io);
				Employee newEmployee = new Employee(id, name, birthDate, department, salary);

				dataBase.addEmployee(newEmployee);
				res = "Employee added";
				
			
			io.writeLine(res);
			
			};

		Consumer<InputOutput> removeEmployee = x -> {
			String res;
			long id = getId(io);
			while (dataBase.getEmployee(id) == null) {
				res = "You want to remove unexisting employee, try once again with another id";
				io.writeLine(res);
				id = getId(io);
			} 
				dataBase.removeEmployee(id)	;
				res = "Eployee successfully removed";
				io.writeLine(res);
			};
		
		Consumer<InputOutput> saveCompany = x -> {			
			String pathName = "test.data";
			dataBase.save(pathName);	
			String res = "Company successfully saved";
			io.writeLine(res);
		};
		
		Consumer<InputOutput> restoreCompany = x -> {			
			String pathName = "test.data";
			dataBase.restore(pathName);	
			String res = "Company successfully restored";
			io.writeLine(res);
		};
		
		Item addEmpl = Item.of("Add employee", addEmployee);
		Item removeEmpl = Item.of("Remove Employee", removeEmployee);
		Item save = Item.of("Save company", saveCompany);
		Item restore = Item.of("Restore company", restoreCompany);
		
		itemsAdmin.add(addEmpl);
		itemsAdmin.add(removeEmpl);
		itemsAdmin.add(save);
		itemsAdmin.add(restore);
		itemsAdmin.add(exit);
		 
		return itemsAdmin;
	}

	private static int getSalary(InputOutput io) {
		return io.readInt("Enter salary value:", "please enter a number", 0, Integer.MAX_VALUE);
	}

	private static String getDepartment(InputOutput io) {

		return io.readString("Enter department name:");
	}

	private static LocalDate getBirthDate(InputOutput io) {

		return io.readDate("Enter birth date:", "Wrong data format", "yyyy-MM-dd", LocalDate.MIN, LocalDate.MAX);
	}

	private static String getName(InputOutput io) {

		return io.readString("Enter employee name:");
	}

	private static long getId(InputOutput io) {

		return io.readLong("Enter employee id:", "entered wrong data format", 0, Long.MAX_VALUE);
	}
}
