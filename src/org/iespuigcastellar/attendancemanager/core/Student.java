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

public class Student implements HasGetName {
	private int id;
	private String surname1;
	private String surname2;
	private String name;
	private String email;
	
	public Student(int id, String surname1, String surname2, String name, String email) {
		this.id = id;
		setSurname1(surname1);
		setSurname2(surname2);
		setName(name);
		setEmail(email);
	}
	
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public String getSurname1() {
		return surname1;
	}
	public void setSurname1(String surname1) {
		this.surname1 = Constants.shortString(surname1);
	}
	public String getSurname2() {
		return surname2;
	}
	public void setSurname2(String surname2) {
		this.surname2 = Constants.shortString(surname2);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = Constants.shortString(name);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = Constants.shortString(email);
	}
	
	@Override
	public String toString() {
		return "Student [email=" + email + ", id=" + id + ", name=" + name
				+ ", surname1=" + surname1 + ", surname2=" + surname2 + "]";
	}
	
	
}
