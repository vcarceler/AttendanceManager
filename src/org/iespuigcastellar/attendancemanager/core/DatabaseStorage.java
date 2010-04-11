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

package org.iespuigcastellar.attendancemanager.core;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Vector;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class DatabaseStorage {

	private Connection con;
	private Statement statement;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	private MessageDigest md;
	private HexBinaryAdapter hbinary = new HexBinaryAdapter();
	
	public DatabaseStorage() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/amdb";
			// Valores por defecto para el desarrollo amuser/secreto
			con = DriverManager.getConnection(url, "amuser", "secreto");
			statement = con.createStatement();
			Logger.log("DatabaseStorage() DB connection established");
			
			md = MessageDigest.getInstance("SHA-1"); 
			
		} catch(Exception e) {
			Logger.log("DatabaseStorage.DatabaseStorage()");
			System.err.println("DatabaseStorage.DatabaseStorage() EXCEPTION !!!");
			System.err.println(e);
		}
	}
	
	/** Closes connection with database. */
	public void close() {
		try {
			con.close();
			Logger.log("DatabaseStorage.close() DB connection closed");
		} catch (SQLException e) {
			System.err.println("DatabaseStorage.close()");
			System.err.println(e);
			e.printStackTrace();
		}
	}

	/** Looks for the login/password pair in Teacher table, returns Teacher object if
	 * it's a real user or null if login fails.*/
	public Teacher loginUser(String login, String password) {
		
		Teacher user = null;
		
		try {
			
			String passwordhash = hbinary.marshal(md.digest(password.getBytes())).toLowerCase();
			
			ResultSet result = statement.executeQuery("SELECT idteacher, comment, admin FROM teacher WHERE teacher.login = '" + login + "' AND teacher.password = '" + passwordhash + "';");
			if(result.next()) {
				user = new Teacher(result.getInt("idteacher"), login, passwordhash, result.getString("comment"), result.getBoolean("admin"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	/** Returns true if there is another teacher with the same login, false otherwise */
	public boolean loginExists(String login) {
		boolean exists = false;
		
		try {
			ResultSet result = statement.executeQuery("SELECT login FROM teacher WHERE teacher.login = '" + login + "';");
			if(result.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return exists;
	}
	
	/** Returns a collection with all the subjects of idteacher */
	public Collection<Subject> subjectsOfTeacher(int idteacher) {
		Vector<Subject> vector = new Vector<Subject>();
		
		try {
			ResultSet result = statement.executeQuery("SELECT idsubject, name, comment FROM subject WHERE subject.teacher_idteacher = "+idteacher+";");
			while(result.next()) {
				vector.add(new Subject(result.getInt("idsubject"), idteacher, result.getString("name"), result.getString("comment")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vector;
	}
	
	/**
	 * Returns a Collection of subjects of a given teacher and group
	 */
	public Collection<Subject> subjectsOfTeacherInGroup(int idteacher, int idgroup) {
		Vector<Subject> vector = new Vector<Subject>();

		try {
			ResultSet result = statement.executeQuery("SELECT idsubject, name, comment FROM subject, group_has_subject WHERE subject.teacher_idteacher = " + idteacher + " AND subject.idsubject = group_has_subject.subject_idsubject AND group_has_subject.group_idgroup = " + idgroup + ";");
			while(result.next()) {
				vector.add(new Subject(result.getInt("idsubject"), idteacher, result.getString("name"), result.getString("comment")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vector;
	}
	
	/** Returns a collection with all the groups of idteacher */
	public Collection<Group> groupsOfTeacher(int idteacher) {
		Vector<Group> vector = new Vector<Group>();
		
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM amdb.group WHERE group.idgroup = ANY (SELECT group_has_subject.group_idgroup FROM group_has_subject WHERE group_has_subject.subject_idsubject = ANY (SELECT subject.idsubject FROM subject WHERE subject.teacher_idteacher = "+idteacher+"));");
			while(result.next()) {
				vector.add(new Group(result.getInt("idgroup"), result.getString("name"), result.getString("comment")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vector;
	}
	
	/** Returns a Collection of students in a given group.*/
	public Collection<Student> studentsOfGroup(int idgroup) {
		Vector<Student> vector = new Vector<Student>();
		
		try {
			ResultSet result = statement.executeQuery("SELECT idstudent, surname1, surname2, name, email FROM student, group_has_student WHERE group_has_student.group_idgroup = " + idgroup + " AND group_has_student.student_idstudent = student.idstudent ORDER BY surname1, surname2, name;");
			while(result.next()) {
				vector.add(new Student(result.getInt("idstudent"), result.getString("surname1"), result.getString("surname2"), result.getString("name"), result.getString("email")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vector;
	}
	
	/** Adds teacher to the database and returns the assigned ID or 0 if there is some problem. 
	 * A teacher gets one ID when stored in database for the first time.*/
	public int addTeacher(Teacher teacher) {
		int id = 0;
		
		try {
			int admin = teacher.getAdmin() ? 1 : 0; 
			statement.execute("INSERT INTO teacher VALUES('0', '" + teacher.getLogin() + "', '" + teacher.getPassword() + "', '" + teacher.getComment() + "', '" + admin + "');");
			ResultSet result = statement.executeQuery("SELECT LAST_INSERT_ID() AS ID;");
			result.next();
			id = result.getInt("ID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/** Adds miss to the database and returns the assigned ID or 0 if there is some problem.
	 * A miss gets one ID when stored in database for the first time.*/
	public int addMiss(Miss miss) {
		int id = 0;
		
		try { 
			statement.execute("INSERT INTO miss VALUES('0', '" + miss.getType() + "', '" + dateFormat.format(miss.getDate()) + "', '" + miss.getComment() + "', '" + miss.getIDStudent() + "', '" + miss.getIDTeacher() + "', '" + miss.getIDSubject() + "', '" + miss.getIDClassblock() + "');");
			ResultSet result = statement.executeQuery("SELECT LAST_INSERT_ID() AS ID;");
			result.next();
			id = result.getInt("ID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/** Update the specified teacher value in database. */
	public void updateTeacher(Teacher teacher) {
		
		try {
			int admin = teacher.getAdmin() ? 1 : 0;
			statement.execute("UPDATE teacher SET login='" + teacher.getLogin() + "', password='" + teacher.getPassword() + "', comment='" + teacher.getComment() + "', admin='" + admin + "' WHERE idteacher=" + teacher.getID() +";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Returns a collection of all teachers in DB*/
	public Collection<Teacher> allTeachers() {
		Vector<Teacher> vector = new Vector<Teacher>();
		
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM teacher;");
			while(result.next()) {
				vector.add(new Teacher(result.getInt("idteacher"), result.getString("login"), result.getString("password"), result.getString("comment"), result.getBoolean("admin")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vector;
	}
	
	/** Deletes the specified teacher */
	public void deleteTeacher(int idteacher) {
		try {
			statement.execute("DELETE FROM teacher WHERE idteacher = " + idteacher + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Deletes the specified miss */
	public void deleteMiss(int type, java.util.Date date, int idstudent, int idclassblock) {
		try {
			statement.execute("DELETE FROM miss WHERE type = " + type + " AND date = '" + dateFormat.format(date) + "' AND student_idstudent = " + idstudent + " AND classblock_idclassblock = " + idclassblock + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Returns a collection of all classblocks for the specified teacher on the specified date*/
	public Collection<Classblock> classblocksOfTeacherOnDate(int idteacher, java.util.Date date) {
		Vector<Classblock> vector = new Vector<Classblock>();
		
		try {
			ResultSet result = statement.executeQuery("SELECT classblock.*,amdb.group.name AS groupname, subject.name AS subjectname FROM classblock,amdb.group,subject WHERE classblock.subject_idsubject = subject.idsubject AND amdb.group.idgroup = classblock.group_idgroup AND classblock.day_of_week = DAYOFWEEK('" + dateFormat.format(date) + "') AND subject_idsubject = ANY (SELECT idsubject FROM subject WHERE teacher_idteacher = " + idteacher + ") ORDER BY start;");
			while(result.next()) {
				Classblock block = new Classblock(result.getInt("idclassblock"), result.getInt("subject_idsubject"), result.getInt("group_idgroup"), result.getInt("day_of_week"), result.getDate("start"), result.getDate("end"));
				block.setName(timeFormat.format(result.getTime("start")) + "-" + timeFormat.format(result.getTime("end")) + " " + result.getString("groupname") + " / " + result.getString("subjectname"));
				vector.add(block);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vector;
	}
	
	/** Returns true if exists a miss for the specified type, date, classblockID and studentID*/
	public boolean existsMiss(int type, java.util.Date date, int classblockid, int studentid) {
		Boolean exists = false;
		
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM miss WHERE type = " + type + " AND date = '" + dateFormat.format(date) + "' AND classblock_idclassblock = " + classblockid + " AND student_idstudent = " + studentid + ";");
			if(result.next()) exists = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return exists;
	}
	
	/** Returns a string of lowercase hexcodes of SHA-1 hash value for the provided password */
	public String getPasswordHash(String password) {
		return hbinary.marshal( md.digest(password.getBytes()) ).toLowerCase();
	}
}
