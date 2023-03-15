package telran.employees;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompanyImpl implements Company {

	HashMap<Long, Employee> hashMap = new HashMap<Long, Employee>();

	private static final long serialVersionUID = 1L;

	@Override
	public Iterator<Employee> iterator() {
		return hashMap.values().iterator();
	}

	@Override
	public boolean addEmployee(Employee empl) {
		if (!hashMap.containsKey(empl.id)) {
			hashMap.put(empl.id, empl);
			return true;
		}
		return false;
	}

	@Override
	public Employee removeEmployee(long id) {
		if (hashMap.containsKey(id)) {
			Employee removedEmployee = hashMap.get(id);
			hashMap.remove(id);
			return removedEmployee;
		}
		return null;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return hashMap.values().stream().collect(Collectors.toList());
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		return hashMap.values().stream().filter(empl -> empl.birthDate.getMonthValue() == month)
				.collect(Collectors.toList());

	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return hashMap.values().stream().filter(empl -> empl.salary > salaryFrom & empl.salary < salaryTo)
				.collect(Collectors.toList());
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return hashMap.values().stream().filter(empl -> empl.department == department).collect(Collectors.toList());

	}

	@Override
	public Employee getEmployee(long id) {
		return hashMap.get(id);
	}

	@Override
	public void save(String pathName) {
		File file = new File(pathName);
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
			for (Employee empl : hashMap.values()) {
				output.writeObject(empl);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void restore(String pathName) {
		File file = new File(pathName);
		boolean cont = true;
		while (cont) {
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
				Employee obj = (Employee) input.readObject();
				if (obj != null) {
					hashMap.put(obj.id, obj);
					
				} else {
					cont = false;
				}
				
			} catch (Exception e) {
			}
hashMap.forEach((key, value) -> System.out.println(value));
		}
	}
}
