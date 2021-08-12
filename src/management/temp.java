package management;

import java.sql.ResultSet;
import java.sql.SQLException;

public class temp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		backEnd backend = new backEnd();
		backend.username = "postgres";
		backend.password = "1345";
		backend.createDatabase();
		backend.init();

		ResultSet result = backend.complete_emp_table();
		try {
			while (result.next()) {
				int id = result.getInt("emp_id");
				System.out.println(id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
