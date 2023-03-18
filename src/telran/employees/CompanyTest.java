package telran.employees;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.employees.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTest {
private static final long ID1 = 123;
private static final int MONTH1 = 1;
private static final String DEPARTMENT1 = "department1";
private static final int SALARY1 = 1000;
private static final long ID2 = 124;
private static final String DEPARTMENT2 = "department2";
private static final int SALARY2 = 2000;
private static final LocalDate BIRTH2 = LocalDate.of(2000, MONTH1, 1);
private static final int MONTH2 = 2;
private static final int SALARY3 = 3000;
private static final long ID3 = 125;
private static final int SALARY4 = 4000;
private static final long ID4 = 126;
private static final long ID10 = 100000;
private static final String FILE_NAME = "test.data";
Employee empl1 = new Employee(ID1, "name", LocalDate.of(2000, MONTH1, 1), DEPARTMENT1, SALARY1);
Employee empl2 = new Employee(ID2, "name", LocalDate.of(2000, MONTH1, 1), DEPARTMENT2, SALARY2);
Employee empl3 = new Employee(ID3, "name", LocalDate.of(2000, MONTH2, 1), DEPARTMENT1, SALARY3);
Employee empl4 = new Employee(ID4, "name", LocalDate.of(2000, MONTH1, 1), DEPARTMENT2, SALARY4);
Employee[] employees = {empl1, empl2, empl3, empl4};
Company company;
	@BeforeEach
	void setUp() throws Exception {
		company = new CompanyImpl();
		for(Employee empl: employees) {
			company.addEmployee(empl);
		}
	}

	@Test
	void addEmployeeTest() {
		Employee newEmployee = new Employee(ID10, DEPARTMENT2, BIRTH2, DEPARTMENT1, SALARY1);
		assertTrue(company.addEmployee(newEmployee));
		assertFalse(company.addEmployee(newEmployee));
	}
	@Test
	void removeEmployeeTest() {
		assertEquals(empl1, company.removeEmployee(ID1));
		assertNull(company.removeEmployee(ID1));
		runTest(new Employee[] {empl2, empl3, empl4});
		
	}
	@Test
	void employeesByMonthTest() {
		assertTrue(company.getEmployeesByMonthBirth(20).isEmpty());
		Employee[] expected = {empl1, empl2, empl4};
		Employee[] actual = company.getEmployeesByMonthBirth(MONTH1).toArray(Employee[]::new);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
		company.removeEmployee(ID1);
		company.removeEmployee(ID2);
		company.removeEmployee(ID4);
		assertTrue(company.getEmployeesByMonthBirth(MONTH1).isEmpty());
	}
	@Test
	void employeesByDepartmentTest() {
		assertTrue(company.getEmployeesByDepartment("gggggg").isEmpty());
		Employee[] expected = {empl2, empl4};
		Employee[] actual = company.getEmployeesByDepartment(DEPARTMENT2).toArray(Employee[]::new);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
		company.removeEmployee(ID2);
		company.removeEmployee(ID4);
		assertTrue(company.getEmployeesByDepartment(DEPARTMENT2).isEmpty());
		
	}
	@Test
	void employeesBySalaryTest() {
		assertTrue(company.getEmployeesBySalary(100000000,100000100).isEmpty());
		Employee[] expected = {empl1, empl2, empl3};
		Employee[] actual = company.getEmployeesBySalary(SALARY1, SALARY3).toArray(Employee[]::new);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
		company.removeEmployee(ID1);
		company.removeEmployee(ID2);
		company.removeEmployee(ID3);
		assertTrue(company.getEmployeesBySalary(SALARY1, SALARY3).isEmpty());
	}

	private void runTest(Employee[] expected) {
		Employee[]actual = company.getAllEmployees().toArray(Employee[]::new);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
		
	}
	
	@Test
	@Order(1)
	void saveTest() {
		company.save(FILE_NAME);
	}
	@Test
	@Order(2)
	void restoreTest() {
		Company company2 = new CompanyImpl();
		company2.restore(FILE_NAME);
		assertIterableEquals(company, company2);
	}
	
	

}



