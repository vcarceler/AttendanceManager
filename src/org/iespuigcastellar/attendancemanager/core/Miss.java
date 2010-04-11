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

import java.util.Date;

public class Miss {
	public final static int MISS 			= 1;
	public final static int EXCUSED_MISS 	= 2;
	public final static int DELAY 			= 3;
	public final static int EXPULSION 		= 4;
	
	private int id;
	private int type;
	private Date date;
	private String comment;
	private int idstudent;
	private int idteacher;
	private int idsubject;
	private int idclassblock;
	
	public Miss(int id, int type, Date date, String comment, int idstudent, int idteacher, int idsubject, int idclassblock) {
		this.id = id;
		this.type = type;
		this.date = date;
		this.comment = Constants.longString(comment);
		this.idstudent = idstudent;
		this.idsubject = idsubject;
		this.idteacher = idteacher;
		this.idclassblock = idclassblock;
	}
	
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = Constants.shortString(comment);
	}
	public int getIDStudent() {
		return idstudent;
	}
	public void setIDStudent(int idstudent) {
		this.idstudent = idstudent;
	}
	public int getIDTeacher() {
		return idteacher;
	}
	public void setIDTeacher(int idteacher) {
		this.idteacher = idteacher;
	}
	public int getIDSubject() {
		return idsubject;
	}
	public void setIDSubject(int idsubject) {
		this.idsubject = idsubject;
	}

	public int getIDClassblock() {
		return idclassblock;
	}

	public void setIDClassblock(int idclassblock) {
		this.idclassblock = idclassblock;
	}
}
