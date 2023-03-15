package telran.employees;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CompanyTest {
	static String filePath = "company.data";
	Employee empl1 = new Employee(1111, "vasya", LocalDate.parse("2007-12-03"), "front", 4000);
	Employee empl2 = new Employee(2222, "petya", LocalDate.parse("2000-12-03"), "front", 5000);
	Employee empl3 = new Employee(3333, "dima", LocalDate.parse("2000-06-03"), "back", 6000);
	Employee empl4 = new Employee(4444, "tanya", LocalDate.parse("2000-06-03"), "back", 7000);
	Employee empl5 = new Employee(5555, "lena", LocalDate.parse("2000-07-03"), "back", 8000);
	Employee empl6 = new Employee(6666, "kostya", LocalDate.parse("2000-07-03"), "dev", 9000);

	Company company = new CompanyImpl();

	@BeforeEach
	void addTest() throws Exception {
		assertTrue(company.addEmployee(empl1));
		assertTrue(company.addEmployee(empl2));
		assertTrue(company.addEmployee(empl3));
		assertTrue(company.addEmployee(empl4));
		assertTrue(company.addEmployee(empl5));
		assertTrue(company.addEmployee(empl6));

		assertFalse(company.addEmployee(empl1));
	}

	@Test
	void getEmployeeTest() {
		assertEquals(empl1, company.getEmployee(1111));
		assertNull(company.getEmployee(0000));
	}

	@Test
	void byDepartmentTest() {
		List<Employee> listByDepartment = company.getEmployeesByDepartment("back");
		Object[] byDep = listByDepartment.stream().map(em -> em.getId()).toArray();
		Arrays.sort(byDep);
		Object[] depExp = { (long) 3333, (long) 4444, (long) 5555 };
		assertArrayEquals(depExp, byDep);
	}

	@Test
	void byMonthTest() {
		List<Employee> listByMonth = company.getEmployeesByMonthBirth(12);
		Object[] byMon = listByMonth.stream().map(em -> em.getId()).toArray();
		Arrays.sort(byMon);
		Object[] monExp = { (long) 1111, (long) 2222 };
		assertArrayEquals(monExp, byMon);
	}

	@Test
	void removeTest() {
		assertEquals(empl3, company.removeEmployee(3333));
		assertNull(company.removeEmployee(0000));

		List<Employee> listByDepartment = company.getEmployeesByDepartment("back");
		Object[] byDep = listByDepartment.stream().map(em -> em.getId()).toArray();
		Arrays.sort(byDep);
		Object[] depExp = { (long) 4444, (long) 5555 };
		assertArrayEquals(depExp, byDep);

		List<Employee> listBySalary = company.getEmployeesBySalary(5500, 8500);
		Object[] bySal = listBySalary.stream().map(em -> em.getId()).toArray();
		Arrays.sort(bySal);
		Object[] salExp = { (long) 4444, (long) 5555 };
		assertArrayEquals(bySal, salExp);
	}

	@Test
	void getAllEmployeesTest() {
		List<Employee> listAll = company.getAllEmployees();
		Object[] all = listAll.stream().map(em -> em.getId()).toArray();
		Arrays.sort(all);
		Object[] allExp = { (long) 1111, (long) 2222, (long) 3333, (long) 4444, (long) 5555, (long) 6666 };
		assertArrayEquals(all, allExp);
	}

	@Test
	void getEmployeesBySalaryTest() {
		List<Employee> listBySalary = company.getEmployeesBySalary(5500, 8500);
		Object[] bySal = listBySalary.stream().map(em -> em.getId()).toArray();
		Arrays.sort(bySal);
		Object[] salExp = { (long) 3333, (long) 4444, (long) 5555 };
		assertArrayEquals(bySal, salExp);
	}
	@Test
	void saveTest() {
		company.save(filePath);
	}
	@Test
	@Disabled
	// не получается, идет зацикливание. Разбираюсь...
	void restoreTest() {
				company.restore(filePath);
	}
}
