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

import java.util.Collection;
import java.util.Iterator;

import org.iespuigcastellar.attendancemanager.AttendancemanagerApplication;
import org.iespuigcastellar.attendancemanager.core.Teacher;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class EditTeachersLayout extends SplitPanel {
	private AttendancemanagerApplication app;
	
	private Table teachersTable = new Table();
	private Form teacherForm = new Form();
	private TextField loginTextField = new TextField("Login");
	private TextField passwordTextField = new TextField("Password");
	private TextField commentTextField = new TextField("Comment:");
	private CheckBox adminCheckBox = new CheckBox("It's admin?");
	private Button addupdateButton = new Button("commit");
	private HorizontalLayout tablecontrols = new HorizontalLayout();
	private Button tableremoveButton;
	private IndexedContainer teachersData;
	
	private static String visibleCols[] = new String[] {"id", "login", "comment"};
	private static String fields[] = new String[] {"id", "login", "password", "comment", "admin"};

	public EditTeachersLayout() {
		super(SplitPanel.ORIENTATION_HORIZONTAL);
		initLayout();
		initAddRemoveButtons();

	}
	
	public void attach() {
		app = (AttendancemanagerApplication)getApplication();
		
		loadData();
		initTeachersTable();
		initFilteringControls();
	}
	
	private void initLayout() {
		VerticalLayout left = new VerticalLayout();
		left.setSizeFull();
		left.addComponent(teachersTable);
		teachersTable.setSizeFull();
		left.setExpandRatio(teachersTable, 1);
		tablecontrols.setWidth("100%");		
		left.addComponent(tablecontrols);
		this.addComponent(left);
		
		VerticalLayout right = new VerticalLayout();
		right.setSizeFull();
		Panel formPanel = new Panel("Form:");
		formPanel.setSizeFull();
		formPanel.addComponent(teacherForm);
		formPanel.addComponent(addupdateButton);
		right.addComponent(formPanel);
				
		teacherForm.setSizeFull();
		teacherForm.getLayout().setMargin(true);
		teacherForm.setImmediate(true);
		
		teacherForm.getLayout().addComponent(loginTextField);
		teacherForm.getLayout().addComponent(passwordTextField);
		teacherForm.getLayout().addComponent(commentTextField);
		teacherForm.getLayout().addComponent(adminCheckBox);
		//teacherForm.setEnabled(false);
		
		this.addComponent(right);	
	}
	
	private void initAddRemoveButtons() {
		tablecontrols.addComponent(new Button("+", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Object id = teachersTable.addItem();
				teachersTable.setValue(id);
			}
		}));
		
		tableremoveButton = new Button("-", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				app.storage.deleteTeacher((Integer)teachersTable.getValue());
				teachersTable.removeItem(teachersTable.getValue());
				teachersTable.select(null);
			}
		});
		
		tableremoveButton.setVisible(false);
		tablecontrols.addComponent(tableremoveButton);
	}
	
	private String[] initTeachersTable() {
		teachersTable.setContainerDataSource(teachersData);
		teachersTable.setVisibleColumns(visibleCols);
		teachersTable.setSelectable(true);
		teachersTable.setImmediate(true);
		teachersTable.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Object id = teachersTable.getValue();
				teacherForm.setItemDataSource(id == null ? null : teachersTable.getItem(id));
				tableremoveButton.setVisible(id != null);
			}
		});
		
		return visibleCols;
	}
	
	private void initFilteringControls() {
        for (final String pn : visibleCols) {
            final TextField sf = new TextField();
            tablecontrols.addComponent(sf);
            sf.setWidth("100%");
            sf.setInputPrompt(pn);
            sf.setImmediate(true);
            tablecontrols.setExpandRatio(sf, 1);
            sf.addListener(new Property.ValueChangeListener() {
                public void valueChange(ValueChangeEvent event) {
                    teachersData.removeContainerFilters(pn);
                    if (sf.toString().length() > 0 && !pn.equals(sf.toString())) {
                        teachersData.addContainerFilter(pn, sf.toString(),
                                true, false);
                    }
                    //getMainWindow().showNotification(
                    //        "" + addressBookData.size() + " matches found");
                }
            });
        }
    }
	
	private void loadData() {
		teachersData = new IndexedContainer();
		
		for(String p : fields) {
			teachersData.addContainerProperty(p, String.class, "");
		}
		
		Collection<Teacher> teachers = app.storage.allTeachers();
		for(Iterator<Teacher> i = teachers.iterator(); i.hasNext(); ) {
			Teacher t = i.next();
			Object id = teachersData.addItem();
			teachersData.getContainerProperty(id, "id").setValue(t.getID());
			teachersData.getContainerProperty(id, "login").setValue(t.getLogin());
			teachersData.getContainerProperty(id, "password").setValue(t.getPassword());
			teachersData.getContainerProperty(id, "comment").setValue(t.getComment());
			teachersData.getContainerProperty(id, "admin").setValue(t.getAdmin());
		}
	}
	
}
