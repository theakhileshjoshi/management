package management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import javax.swing.JOptionPane;
/**
 * This class will be used as a backend API for postgresql and Java Application connectivity for various operations regarding employee-database management application project
 * @author theak
 *
 */
public class backEnd {
	/**
	 * Declarations to be filled
	 */
	public static String username, password;
	static String jdbc = "jdbc:postgresql://localhost:5432/";
	static String jdbcURL = "jdbc:postgresql://localhost:5432/emp_dept_management";

	static Connection connection;// Connection object
	static ResultSet result;// resultset object
	SimpleFormatterLogging logs;// simpleformatter logging object
	static int flag;//

	/**
	 * Sets the String _username
	 * 
	 * @param _username this is the username
	 */

	public void setUsername(String _username) {
		username = _username;
	}

	/**
	 * sets the String _password
	 * 
	 * @param _password this is the password
	 */

	public void setPassword(String _password) {
		password = _password;
	}

	/**
	 * This is a constructor which creates a log page using a flag which checks for
	 * first time run.
	 * 
	 * 
	 */
	public backEnd() {
		if (flag == 0) {
			try {
				logs = new SimpleFormatterLogging("Logs");
				logs.logger.setLevel(Level.ALL);
				logs.logger.info("New Session Started Successfully");
				flag = 1;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				logs = new SimpleFormatterLogging("Logs");
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logs.logger.setLevel(Level.ALL);
		}
	}

	/**
	 * This function will create a database of specific constraints and
	 * credentialsif not present in the database
	 */

	public void createDatabase() {
		String create_db = "CREATE DATABASE emp_dept_management";

		String create_emp_table = "Create Table emp_db(emp_id int primary key NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 1000000 CACHE 1 ), emp_fname text COLLATE pg_catalog.\"default\" NOT NULL, emp_lname text COLLATE pg_catalog.\"default\" NOT NULL, emp_age int NOT NULL)";

		String create_dept_table = "Create Table dept_db(dept_id int primary key NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 1000000 CACHE 1 ),dept_name text COLLATE pg_catalog.\"default\" NOT NULL)";

		String create_emp_dept_table = "Create Table emp_dept(emp_id integer, dept_id integer, CONSTRAINT dept_id FOREIGN KEY (dept_id) REFERENCES public.dept_db (dept_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE  NOT VALID, CONSTRAINT emp_id FOREIGN KEY (emp_id) REFERENCES public.emp_db (emp_id) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE CASCADE NOT VALID)";

		String databaseName = null;
		try {
			connection = DriverManager.getConnection(jdbc, username, password);
			PreparedStatement statement = connection.prepareStatement(create_db);
			ResultSet resultSet = connection.getMetaData().getCatalogs();
			while (resultSet.next()) {
				databaseName = resultSet.getString(1);
			}
			System.out.println(databaseName);
			if (databaseName != "emp_dept_management") {
				statement.executeUpdate();
				connection = DriverManager.getConnection(jdbcURL, username, password);
				PreparedStatement statement1 = connection.prepareStatement(create_emp_table);
				PreparedStatement statement2 = connection.prepareStatement(create_dept_table);
				PreparedStatement statement3 = connection.prepareStatement(create_emp_dept_table);
				statement1.executeUpdate();
				statement2.executeUpdate();
				statement3.executeUpdate();

				logs.logger.info("A new Database was created successfully.");
			} else {
				logs.logger.info("Connecting to the existing database.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			int error = e.getErrorCode();
			if (error != 0) {
				e.printStackTrace();
				logs.logger.warning("Error Creating a database");
			}
		}
	}

	/**
	 * This function will check for initialization of connection if not initialized
	 * connect to the database
	 * 
	 * @return This will return True if connection is maintained or False if
	 *         connection is unsuccessful
	 */
	public boolean init() {

		try {
			// Connecting to the database using jdbc driver.
			connection = DriverManager.getConnection(jdbcURL, username, password);

			return true;// returning true after connection is successful

		} catch (SQLException e) {
			e.printStackTrace();
			logs.logger.warning("Error connecting to database");
		}
		return false;

	}

	/**
	 * This function should be called to add a row to the employee database
	 * 
	 * @param fname EmployeeFirstName Used to fill the database
	 * @param lname EmployeeLastName Used to fill the database
	 * @param age   EmployeeAge Used to fill the database
	 * @return True/False according to completion of the event
	 */

	public boolean enter_emp_data(String fname, String lname, int age) {
		// This function adds a row inside the employ database.
		if (init()) {
			String sql = "INSERT INTO emp_db(emp_fname,emp_lname,emp_age)" + "VALUES(?,?,?)";
			try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, fname);
				statement.setString(2, lname);
				statement.setInt(3, age);

				int rows = statement.executeUpdate();
				if (rows > 0) {
					connection.close();
					logs.logger.info(
							String.format("New Employ with Name: '%s' '%s' age: '%d' added to the employee database",
									fname, lname, age));
					return true;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				logs.logger.warning("Failed adding a employee.");
			}
		}

		return false;
	}

	/**
	 * This function should be called to add a row to the department database
	 * 
	 * @param dept_name DepartmentName
	 * @return Boolean True/False according to completion of event
	 */
	public boolean enter_dept_data(String dept_name) {
		if (init()) {
			String sql = "INSERT INTO dept_db(dept_name)" + "VALUES(?)";
			PreparedStatement statement;
			try {
				statement = connection.prepareStatement(sql);
				statement.setString(1, dept_name);// Sets dept_name as the first value
				int rows = statement.executeUpdate();
				if (rows > 0) {
					connection.close();
					logs.logger
							.info(String.format("New department with department name : '%s' was created", dept_name));
					return true;
				}
			} catch (SQLException e) {
				logs.logger.warning("Failed adding a new department");
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * This function should be called to Search Employee details
	 * 
	 * @param emp_id Employee id in integer
	 * @return SearchResult in ResultSet Object
	 */
	public ResultSet searchEmpUsingID(int emp_id) {
		if (init()) {
			try {
				String read = String.format("select * from emp_db where emp_id = '%d'", emp_id);
				Statement create_statement = connection.createStatement();
				result = create_statement.executeQuery(read);
				connection.close();
				logs.logger.info(String.format("Employee id: '%d' was searched ", emp_id));
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
				logs.logger.warning("Failed searching for employee");
			}

			return result;
		} else {
			JOptionPane.showMessageDialog(null, "Connect to the databse first");
		}
		return null;
	}

	/**
	 * This function should be called to search department detail
	 * 
	 * @param dept_id Integer Department ID
	 * @return Search Result in ResultSet Object
	 */

	public ResultSet searchDeptUsingID(int dept_id) {
		try {
			String read = String.format("select * from dept_db where dept_id = '%d'", dept_id);
			Statement create_statement = connection.createStatement();
			ResultSet result = create_statement.executeQuery(read);
			logs.logger.info(String.format("Department id : '%d' was searched", dept_id));
			connection.close();
			return result;
		} catch (SQLException e) {
			logs.logger.warning("Failed searching department.");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This function should be called to add a row to the bridge relation database
	 * 
	 * @param emp_id  Integer Employ ID
	 * @param dept_id Integer Department ID
	 * @return Boolean True When Employee Added else False
	 */

	public boolean addEmpToDept(int emp_id, int dept_id) {
		String step1 = "INSERT INTO emp_dept(emp_id,dept_id)" + "VALUES(?,?)";
		if (init()) {

			try {
				PreparedStatement statement = connection.prepareStatement(step1);
				statement.setInt(1, emp_id);
				statement.setInt(2, dept_id);

				int rows = statement.executeUpdate();
				if (rows > 0) {
					connection.close();
					logs.logger.info(String.format("Employee no :'%d' successfully added to Department no :'%d' ",
							emp_id, dept_id));
					return true;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				logs.logger.warning("Error Adding employee to department.");
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * This function should be called to Change department of any employee from
	 * current department to a new one
	 * 
	 * @param emp_id     Integer Employee ID : This employee ID will be used to search employee's details
	 * @param olddept_id Integer Old Department ID :This is the current department that is to be changed.
	 * @param newdept_id Integer New Department ID : This is the new department that is to be alloted.
	 * @return True When department change is successful else false
	 */

	public boolean changeEmpDept(int emp_id, int olddept_id, int newdept_id) {
		String sql1 = String.format("UPDATE emp_dept SET dept_id = '%d' Where emp_id = '%d' AND dept_id = '%d'",
				newdept_id, emp_id, olddept_id);

		if (init()) {
			try {
				Statement statement = connection.createStatement();

				int rows = statement.executeUpdate(sql1);
				if (rows > 0) {
					connection.close();
					logs.logger.info(String.format("Changed Department of Employee no : '%d' from '%d' to '%d'", emp_id,
							olddept_id, newdept_id));
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logs.logger.warning("Error changing department for the given employee");
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * This function will be used to call the whole employee database and can also
	 * be used to update the Jtables.
	 * 
	 * @return Whole Employ database in a ResultSet Object
	 */

	public ResultSet complete_emp_table() {
		if (init()) {
			String read = "SELECT * FROM emp_db";

			try {
				Statement create_statement = connection.createStatement();
				ResultSet result = create_statement.executeQuery(read);
				connection.close();
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;

	}

	/**
	 * This function is used to search for departments alloted to a employee using his employee ID
	 * 
	 * @param emp_id This will be used to search department
	 * @return Departments alloted to a employee in ResultSet Object
	 */

	public ResultSet dept_data(int emp_id) {
		if (init()) {
			ResultSet result = null;
			String read = String.format("SELECT dept_id FROM emp_dept WHERE emp_id = '%d'", emp_id);
			try {
				Statement create_statement = connection.createStatement();
				result = create_statement.executeQuery(read);
				connection.close();
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}

	/**
	 * This is a search engine designed for the database where you can fill any
	 * amount of detail you know about the employee and get the employee's details
	 * 
	 * @param emp_id    This is the employee ID used to search using ID
	 * @param emp_fname This is the First name of the employee used to search
	 * @param emp_lname This is the Last name of the employee used to search
	 * @param emp_age   This is the Age of the employee used to search
	 * @return Relevant Search Results according to the parameters entered in a
	 *         ResultSet object
	 */
	public ResultSet searchUsingAnything(String emp_id, String emp_fname, String emp_lname, String emp_age) {
		if (init()) {

			try {
				String id_string;
				String fname_string_and;
				String fname_string;

				String lname_string_and;
				String lname_string;

				String age_string_and;
				String age_string;
				boolean flag = false;

				String read = String.format("select * from emp_db where");
				if (!emp_id.isEmpty()) {
					id_string = String.format(" emp_id ='%s'", emp_id);
					read = read + id_string;
					flag = true;
				}
				if (!emp_fname.isEmpty()) {
					if (flag) {
						fname_string_and = String.format(" AND emp_fname = '%s'", emp_fname);
						read = read + fname_string_and;
						flag = true;
					} else {
						fname_string = String.format(" emp_fname = '%s'", emp_fname);
						read = read + fname_string;
						flag = true;
					}
				}
				if (!emp_lname.isEmpty()) {
					if (flag) {
						lname_string_and = String.format(" AND emp_lname = '%s'", emp_lname);
						read = read + lname_string_and;
						flag = true;
					} else {
						lname_string = String.format(" emp_lname = '%s'", emp_lname);
						read = read + lname_string;
						flag = true;
					}
				}
				if (!emp_age.isEmpty()) {
					if (flag) {
						age_string_and = String.format(" AND emp_age = '%s'", emp_age);
						read = read + age_string_and;
						flag = true;
					} else {
						age_string = String.format(" emp_age = '%s'", emp_age);
						read = read + age_string;
						flag = true;
					}
				}

				Statement create_statement = connection.createStatement();
				System.out.println(read);
				result = create_statement.executeQuery(read);
				connection.close();
				logs.logger.info("Search was successfull");
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
				logs.logger.warning("Employee search failed");
			}
			return result;
		} else {
			JOptionPane.showMessageDialog(null, "Connect to the databse first");
		}
		return null;

	}

	/**
	 * Returns the Complete Department Table.
	 * 
	 * @return ResultSet Object
	 */

	public ResultSet complete_dept_table() {
		if (init()) {
			String read = "SELECT * FROM dept_db";

			try {
				Statement create_statement = connection.createStatement();
				ResultSet result = create_statement.executeQuery(read);
				connection.close();
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;

	}

	/**
	 * Deletes the row containing employeeID from the database.
	 * 
	 * @param employeeID Row containing this Employee ID will be deleted
	 * @return Returns true if deletion is successful else returns false
	 */

	public boolean delete_emp(int employeeID) {
		if(init()) {
			String sql = String.format("DELETE  FROM emp_db WHERE emp_id = '%d'", employeeID);
			String sql2 = String.format("DELETE  FROM emp_dept WHERE emp_id = '%d'", employeeID);
			try {
				Statement create_statement = connection.createStatement();
				int rows = create_statement.executeUpdate(sql);
				@SuppressWarnings("unused")
				int rows2 = create_statement.executeUpdate(sql2);
				if (rows > 0) {
					connection.close();
					logs.logger.info(String.format("Employee no : '%d' was sucessfully deleted", employeeID));
					return true;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logs.logger.warning("Deletion operation failed.");
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Returns all Employee IDs of the given departmentID.
	 * 
	 * @param departmentID This departmentID will be searched
	 * @return ResultSet Object
	 */

	public ResultSet empIdUsingDeptid(int departmentID) {
		if (init()) {
			ResultSet result = null;
			String read = String.format("SELECT emp_id FROM emp_dept WHERE dept_id = '%d'", departmentID);
			try {
				Statement create_statement = connection.createStatement();
				result = create_statement.executeQuery(read);
				connection.close();
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}

	/**
	 * Deletes the row from the database with matching departmentID.
	 * 
	 * @param departmentID Row with this Department ID will be deleted
	 * @return Returns true if departmentID deleted successfully or else returns
	 *         false
	 */

	public boolean delete_dept(int departmentID) {
		String sql = String.format("DELETE  FROM dept_db WHERE dept_id = '%d'", departmentID);
		String sql2 = String.format("DELETE  FROM emp_dept WHERE dept_id = '%d'", departmentID);
		if (init()) {
			try {
				Statement create_statement = connection.createStatement();
				int rows = create_statement.executeUpdate(sql);
				@SuppressWarnings("unused")
				int rows2 = create_statement.executeUpdate(sql2);
				if (rows > 0) {
					connection.close();
					logs.logger.info(String.format("Department no : '%d' was deleted successfull", departmentID));
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logs.logger.warning("Deletion Operation Failed");
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * Returns a ResultSet Object with matching table row content. Content can be
	 * searched using either departmentID or departmentName
	 * 
	 * @param departmentID   This Department ID will be searched
	 * @param departmentName This Department Name will be searched
	 * @return ResultSet Object
	 */

	public ResultSet searchDept(String departmentID, String departmentName) {
		String id_string;
		String fname_string_and;
		String fname_string;
		boolean flag = false;

		String read = String.format("select * from dept_db where");
		if (!departmentID.isEmpty()) {
			id_string = String.format(" dept_id ='%s'", departmentID);
			read = read + id_string;
			flag = true;
		}
		if (!departmentName.isEmpty()) {
			if (flag) {
				fname_string_and = String.format(" AND dept_name = '%s'", departmentName);
				read = read + fname_string_and;
				flag = true;
			} else {
				fname_string = String.format(" dept_name = '%s'", departmentName);
				read = read + fname_string;
				flag = true;
			}
		}
		try {
			Statement create_statement = connection.createStatement();
			result = create_statement.executeQuery(read);
			connection.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
