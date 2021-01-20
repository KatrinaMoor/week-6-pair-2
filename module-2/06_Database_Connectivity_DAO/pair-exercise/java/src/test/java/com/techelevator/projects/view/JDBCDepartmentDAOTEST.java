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





public class JDBCDepartmentDAOTEST 
{
	// this is the NEW country code we use for all tests
		private static final String TEST_DEPARTMENT = "Home Destruction";

		//SingleConnectionDataSource allows for transactions -- important so that we can ROLLBACK the tests
		private static SingleConnectionDataSource dataSource;
		private static JdbcTemplate jdbcTemplate; // this is for test verification
		
		// this is the object under test
		private JDBCDepartmentDAOTEST dao;

	public JDBCDepartmentDAOTEST(SingleConnectionDataSource dataSource2) 
	{
			// TODO Auto-generated constructor stub
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		//setup the data source and transaction for ALL tests (one big transaction)
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
	public static void tearDownAfterClass() throws Exception 
	{
		// after ALL tests run destroy the data source
				dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception 
	{
		// this is the arrange that runs before each test
				updateDepartment();		

				// create a new instance so that we have a CLEAN/FRESH DAO for each test
				dao = new JDBCDepartmentDAOTEST(dataSource);
	}

	@After
	public void tearDown() throws Exception 
	{
		// ROLLBACK the changes in the database after EACH test
				dataSource.getConnection().rollback();
	}

	@Test
	public void test() throws SQLException 
	{
		//arrange
		
				//create data source
				SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
				dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
				dataSource.setUsername("postgres");
				dataSource.setPassword("postgres1");
				
				dataSource.setAutoCommit(false);
				
				// use for verification
				JdbcTemplate template = new JdbcTemplate(dataSource);
				String sqlInsertDepartment = "UPDATE department " +
						   " SET name = ? " +
						   " WHERE department_id = ?; ";
				
				// create the dao -- this is the object under test
				JDBCDepartmentDAOTEST dao = new JDBCDepartmentDAOTEST(dataSource);
				
				Department department = new Department();
				department.setName("Store Support");
				department.setId((long) 4);
				
				Department testDepartment = buildDepartment((long) 4, "Store Support");
		
				//act
				dao.save(department);
				
				//assert
				// where I test if it worked
				
				// check the database
				SqlRowSet rows = template.queryForRowSet(sqlInsertDepartment, department.getId());
				
				if(rows.next())
				{
					assertEquals(department.getName(), rows.getString("name"));
				}
				else
					fail("Because we did not get the name we were looking for: ");
				
				dataSource.getConnection().rollback();
	}

	private void save(Department department) 
	{
		// TODO Auto-generated method stub
		
	}

		// DTO helpers
		private Department buildDepartment(Long department_id, String name)
		{
			Department theDepartment;
			theDepartment = new Department();
			theDepartment.setId(department_id);
			theDepartment.setName("name");
			
			return theDepartment;
		}
	
	
	// database helpers
	public void updateDepartment()
	{
		String sqlUpdateDepartment = "UPDATE department " +
				   " SET name = ? " +
				   " WHERE department_id = ?; ";
		
		jdbcTemplate.update(sqlUpdateDepartment, TEST_DEPARTMENT);
	}
}
