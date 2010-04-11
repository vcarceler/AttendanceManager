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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.iespuigcastellar.attendancemanager.AttendancemanagerApplication;
import org.iespuigcastellar.attendancemanager.core.Constants;
import org.iespuigcastellar.attendancemanager.core.Teacher;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class NewEditTeachersLayout extends SplitPanel {

	private static final long serialVersionUID = 9039832374876681347L;
	
	private AttendancemanagerApplication app;

	private Table teachersTable = new Table();
	private Form teacherForm = new Form();
	private HorizontalLayout filterHorizontalLayout = new HorizontalLayout();
	private Button newButton;
	private Button deleteButton;
	private Button saveButton;
	private Button updateButton;
	
	private IndexedContainer teachersData;
	
	private static String visibleCols[] = new String[] {"id", "login", "comment"};
	//private static String fields[] = new String[] {"id", "login", "password", "comment", "admin"};

//	private Teacher teacher;
//	private BeanItem<Teacher> teacherItem;
	
	
	public NewEditTeachersLayout() {
		super(SplitPanel.ORIENTATION_HORIZONTAL);
		initLayout();
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
		filterHorizontalLayout.setWidth("100%");		
		left.addComponent(filterHorizontalLayout);
		this.addComponent(left);
		
		VerticalLayout right = new VerticalLayout();
		
		HorizontalLayout controlsHorizontalLayout = new HorizontalLayout();
		
		newButton = new Button("New", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Nuevo profesor !!!");
				
				Object id = teachersTable.addItem();
				teachersTable.setValue(id);
				teachersData.getContainerProperty(id, "id").setValue(0);
				teachersData.getContainerProperty(id, "login").setValue("");
				teachersData.getContainerProperty(id, "password").setValue("");
				teachersData.getContainerProperty(id, "comment").setValue("");
				teachersData.getContainerProperty(id, "admin").setValue(new Boolean(false)); // <- REVISAR !!!
				
				deleteButton.setEnabled(false);
				saveButton.setVisible(true);
				updateButton.setVisible(false);
			}
		});
		controlsHorizontalLayout.addComponent(newButton);
		
		deleteButton = new Button("Delete", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("Delete teacher " + teachersTable.getValue() + " on " + new java.util.Date());
				app.storage.deleteTeacher((Integer)teachersTable.getValue());
				teachersTable.removeItem(teachersTable.getValue());
				teachersTable.select(null);
				
				newButton.setEnabled(true);
				deleteButton.setEnabled(false);
			}
		});
		
		deleteButton.setEnabled(false);
		
		controlsHorizontalLayout.addComponent(deleteButton);
		controlsHorizontalLayout.setSpacing(true);
		controlsHorizontalLayout.setMargin(true);
		
		right.addComponent(new Panel("Controls", controlsHorizontalLayout));
	
		saveButton = new Button("save", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("Save Button !!!");
				deleteButton.setEnabled(false);
				teacherForm.commit();
				
				Teacher t = new Teacher(Integer.valueOf((String)teacherForm.getField("id").getValue()), (String)teacherForm.getField("login").getValue(), (String)teacherForm.getField("password").getValue(), (String)teacherForm.getField("comment").getValue(), (Boolean)teacherForm.getField("admin").getValue());
				int id = app.storage.addTeacher(t);
				
				//teachersTable.removeAllItems();
				//loadData();
				
				//teacherForm.getField("id").setValue(id);
				
				System.out.println(t);
				teachersTable.select(null);
				saveButton.setVisible(false);
			}
		});
		
		saveButton.setVisible(false);
		
		updateButton = new Button("update", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("Update Button ");
				Teacher t = new Teacher(Integer.valueOf((String)teacherForm.getField("id").getValue()), (String)teacherForm.getField("login").getValue(), (String)teacherForm.getField("password").getValue(), (String)teacherForm.getField("comment").getValue(), (Boolean)teacherForm.getField("admin").getValue());
				app.storage.updateTeacher(t);
				teacherForm.commit();
			}
		});
		
		updateButton.setVisible(false);
		
		teacherForm.getLayout().addComponent(saveButton);
		teacherForm.getLayout().addComponent(updateButton);
		teacherForm.setSizeFull();
		teacherForm.getLayout().setMargin(true);
		teacherForm.setImmediate(true);
		
		//teacherForm.setEnabled(false);
		
