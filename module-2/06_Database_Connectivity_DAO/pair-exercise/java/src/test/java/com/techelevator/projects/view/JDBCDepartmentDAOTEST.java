package com.techelevator.projects.view;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;


public class JDBCDepartmentDAOTEST 
{
		// Class variables
	
		// This is the NEW country code we use for all tests
		private static final long TEST_ID = 1211;

		// SingleConnectionDataSource allows for transactions -- important so that we can ROLLBACK the tests
		private static SingleConnectionDataSource dataSource;
		private static JdbcTemplate jdbcTemplate; // this is for test verification
		
		// This is the object under test
		private JDBCDepartmentDAO dao;

	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		// Setup the data source and transaction for ALL tests (one big transaction)
				dataSource = new SingleConnectionDataSource();
				dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
				dataSource.setUsername("postgres");
				dataSource.setPassword("postgres1");

				// disable the transaction auto commit - so that we can ROLLBACK
				dataSource.setAutoCommit(false);

				// we will use the jdbcTemplate to verify database results
				jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@AfterClass
	public static void tearDownAfterClass() throws SQLException 
	{
		// After ALL tests run destroy the data source
		dataSource.destroy();
	}

	@Before
	public void setUp() 
	{
		// Create a new instance so that we have a CLEAN/FRESH DAO for each test
		dao = new JDBCDepartmentDAO(dataSource);
	}

	@After
	public void tearDown() throws SQLException 
	{
		// ROLLBACK the changes in the database after EACH test
		dataSource.getConnection().rollback();
	}

	@Test
	public void saveDepartment_updates_to_department() throws SQLException 
	{
			// arrange
			Department testDepartment = buildDepartment( 4, "Store Support");
		
			// act
			dao.saveDepartment(testDepartment);
			Department actualDepartment = selectDepartmentById(testDepartment.getId());
				
			//assert
			// where I test if it worked
				
			assertNotNull(testDepartment.getId());
			assertDepartmentsAreEqual(testDepartment, actualDepartment);
					
				
	}

		// Assertion helpers
	private void assertDepartmentsAreEqual(Department expected, Department actual)
	{
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());
	}

		// DTO helpers
	private Department buildDepartment(long id, String name)
	{
		Department department = new Department();
		department.setId(id);
		department.setName(name);
		return department;
	}
	
	
	   // Database helpers -- used to verify results in the database
	private Department selectDepartmentById(long id)
	{
		Department department = null;
		
		String sql = "SELECT department_id"
				   + ", name "
				   + "FROM department "
				   + "WHERE department_id = ?;";
		
		
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);
		
		if(rows.next())
		{
			String name = rows.getString("name");
			department = buildDepartment(id, name);
		}
		return department;
		
		
	}
	
}
