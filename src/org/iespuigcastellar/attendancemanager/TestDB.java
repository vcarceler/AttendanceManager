/*
    AttendanceManager
    Copyright (C) 2010  Victor Carceler

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.iespuigcastellar.attendancemanager;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;


public class TestDB {
	
	private final String FILE_PROPERTIES = "/home/vcarceler/workspace/AttendanceManager/src/org/iespuigcastellar/attendancemanager/AttendanceManager.properties";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestDB test = new TestDB();
		test.doit();
	}
	
	public void doit() {
		try {
			
			FileInputStream propertiesFile = new FileInputStream(FILE_PROPERTIES);
			Properties properties = new Properties();
			properties.load(propertiesFile);
			propertiesFile.close();
			
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + properties.getProperty("mysql_server") + ":3306/" + properties.getProperty("mysql_database");
			Connection con = DriverManager.getConnection(url, properties.getProperty("mysql_user"), properties.getProperty("mysql_password"));
			
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery("SELECT idteacher, login, password, comment FROM teacher;");
			
			while(result.next()) {
				System.out.println("id: " + result.getInt("idteacher") + " login: " + result.getString("login") + " password: " + result.getString("password"));
			}
			
			
		} catch(Exception e) {
			System.out.println("Ha ocurrido un error !!!");
			System.out.println(e);
		}
	}

}
