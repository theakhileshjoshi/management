package management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class test {

	static Connection connection;// creating connection object
	public String jdbcURL = "jdbc:postgresql://localhost:5432/emp_dept_management";
	public String username, password;

	public boolean init(String _username, String _password) {
		// String create_db = "CREATE DATABASE sample";
		username = _username;
		password = _password;

		try {
			// Connecting to the database using jdbc driver.
			connection = DriverManager.getConnection(jdbcURL, username, password);
			// statement.execute(create_db);
			return true;// returning true after connection is successful

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean enter_emp_data(String fname, String lname, int age) {
		// This function adds a row inside the employ database.
		if (init(username, password)) {
			String step1 = "INSERT INTO emp_db(emp_fname,emp_lname,emp_age)" + "VALUES(?,?,?)";

			try {
				PreparedStatement statement = connection.prepareStatement(step1);
				statement.setString(1, fname);
				statement.setString(2, lname);
				statement.setInt(3, age);

				int rows = statement.executeUpdate();
				if (rows > 0) {
					return true;
				}

			} catch (SQLException e) {

				e.printStackTrace();

			}
		} else {
			return false;
		}
		return false;
	}

	public boolean enter_dept_data(String dept_name) {
		if (init(username, password)) {
			// String format of the postgresql statement to be executed.
			String sql = "INSERT INTO dept_db(dept_name)" + "VALUES(?)";

			// Creating a prepared statement for the data to be entered.
			PreparedStatement statement;
			try {
				statement = connection.prepareStatement(sql);
				statement.setString(1, dept_name);// Sets dept_name as the first value
				int rows = statement.executeUpdate();
				if (rows > 0) {
					return true;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

	public ResultSet searchEmpUsingID(int emp_id) {
		ResultSet something = null;
		if (init(username, password)) {
			try {
				String read = String.format("select * from emp_db where emp_id = '%d'", emp_id);
				Statement create_statement = connection.createStatement();

				ResultSet result = create_statement.executeQuery(read);
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			return something;
		}
		return something;

	}

	public ResultSet searchDeptUsingID(int dept_id) {
		ResultSet something = null;
		if (init(username, password)) {
			try {
				String read = String.format("select * from dept_db where dept_id = '%d'", dept_id);
				Statement create_statement = connection.createStatement();

				ResultSet result = create_statement.executeQuery(read);
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			return something;
		}
		return something;
	}

	public boolean addEmpToDept(int emp_id, int dept_id) {
		if (init(username, password)) {
			String step1 = "INSERT INTO emp_dept(emp_id,dept_id)" + "VALUES(?,?)";

			try {
				PreparedStatement statement = connection.prepareStatement(step1);
				statement.setInt(1, emp_id);
				statement.setInt(2, dept_id);

				int rows = statement.executeUpdate();
				if (rows > 0) {
					return true;
				}

			} catch (SQLException e) {

				e.printStackTrace();

			}
		} else {
			return false;
		}
		return false;
	}

	public boolean changeEmpDept(int emp_id, int olddept_id, int newdept_id) {
		if (init(username, password)) {
			String sql1 = String.format("UPDATE emp_dept SET dept_id = '%d' Where emp_id = '%d' AND dept_id = '%d'",
					newdept_id, emp_id, olddept_id);

			try {
				Statement statement = connection.createStatement();

				int rows = statement.executeUpdate(sql1);
				if (rows > 0) {
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

}
