package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.interfaces.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	public JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() 
	{
		// 1. create a container to hold the cities
		List<Department> departments = new ArrayList<Department>();
		
		// write your query
		String query = "SELECT department_id, name " +
		        " FROM department;";
		
		// execute the query
				SqlRowSet rows = jdbcTemplate.queryForRowSet(query);
		
		//Loop through results		
				while(rows.next())
				{
					Department department = mapRowToDeparment(rows);
					
					departments.add(department);
				}
				
		// return the results
				return departments;
		
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) 
	{
		// 1. create a container to hold the cities
		List<Department> departments = new ArrayList<Department>();
		
		// write your query
		String query = "SELECT department_id , name " +
				        " FROM department" +
						" WHERE name ILIKE ? ;";
		// execute the query
		SqlRowSet rows = jdbcTemplate.queryForRowSet(query, nameSearch);
		
		//Loop through results		
		while(rows.next())
		{
			Department department = mapRowToDeparment(rows);
			
			departments.add(department);
		}
		return departments;
	}

	@Override
	public void saveDepartment(Department updatedDepartment) 
	{
		String sqlUpdateDepartment = "UPDATE department " +
				   " SET name = ? " +
				   " WHERE department_id = ?; ";
	

		// then update the data
		jdbcTemplate.update(sqlUpdateDepartment, updatedDepartment.getName(),
							  updatedDepartment.getId());	
	}

	@Override
	public Department createDepartment(Department newDepartment) 
	{
		String sqlInsertDepartment = "INSERT INTO department (department_id, name) " +
				   " VALUES(?, ?) ";

		// call the nextval() function in postgres
		
		newDepartment.setId(getNextDepartment_Id()); 

		// then insert the data
		jdbcTemplate.update(sqlInsertDepartment, newDepartment.getId(),
							  newDepartment.getName());	
		return newDepartment;
	}

	@Override
	public Department getDepartmentById(Long id) 
	{
		Department department = null;
		String query = "SELECT department_id, name " +
				" FROM department " +
				" WHERE department_id = ?;";
		
		SqlRowSet rows = jdbcTemplate.queryForRowSet(query, id);
		
		//Loop through results		
		if(rows.next())
		{
			 department = mapRowToDeparment(rows);
			
		}
		return department;
	}
	
	private Department mapRowToDeparment(SqlRowSet results)
	{
		Department theDepartment;
		theDepartment = new Department();
		theDepartment.setId(results.getLong("department_id"));
		theDepartment.setName(results.getString("name"));
		
		return theDepartment;
	}

	private long getNextDepartment_Id() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_department_id')");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new city");
		}
	}
}
