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

public class Subject implements HasGetName {
	private int id;
	private int idteacher;
	private String name;
	private String comment;
	
	public Subject(int id, int idteacher, String name, String comment) {
		this.id = id;
		this.idteacher = idteacher;
		setName(name);
		setComment(comment);
	}
	
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public int getIDTeacher() {
		return idteacher;
	}
	public void setIDTeacher(int idteacher) {
		this.idteacher = idteacher;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = Constants.shortString(name);
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = Constants.longString(comment);
	}

	@Override
	public String toString() {
		return "Subject [comment=" + comment + ", id=" + id + ", idteacher="
				+ idteacher + ", name=" + name + "]";
	}
	
	
}
