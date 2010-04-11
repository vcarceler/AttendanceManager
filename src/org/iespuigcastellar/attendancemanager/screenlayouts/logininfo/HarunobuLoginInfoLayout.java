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

package org.iespuigcastellar.attendancemanager.screenlayouts.logininfo;

import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;

public class HarunobuLoginInfoLayout extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7894909302909300416L;
	
	public void attach() {
		Embedded item = new Embedded("", new ClassResource("screenlayouts/logininfo/harunobu.jpg", getApplication())); 
		addComponent(item);
		setComponentAlignment(item, Alignment.MIDDLE_CENTER);
	}

}
