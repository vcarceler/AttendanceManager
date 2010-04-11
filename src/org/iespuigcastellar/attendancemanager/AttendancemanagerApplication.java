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

import java.util.Locale;
import java.util.ResourceBundle;

import org.iespuigcastellar.attendancemanager.core.DatabaseStorage;
import org.iespuigcastellar.attendancemanager.core.Teacher;
import org.iespuigcastellar.attendancemanager.screenlayouts.LoginLayout;

import com.vaadin.Application;
import com.vaadin.ui.*;

@SuppressWarnings("serial")
public class AttendancemanagerApplication extends Application {
	
	public DatabaseStorage storage = new DatabaseStorage();
	public Teacher user = null;
	public Window editTeachersWindow;
	public ResourceBundle locale = ResourceBundle.getBundle("AttendanceManagerLocalization", new Locale("es"));
	
	@Override
	public void init() {
		Window mainWindow = new Window(locale.getString("MAINWINDOW_TITLE"));

		LoginLayout loginLayout = new LoginLayout();
		
		mainWindow.addComponent(loginLayout);
		setMainWindow(mainWindow);
	}
	
	// Mute the uncaught Exception in Component Error Indicator
	// see http://vaadin.com/book/-/page/application.errors.html#figure.application.errors.unchecked-exceptions
	//
	// COMENTAR DURANTE EL DESARROLLO !!!
//	public void terminalError(Terminal.ErrorEvent event) {
//		
//	}
	
}
