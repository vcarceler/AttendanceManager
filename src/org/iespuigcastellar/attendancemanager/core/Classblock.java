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

public class Classblock implements HasGetName {
	private int id;
	private int idsubject;
	private int idgroup;
	private int day_of_week;
	private Date start;
	private Date end;
	private String name; // It's a name composed by time + group.name + " / " + subject.name
	
	public Classblock(int id, int idsubject, int idgroup, int day_of_week, Date start, Date end) {
		this.id = id;
		this.idsubject = idsubject;
		this.idgroup = idgroup;
		this.day_of_week = day_of_week;
		this.start = start;
		this.end = end;
	}
	
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public int getIDSubject() {
		return idsubject;
	}
	public void setIDSubject(int idsubject) {
		this.idsubject = idsubject;
	}
	public int getDay_of_week() {
		return day_of_week;
	}
	public void setDay_of_week(int dayOfWeek) {
		day_of_week = dayOfWeek;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}

	public void setIDGroup(int idgroup) {
		this.idgroup = idgroup;
	}

	public int getIDGroup() {
		return idgroup;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
