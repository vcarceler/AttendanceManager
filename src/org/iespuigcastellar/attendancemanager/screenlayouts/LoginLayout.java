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

package org.iespuigcastellar.attendancemanager.screenlayouts;

import org.iespuigcastellar.attendancemanager.AttendancemanagerApplication;
import org.iespuigcastellar.attendancemanager.core.Logger;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
/** Presents login to the application*/
public class LoginLayout extends VerticalLayout {
	
	private final LoginInfo loginInfo = new LoginInfo();
	private final Form loginForm = new Form();
	private AttendancemanagerApplication app;
	
	public class LoginInfo {
		String login = "";
		String password = "";
		
		public String getLogin() {
			return login;
		}
		public void setLogin(String login) {
			this.login = login;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
	
	public LoginLayout() {
	}
	
	public void attach() {	
		app = (AttendancemanagerApplication)getApplication();
		
		loginForm.setDescription(app.locale.getString("LOGINLAYOUT_LOGINFORM_DESCRIPTION"));
		
		BeanItem<LoginInfo> loginBean = new BeanItem<LoginInfo>(loginInfo);
		loginForm.setItemDataSource(loginBean);
		
		loginForm.getField("login").setRequired(true);
		loginForm.getField("login").setCaption(app.locale.getString("LOGINLAYOUT_LOGINFORM_LOGINCAPTION"));
		loginForm.getField("login").setRequiredError(app.locale.getString("LOGINLAYOUT_LOGINFORM_LOGINREQUIREDERROR"));
		//loginForm.getField("login").setWidth("15em");
		TextField passwordTextField = (TextField)loginForm.getField("password");
		passwordTextField.setSecret(true);
		loginForm.getField("password").setRequired(true);
		loginForm.getField("password").setCaption(app.locale.getString("LOGINLAYOUT_LOGINFORM_PASSWORDCAPTION"));
		loginForm.getField("password").setRequiredError(app.locale.getString("LOGINLAYOUT_LOGINFORM_PASSWORDREQUIREDERROR"));
		loginForm.getField("password").addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// Password text field value change
				commit();
				System.out.println("event.hashCode() " + event.hashCode());
				System.out.println();
			}
		});
		loginForm.setImmediate(true);
		
		Button okButton = new Button(app.locale.getString("LOGINLAYOUT_LOGINFORM_OKBUTTON"), this, "commit");
		okButton.setIcon(new ThemeResource("../runo/icons/16/ok.png"));
		loginForm.getFooter().addComponent(okButton);
		
		Panel loginPanel = new Panel(app.locale.getString("LOGINLAYOUT_LOGINPANEL_CAPTION"));
		loginPanel.addComponent(loginForm);
		
		Panel infoPanel = new Panel(app.locale.getString("LOGINLAYOUT_INFOPANEL_CAPTION"));
		infoPanel.addComponent(new LoginInfoLayout());
		
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setWidth("100%");
		loginPanel.setWidth("25em");
		hLayout.addComponent(loginPanel);
		hLayout.addComponent(infoPanel);
		hLayout.setExpandRatio(infoPanel, 1.0f);
		hLayout.setSpacing(true);
		
		addComponent(hLayout);

		
		loginForm.getField("login").focus();
	}
	
	public void commit() {
		//if(loginForm.isModified()) loginForm.commit();
		//loginForm.commit();
		
		app.user = app.storage.loginUser(loginInfo.login, loginInfo.password);
		if(app.user != null) {
			Logger.log("LoginLayout() User " + app.user.getLogin() + " opens session");
			Window window = getWindow();
			
			if(window != null) window.setContent(new TeacherMainLayout());
			
//			if(app.user.getAdmin()) {
//				System.out.println("Admin login: " + app.user.getLogin() + " " + new java.util.Date());
//				app.editTeachersWindow = new Window("Edit Teachers", new EditTeachersLayout());
//				app.editTeachersWindow.setName("editteachers");
//				app.addWindow(app.editTeachersWindow);
//				
//				Window otra = new Window("Edit teachers", new NewEditTeachersLayout());
//				otra.setName("otra");
//				app.addWindow(otra);
//			}
		} else {
			getWindow().showNotification(app.locale.getString("LOGINLAYOUT_ERRORLOGIN_NOTIFICATION"));
			Logger.log("LoginLayout() User " + loginInfo.login + " fails to open session");
		}
	}

}
