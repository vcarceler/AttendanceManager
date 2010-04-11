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

public class Teacher {
	private int id;
	private String login;
	private String password;
	private String comment;
	private boolean admin;
	
	public Teacher(int id, String login, String password, String comment, boolean admin) {
		this.id = id;
		setLogin(login);
		setPassword(password);
		setComment(comment);
		this.admin = admin;
	}
	
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = Constants.shortString(login);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = Constants.shortString(password).toLowerCase();
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = Constants.shortString(comment);
	}

	public boolean getAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public String toString() {
		return "Teacher [admin=" + admin + ", comment=" + comment + ", id="
				+ id + ", login=" + login + ", password=" + password + "]";
	}
}
