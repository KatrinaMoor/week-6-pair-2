package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.interfaces.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() 
	{
		// 1. create a container to hold the cities
				List<Employee> employees = new ArrayList<Employee>();
				
				// write your query
				String query = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date  " +
				        " FROM employee;";
				
				// execute the query
						SqlRowSet rows = jdbcTemplate.queryForRowSet(query);
				
				//Loop through results		
						while(rows.next())
						{
							Employee employee = mapRowToEmployee(rows);
							
							employees.add(employee);
						}
						
				// return the results
						return employees;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) 
	{
		// 1. create a container to hold the cities
		List<Employee> employees = new ArrayList<Employee>();
		
		// write your query
		String query = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date  " +
		        " FROM employee " +
				" WHERE first_name = ? " +
		        " AND last_name = ?; ";
		
		// execute the query
				SqlRowSet rows = jdbcTemplate.queryForRowSet(query, firstNameSearch, lastNameSearch);
		
		//Loop through results		
				while(rows.next())
				{
					Employee employee = mapRowToEmployee(rows);
					
					employees.add(employee);
				}
				
		// return the results
				return employees;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) 
	{
	List<Employee> employees = new ArrayList<Employee>();
		
		// write your query
		String query = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date  " +
		        " FROM employee " +
				" WHERE employee_id = ?;";
		
		// execute the query
				SqlRowSet rows = jdbcTemplate.queryForRowSet(query, id);
		
		//Loop through results		
				while(rows.next())
				{
					Employee employee = mapRowToEmployee(rows);
					
					employees.add(employee);
				}
				
		// return the results
				return employees;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() 
	{
	List<Employee> employees = new ArrayList<Employee>();
		
		// write your query
		String query = "SELECT e.employee_id, department_id, first_name, last_name, birth_date, gender, hire_date  " +
		        " FROM employee AS e" +
				" LEFT JOIN project_employee AS pe " +
				" ON e.employee_id = pe.employee_id " +
		        " WHERE pe.project_id IS NULL; ";
		
		// execute the query
				SqlRowSet rows = jdbcTemplate.queryForRowSet(query);
		
		//Loop through results		
				while(rows.next())
				{
					Employee employee = mapRowToEmployee(rows);
					
					employees.add(employee);
				}
				
		// return the results
				return employees;	
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) 
	{
		List<Employee> employees = new ArrayList<Employee>();
		
		// write your query
		String query = "SELECT e.employee_id, department_id, first_name, last_name, birth_date, gender, hire_date  " +
		        " FROM employee AS e" +
				" INNER JOIN project_employee AS pe" +
				" ON e.employee_id = pe.employee_id " +
		        " WHERE pe.project_id = ? ; ";
		
		// execute the query
				SqlRowSet rows = jdbcTemplate.queryForRowSet(query, projectId);
		
		//Loop through results		
				while(rows.next())
				{
					Employee employee = mapRowToEmployee(rows);
					
					employees.add(employee);
				}
				
		// return the results
				return employees;	
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) 
	{
		String changeEmployee = " UPDATE employee " +
								" SET department_id = ? " +
								" WHERE employee_id = ?; ";
		jdbcTemplate.update(changeEmployee, departmentId, employeeId);
		
	}

	
	private Employee mapRowToEmployee(SqlRowSet results)
	{
		Employee theEmployee;
		theEmployee = new Employee();
		theEmployee.setId(results.getLong("employee_id"));
		theEmployee.setDepartmentId(results.getLong("department_id"));
		theEmployee.setFirstName(results.getString("first_name"));
		theEmployee.setLastName(results.getString("last_name"));
		theEmployee.setBirthDay(results.getDate("birth_date").toLocalDate());
		theEmployee.setHireDate(results.getDate("hire_date").toLocalDate());
		theEmployee.setGender(results.getString("gender").charAt(0));
		return theEmployee;
	}
}
