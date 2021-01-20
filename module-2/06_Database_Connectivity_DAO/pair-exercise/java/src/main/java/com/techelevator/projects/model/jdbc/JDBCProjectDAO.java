package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.interfaces.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() 
	{
		// 1. create a container to hold the cities
				List<Project> projects = new ArrayList<Project>();
				
				// write your query
				String query = " SELECT project_id, name, from_date, to_date " +
				        " FROM project;";
				
				// execute the query
						SqlRowSet rows = jdbcTemplate.queryForRowSet(query);
				
				//Loop through results		
						while(rows.next())
						{
							Project project = mapRowToProject(rows);
							
							projects.add(project);
						}
						
				// return the results
						return projects;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) 
	{
		
		String removeEmployee = " DELETE FROM project_employee" +
								" WHERE project_id = ? " +
								" AND employee_id = ?;";
		
		jdbcTemplate.update(removeEmployee, projectId, employeeId);	
		
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) 
	{
		String addEmployee = " INSERT INTO project_employee (project_id, employee_id) " +
										" VALUES(?, ?);";
		
		
		
		jdbcTemplate.update(addEmployee, projectId, employeeId);
		
	}

	private Project mapRowToProject(SqlRowSet results)
	{
		Project theProject;
		theProject = new Project();
		theProject.setId(results.getLong("project_id"));
		theProject.setName(results.getString("name"));
		if(results.getString ("from_date") != null)
		{
		theProject.setStartDate(results.getDate("from_date").toLocalDate());
		}
		if(results.getString("to_date") != null)
		{
		theProject.setEndDate(results.getDate("to_date").toLocalDate());
		}
		return theProject;
	}
}