//package telran.employees;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.time.LocalDate;
//import java.util.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//
//class CompanyTest {
//	static String filePath = "company.data";
//	Employee empl1 = new Employee(1111, "vasya", LocalDate.parse("2007-12-03"), "front", 4000);
//	Employee empl2 = new Employee(2222, "petya", LocalDate.parse("2000-12-03"), "front", 5000);
//	Employee empl3 = new Employee(3333, "dima", LocalDate.parse("2000-06-03"), "back", 6000);
//	Employee empl4 = new Employee(4444, "tanya", LocalDate.parse("2000-06-03"), "back", 7000);
//	Employee empl5 = new Employee(5555, "lena", LocalDate.parse("2000-07-03"), "back", 8000);
//	Employee empl6 = new Employee(6666, "kostya", LocalDate.parse("2000-07-03"), "dev", 9000);
//
//	Company company = new CompanyImpl();
//
//	@BeforeEach
//	void addTest() throws Exception {
//		assertTrue(company.addEmployee(empl1));
//		assertTrue(company.addEmployee(empl2));
//		assertTrue(company.addEmployee(empl3));
//		assertTrue(company.addEmployee(empl4));
//		assertTrue(company.addEmployee(empl5));
//		assertTrue(company.addEmployee(empl6));
//
//		assertFalse(company.addEmployee(empl1));
//	}
//
//	@Test
//	void getEmployeeTest() {
//		assertEquals(empl1, company.getEmployee(1111));
//		assertNull(company.getEmployee(0000));
//	}
//
//	@Test
//	void byDepartmentTest() {
//		List<Employee> listByDepartment = company.getEmployeesByDepartment("back");
//		Object[] byDep = listByDepartment.stream().map(em -> em.getId()).toArray();
//		Arrays.sort(byDep);
//		Object[] depExp = { (long) 3333, (long) 4444, (long) 5555 };
//		assertArrayEquals(depExp, byDep);
//	}
//
//	@Test
//	void byMonthTest() {
//		List<Employee> listByMonth = company.getEmployeesByMonthBirth(12);
//		Object[] byMon = listByMonth.stream().map(em -> em.getId()).toArray();
//		Arrays.sort(byMon);
//		Object[] monExp = { (long) 1111, (long) 2222 };
//		assertArrayEquals(monExp, byMon);
//	}
//
//	@Test
//	void removeTest() {
//		assertEquals(empl3, company.removeEmployee(3333));
//		assertNull(company.removeEmployee(0000));
//
//		List<Employee> listByDepartment = company.getEmployeesByDepartment("back");
//		Object[] byDep = listByDepartment.stream().map(em -> em.getId()).toArray();
//		Arrays.sort(byDep);
//		Object[] depExp = { (long) 4444, (long) 5555 };
//		assertArrayEquals(depExp, byDep);
//
//		List<Employee> listBySalary = company.getEmployeesBySalary(5500, 8500);
//		Object[] bySal = listBySalary.stream().map(em -> em.getId()).toArray();
//		Arrays.sort(bySal);
//		Object[] salExp = { (long) 4444, (long) 5555 };
//		assertArrayEquals(bySal, salExp);
//	}
//
//	@Test
//	void getAllEmployeesTest() {
//		List<Employee> listAll = company.getAllEmployees();
//		Object[] all = listAll.stream().map(em -> em.getId()).toArray();
//		Arrays.sort(all);
//		Object[] allExp = { (long) 1111, (long) 2222, (long) 3333, (long) 4444, (long) 5555, (long) 6666 };
//		assertArrayEquals(all, allExp);
//	}
//
//	@Test
//	void getEmployeesBySalaryTest() {
//		List<Employee> listBySalary = company.getEmployeesBySalary(5500, 8500);
//		Object[] bySal = listBySalary.stream().map(em -> em.getId()).toArray();
//		Arrays.sort(bySal);
//		Object[] salExp = { (long) 3333, (long) 4444, (long) 5555 };
//		assertArrayEquals(bySal, salExp);
//	}
//	@Test
//	void saveTest() {
//		company.save(filePath);
//	}
//	@Test
//	@Disabled
//	// не получается, идет зацикливание. Разбираюсь...
//	void restoreTest() {
//				company.restore(filePath);
//	}
//}
