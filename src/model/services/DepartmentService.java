package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	public List<Department> findAll(){
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Eletronics"));
		list.add(new Department(2, "Mechanics"));
		list.add(new Department(3, "Clothes"));
		list.add(new Department(4, "Utilities"));
		return list;		
	}
}