//		teacher = new Teacher(0, "", "", "", false);
//		teacherItem = new BeanItem<Teacher>(teacher);
		teacherForm.setCaption("Teacher details");
		teacherForm.setWriteThrough(false);
		teacherForm.setInvalidCommitted(false);
		
		teacherForm.setFormFieldFactory(new TeacherFieldFactory());
//		teacherForm.setData(teacherItem);
		
		teacherForm.setVisibleItemProperties(Arrays.asList(new String[] {"id", "login", "password", "comment", "admin"}));
		
		right.addComponent(teacherForm);
		this.addComponent(right);
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
				//newButton.setEnabled(false);
				deleteButton.setEnabled(true);
				saveButton.setVisible(false);
				updateButton.setVisible(true);
				//tableremoveButton.setVisible(id != null);
				//deleteButton.setEnabled(id != null);
			}
		});
		
		return visibleCols;
	}
	
	private void initFilteringControls() {
        for (final String pn : visibleCols) {
            final TextField sf = new TextField();
            filterHorizontalLayout.addComponent(sf);
            sf.setWidth("100%");
            sf.setInputPrompt(pn);
            sf.setImmediate(true);
            filterHorizontalLayout.setExpandRatio(sf, 1);
            sf.addListener(new Property.ValueChangeListener() {
                public void valueChange(ValueChangeEvent event) {
                    teachersData.removeContainerFilters(pn);
                    if (sf.toString().length() > 0 && !pn.equals(sf.toString())) {
                        teachersData.addContainerFilter(pn, sf.toString(),
                                true, false);
                    }
                    //app.getMainWindow().showNotification(
                    //        "" + teachersData.size() + " matches found");
                }
            });
        }
    }
	
	private void loadData() {
		teachersData = new IndexedContainer();
		
//		for(String p : fields) {
//			teachersData.addContainerProperty(p, String.class, "");
//		}
		teachersData.addContainerProperty("id", Integer.class, "");
		teachersData.addContainerProperty("login", String.class, "");
		teachersData.addContainerProperty("password", String.class, "");
		teachersData.addContainerProperty("comment", String.class, "");
		teachersData.addContainerProperty("admin", Boolean.class, "");
		
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
	
	private class TeacherFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 4978129019256808033L;
		
		private final CheckBox admin = new CheckBox("admin");

		public Field createField(Item item, Object propertyId, Component uiContext) {
            Field f = super.createField(item, propertyId, uiContext);
            
            System.out.println("TeacherFieldFactory.createField() " + propertyId);
            
            if("id".equals(propertyId)) {
            	TextField tf = (TextField) f;
            	tf.setReadOnly(true);
            } else if ("login".equals(propertyId)) {
            	TextField tf = (TextField) f;
            	tf.setRequired(true);
            	tf.setRequiredError("Please insert a login");
            	tf.addValidator(new StringLengthValidator("login must be 1-" + Constants.SHORT_VARCHAR_LENGTH + " characters", 1, Constants.SHORT_VARCHAR_LENGTH, false));
            	tf.addValidator(new AbstractValidator("there is another teacher with the same login") {
            	
            		public void validate(Object value) throws Validator.InvalidValueException {
            			if (isValid(value) == false) {
            				Validator.InvalidValueException exception = new Validator.InvalidValueException("Another teacher already has the login '" + value + "'");
            				throw exception;
            			}
            		}
            		
            		public boolean isValid(Object value) {
            			System.out.println("AbstractValidator.isValid() " + value );
            			if (app.storage.loginExists(value.toString()))
            				return false;
            			else
            				return true;
            		}
            	});
            } else if ("password".equals(propertyId)) {
            	TextField tf = (TextField) f;
            	tf.setRequired(true);
            	tf.setRequiredError("Please insert a password");
            	tf.addValidator(new StringLengthValidator("password must be 1-" + Constants.SHORT_VARCHAR_LENGTH + " characters", 1, Constants.SHORT_VARCHAR_LENGTH, false));
            } else if ("comment".equals(propertyId)) {
            	TextField tf = (TextField) f;
            	tf.addValidator(new StringLengthValidator("Comment too long", 1, Constants.LONG_VARCHAR_LENGTH, false));
            } else if ("admin".equals(propertyId)) {
            	return admin;
            }
            
            return f;
		}
	}

}
