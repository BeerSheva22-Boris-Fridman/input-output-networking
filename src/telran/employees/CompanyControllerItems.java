package telran.employees;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import telran.employees.net.CompanyNetProxy;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.StandardInputOutput;

public class CompanyControllerItems {

	private static Company company;

	private static Item exit = Item.exit();
//		
	static Item close = Item.close();
	static InputOutput io = new StandardInputOutput();
	static HashSet<String> departments = new HashSet<String>();

	public CompanyControllerItems(Company company) {
		super();
		this.company = company;
	}

	public static HashSet <String>  addDepartments(HashSet <String> departments) {
		departments.add("department1");
		departments.add("department2");
		return departments;
	}
	
	public Item closeConnection() {
		Consumer<InputOutput> closeClientConnection = x -> {

			try {
				((CompanyNetProxy) company).close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String res = "Connection successfully closed";
			io.writeLine(res);
		};
		return Item.of("Close connection", closeClientConnection, true);
	}

	public static ArrayList<Item> itemsUserFill() {
		ArrayList<Item> itemsUser = new ArrayList<>();

		Consumer<InputOutput> getAllEmployees = x -> {
			String res = "List of employees: ";
			List<Employee> listEmployee = company.getAllEmployees();
			for (Employee employee : listEmployee) {
				res = res + "\n" + employee.toString();
			}
			io.writeLine(res);
		};

		Consumer<InputOutput> getEmployeesByMonthBirth = x -> {
			int month = io.readInt("Enter the month of birth (month number from 1 to 12):",
					"Wrong value of month? please try again", 1, 12);
			String res = "List of employees that born in " + month;
			List<Employee> listEmployee = company.getEmployeesByMonthBirth(month);
			for (Employee employee : listEmployee) {
				res = res + "\n" + employee.toString();
			}
			io.writeLine(res);
		};

		Consumer<InputOutput> getEmployeesBySalary = x -> {
			int salaryFrom = io.readInt("Enter value of 'Salary from'", "Wrong value, try again", 0, Integer.MAX_VALUE);
			int salaryTo = io.readInt("Enter value of 'Salary to'", "Wrong value, try again", 0, Integer.MAX_VALUE);
			String res = "List of employees with requested salary range:";
			List<Employee> listEmployee = company.getEmployeesBySalary(salaryFrom, salaryTo);
			for (Employee employee : listEmployee) {
				res = res + "\n" + employee.toString();
			}
			io.writeLine(res);
		};

		Consumer<InputOutput> getEmployeesByDepartment = x -> {
			addDepartments(departments);
			String department = io.readStringOptions("Enter department name",
					"Such a department is not exist, try again", departments);
			String res = "List of employees with requested salary range:";
			List<Employee> listEmployee = company.getEmployeesByDepartment(department);
			for (Employee employee : listEmployee) {
				res = res + "\n" + employee.toString();
			}
			io.writeLine(res);
		};

		Consumer<InputOutput> getEmployee = x -> {
			String res;
			long id = getId(io);
			if (company.getEmployee(id) == null) {
				res = "Employee with such an id is not exist, try to enter correct id value";
				io.writeLine(res);
			} else {
				res = "Employees with requested id is:";
				String employee = company.getEmployee(id).toString();
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

	public static ArrayList<Item> itemsAdminFill() {

		ArrayList<Item> itemsAdmin = new ArrayList<>();

		Consumer<InputOutput> addEmployee = x -> {
			String res;
			long id = getId(io);
			while (!(company.getEmployee(id) == null)) {
				res = "Such an employee already exist, try another id";
				io.writeLine(res);
				id = getId(io);
			}
			String name = getName(io);
			LocalDate birthDate = getBirthDate(io);
			addDepartments(departments);
			String department = getDepartment(io);
			while (!departments.contains(department)) {
				res = "Such an department is not exist";
				io.writeLine(res);
				department = getDepartment(io);
			}
			int salary = getSalary(io);
			Employee newEmployee = new Employee(id, name, birthDate, department, salary);
			company.addEmployee(newEmployee);
			res = "Employee added";
			io.writeLine(res);
		};

		Consumer<InputOutput> removeEmployee = x -> {
			String res;
			long id = getId(io);
			while (company.getEmployee(id) == null) {
				res = "You want to remove unexisting employee, try once again with another id";
				io.writeLine(res);
				id = getId(io);
			}
			company.removeEmployee(id);
			res = "Eployee successfully removed";
			io.writeLine(res);
		};

//			
		Consumer<InputOutput> saveCompany = x -> {
			String pathName = "test.data";
			company.save(pathName);
			String res = "Company successfully saved";
			io.writeLine(res);
		};

//			
		Consumer<InputOutput> restoreCompany = x -> {
			String pathName = "test.data";
			company.restore(pathName);
			String res = "Company successfully restored";
			io.writeLine(res);
		};

		Item addEmpl = Item.of("Add employee", addEmployee);
		Item removeEmpl = Item.of("Remove Employee", removeEmployee);
//			
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
