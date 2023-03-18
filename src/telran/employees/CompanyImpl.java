package telran.employees;

import java.util.*;
import java.io.*;

public class CompanyImpl implements Company {

	
	private static final long serialVersionUID = 1L;
	private HashMap<Long, Employee> employees = new HashMap<>();
	private HashMap<Integer, Set<Employee>> employeesMonth = new HashMap<>();
	private HashMap<String, Set<Employee>> employeesDepartment = new HashMap<>();
	private TreeMap<Integer, Set<Employee>> employeesSalary = new TreeMap<>();
	
	@Override
	public Iterator<Employee> iterator() {
		
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee empl) {
		boolean res = false;
		if (employees.putIfAbsent(empl.id, empl) == null) {
			res = true;
			addIndexMap(employeesMonth, empl.getBirthDate().getMonthValue(), empl);
			addIndexMap(employeesDepartment, empl.getDepartment(), empl);
			addIndexMap(employeesSalary, empl.getSalary(), empl);
		}
		
		return res;
	}

	private <T> void addIndexMap(Map<T, Set<Employee>> map, T key, Employee empl) {
		map.computeIfAbsent(key, k->new HashSet<>()).add(empl);
		
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee empl = employees.remove(id); 
		if (empl != null) {
			removeIndexMap(employeesMonth, empl.getBirthDate().getMonthValue(), empl);
			removeIndexMap(employeesDepartment, empl.getDepartment(), empl);
			removeIndexMap(employeesSalary, empl.getSalary(), empl);
		}
		return empl;
	}

	private <T>void removeIndexMap(Map<T, Set<Employee>> map, T key, Employee empl) {
		Set<Employee> set = map.get(key);
		set.remove(empl);
		if (set.isEmpty()) {
			map.remove(key);
		}
		
	}

	@Override
	public List<Employee> getAllEmployees() {
		
		return new ArrayList<>(employees.values());
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		
		return new ArrayList<>(employeesMonth.getOrDefault(month, Collections.emptySet()));
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		
		return employeesSalary.subMap(salaryFrom, true, salaryTo, true)
				.values().stream().flatMap(Set::stream).toList();
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		
		return new ArrayList<>(employeesDepartment.getOrDefault(department, Collections.emptySet()));
	}

	@Override
	public Employee getEmployee(long id) {
		
		return employees.get(id);
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))){
			output.writeObject(getAllEmployees());
		} catch(Exception e) {
			throw new RuntimeException(e.toString()); //some error
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
			List<Employee> allEmployees = (List<Employee>) input.readObject();
			allEmployees.forEach(this::addEmployee);
		}catch(FileNotFoundException e) {
			//empty object but no error
		} catch (Exception e) {
			throw new RuntimeException(e.toString()); //some error
		}

	}

}




//package telran.employees;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class CompanyImpl implements Company {
//
//	HashMap<Long, Employee> hashMap = new HashMap<Long, Employee>();
//
//	private static final long serialVersionUID = 1L;
//
//	@Override
//	public Iterator<Employee> iterator() {
//		return hashMap.values().iterator();
//	}
//
//	@Override
//	public boolean addEmployee(Employee empl) {
//		if (!hashMap.containsKey(empl.id)) {
//			hashMap.put(empl.id, empl);
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public Employee removeEmployee(long id) {
//		if (hashMap.containsKey(id)) {
//			Employee removedEmployee = hashMap.get(id);
//			hashMap.remove(id);
//			return removedEmployee;
//		}
//		return null;
//	}
//
//	@Override
//	public List<Employee> getAllEmployees() {
//		return hashMap.values().stream().collect(Collectors.toList());
//	}
//
//	@Override
//	public List<Employee> getEmployeesByMonthBirth(int month) {
//		return hashMap.values().stream().filter(empl -> empl.birthDate.getMonthValue() == month)
//				.collect(Collectors.toList());
//
//	}
//
//	@Override
//	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
//		return hashMap.values().stream().filter(empl -> empl.salary > salaryFrom & empl.salary < salaryTo)
//				.collect(Collectors.toList());
//	}
//
//	@Override
//	public List<Employee> getEmployeesByDepartment(String department) {
//		return hashMap.values().stream().filter(empl -> empl.department == department).collect(Collectors.toList());
//
//	}
//
//	@Override
//	public Employee getEmployee(long id) {
//		return hashMap.get(id);
//	}
//
//	@Override
//	public void save(String pathName) {
//		File file = new File(pathName);
//		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
//			for (Employee empl : hashMap.values()) {
//				output.writeObject(empl);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	public void restore(String pathName) {
//		File file = new File(pathName);
//		boolean cont = true;
//		while (cont) {
//			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
//				Employee obj = (Employee) input.readObject();
//				if (obj != null) {
//					hashMap.put(obj.id, obj);
//					
//				} else {
//					cont = false;
//				}
//				
//			} catch (Exception e) {
//			}
//hashMap.forEach((key, value) -> System.out.println(value));
//		}
//	}
//}
