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
import java.util.Date;
import java.util.Iterator;

import org.iespuigcastellar.attendancemanager.AttendancemanagerApplication;
import org.iespuigcastellar.attendancemanager.core.Classblock;
import org.iespuigcastellar.attendancemanager.core.HasGetName;
import org.iespuigcastellar.attendancemanager.core.Logger;
import org.iespuigcastellar.attendancemanager.core.Miss;
import org.iespuigcastellar.attendancemanager.core.Student;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
public class TeacherMainLayout extends VerticalLayout implements Property.ValueChangeListener {

	private AttendancemanagerApplication app;
	private PasswordChangeWindow passwordChangeWindow;
	private PopupDateField datePopupDateField;
	private ComboBox classblockComboBox;
	private Table table = new Table();
	
	public void attach() {
		
		app = (AttendancemanagerApplication)getApplication();
		
		initLayout();
		
		Collection<Classblock> classblocks = app.storage.classblocksOfTeacherOnDate(app.user.getID(), new Date());
		setComboBoxItems(classblockComboBox, classblocks);
	}
	
	private void initLayout() {
		
		passwordChangeWindow = new PasswordChangeWindow();
		passwordChangeWindow.setCaption(app.locale.getString("TEACHERMAINLAYOUT_CHANGEMYPASSWORD_BUTTON"));
		
		datePopupDateField = new PopupDateField("");
		datePopupDateField.setDescription(app.locale.getString("TEACHERMAINLAYOUT_DATEFIELD_DESCRIPTION"));
		datePopupDateField.setValue(new java.util.Date());
		datePopupDateField.setResolution(PopupDateField.RESOLUTION_DAY);
		datePopupDateField.setImmediate(true);
		datePopupDateField.addListener(ValueChangeEvent.class, this, "changedDate");
		
		classblockComboBox = new ComboBox();
		classblockComboBox.setInputPrompt(app.locale.getString("TEACHERMAINLAYOUT_CLASSBLOCK_INPUTPROMPT"));
		classblockComboBox.setDescription(app.locale.getString("TEACHERMAINLAYOUT_CLASSBLOCK_DESCRIPTION"));
		classblockComboBox.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
		classblockComboBox.setImmediate(true);
		classblockComboBox.addContainerProperty("name", String.class, "");
		classblockComboBox.setItemCaptionPropertyId("name");
		classblockComboBox.addListener(ValueChangeEvent.class, this, "changedClassblock");
				
		Button logoutButton = new Button(app.locale.getString("TEACHERMAINLAYOUT_LOGOUTBUTTON_CAPTION"));
		
		logoutButton.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Logger.log("User " + app.user.getLogin() + " closes session");
				app.storage.close();
				getApplication().close();
			}
		});
		
		Button changePasswordButton = new Button(app.locale.getString("TEACHERMAINLAYOUT_CHANGEMYPASSWORD_BUTTON"));
		changePasswordButton.setIcon(new ThemeResource("../runo/icons/16/user.png"));
		changePasswordButton.setStyleName(Button.STYLE_LINK);
		changePasswordButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				// Open window if not open already
				if(passwordChangeWindow.getParent() != null) {
					// Window already open
				} else {
					// open window
					getWindow().addWindow(passwordChangeWindow);
				}
			}
		});
		
		GridLayout optionsGridLayout = new GridLayout(2,1);
		HorizontalLayout haLayout = new HorizontalLayout();
		
		haLayout.setSpacing(true);
		
		haLayout.addComponent(changePasswordButton);
		haLayout.setComponentAlignment(changePasswordButton, Alignment.MIDDLE_LEFT);
		haLayout.addComponent(datePopupDateField);
		haLayout.addComponent(classblockComboBox);
		
		optionsGridLayout.addComponent(haLayout);
		optionsGridLayout.addComponent(logoutButton);
		optionsGridLayout.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);
		optionsGridLayout.setWidth("100%");

		addComponent(optionsGridLayout);

		table.setSizeFull();
		table.setImmediate(true);
		table.setColumnReorderingAllowed(true);
	    table.setColumnCollapsingAllowed(true);
	    table.addContainerProperty("Name", String.class, null);
	    table.addContainerProperty("Surname 1", String.class, null);
	    table.addContainerProperty("Surname 2", String.class, null);
	    table.addContainerProperty("Miss", CheckBox.class, null);
	    table.addContainerProperty("Excused", CheckBox.class, null);
	    table.addContainerProperty("Delay", CheckBox.class, null);
	    table.addContainerProperty("Expulsion", CheckBox.class, null);
	    
	    table.setColumnExpandRatio("Name", 1);
	    table.setColumnExpandRatio("Surname 1", 1);
	    table.setColumnExpandRatio("Surname 2", 1);

	    table.setColumnHeaders(new String[] {  app.locale.getString("TEACHERMAINLAYOUT_TABLECOLUMN_NAME"), app.locale.getString("TEACHERMAINLAYOUT_TABLECOLUMN_SURNAME1"), app.locale.getString("TEACHERMAINLAYOUT_TABLECOLUMN_SURNAME2"), app.locale.getString("TEACHERMAINLAYOUT_TABLECOLUMN_MISS"), app.locale.getString("TEACHERMAINLAYOUT_TABLECOLUMN_EXCUSED"), app.locale.getString("TEACHERMAINLAYOUT_TABLECOLUMN_DELAY"), app.locale.getString("TEACHERMAINLAYOUT_TABLECOLUMN_EXPULSION") });
		addComponent(table);
		setExpandRatio(table, 1);
		setSizeFull();
	}
	
	/** Clears ComboBox's Items and sets the ones in collection.  */
	private void setComboBoxItems(ComboBox combo, Collection<?> collection) {
		combo.removeAllItems();
		for(Iterator<?> it = collection.iterator(); it.hasNext(); ) {
			HasGetName element = (HasGetName) it.next();
			Item item = combo.addItem(element);
			item.getItemProperty("name").setValue(element.getName());
		}
		// If there is only one Item select it
		if(collection.size()==1) {
			combo.select(collection.iterator().next());
		}
	}

    public void valueChange(ValueChangeEvent event) {
        getWindow().showNotification("Subject: " + event.getProperty());
    }
    
    public void changedClassblock(ValueChangeEvent event) {
    	
    	Classblock classblock = (Classblock)event.getProperty().getValue();
    	if(classblock != null) {
    		loadTable(classblock.getIDGroup());
    	} else {
    		table.removeAllItems();
    		getWindow().showNotification(app.locale.getString("TEACHERMAINLAYOUT_SELECTCLASSBLOCKNOTIFICATION_CAPTION"), "<br>"+app.locale.getString("TEACHERMAINLAYOUT_SELECTCLASSBLOCKNOTIFICATION_DESCRIPTION"), Notification.TYPE_HUMANIZED_MESSAGE);
    	}
    }
    
    public void changedDate(ValueChangeEvent event) {
    	table.removeAllItems();
    	setComboBoxItems(classblockComboBox, app.storage.classblocksOfTeacherOnDate(app.user.getID(), (Date)event.getProperty().getValue()));
    }
    
    /** Loads students data into table. */
    public void loadTable(int idgroup) {
    	table.removeAllItems();
    	Collection<Student> collection = app.storage.studentsOfGroup(idgroup);
    	for(Iterator<Student> it = collection.iterator(); it.hasNext(); ) {
			Student student = it.next();
			
			CheckBox missCheckBox = new CheckBox();
			missCheckBox.setData(student);
			if(app.storage.existsMiss(Miss.MISS, (Date)datePopupDateField.getValue(), ((Classblock)classblockComboBox.getValue()).getID(), student.getID())) {
				missCheckBox.setValue(true);
			}
			missCheckBox.setImmediate(true);
			missCheckBox.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					Student student = (Student)event.getButton().getData();
					
					Miss miss = new Miss(0, Miss.MISS, (Date)datePopupDateField.getValue(), "", student.getID(), app.user.getID(), ((Classblock)classblockComboBox.getValue()).getIDSubject(), ((Classblock)classblockComboBox.getValue()).getID());

					if((Boolean)event.getButton().getValue()) {
						app.storage.addMiss(miss);
					} else {
						// Delete Miss
						app.storage.deleteMiss(Miss.MISS, miss.getDate(), miss.getIDStudent(), miss.getIDClassblock());
					}
					updateEnabledCheckBoxes(student);
				}
			});
			
			CheckBox excusedmissCheckBox = new CheckBox();
			excusedmissCheckBox.setData(student);
			if(app.storage.existsMiss(Miss.EXCUSED_MISS, (Date)datePopupDateField.getValue(), ((Classblock)classblockComboBox.getValue()).getID(), student.getID())) {
				excusedmissCheckBox.setValue(true);
			}
			excusedmissCheckBox.setImmediate(true);
			excusedmissCheckBox.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					Student student = (Student)event.getButton().getData();
					
					Miss miss = new Miss(0, Miss.EXCUSED_MISS, (Date)datePopupDateField.getValue(), "", student.getID(), app.user.getID(), ((Classblock)classblockComboBox.getValue()).getIDSubject(), ((Classblock)classblockComboBox.getValue()).getID());

					if((Boolean)event.getButton().getValue()) {
						// New Miss
						app.storage.addMiss(miss);
					} else {
						// Delete Miss
						app.storage.deleteMiss(Miss.EXCUSED_MISS, miss.getDate(), miss.getIDStudent(), miss.getIDClassblock());
					}
					updateEnabledCheckBoxes(student);
				}
			});
			
			CheckBox delayCheckBox = new CheckBox();
			delayCheckBox.setData(student);
			if(app.storage.existsMiss(Miss.DELAY, (Date)datePopupDateField.getValue(), ((Classblock)classblockComboBox.getValue()).getID(), student.getID())) {
				delayCheckBox.setValue(true);
			}
			delayCheckBox.setImmediate(true);
			delayCheckBox.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					Student student = (Student)event.getButton().getData();
					Miss miss = new Miss(0, Miss.DELAY, (Date)datePopupDateField.getValue(), "", student.getID(), app.user.getID(), ((Classblock)classblockComboBox.getValue()).getIDSubject(), ((Classblock)classblockComboBox.getValue()).getID());

					if((Boolean)event.getButton().getValue()) {
						app.storage.addMiss(miss);
					} else {
						app.storage.deleteMiss(Miss.DELAY, miss.getDate(), miss.getIDStudent(), miss.getIDClassblock());
					}
					updateEnabledCheckBoxes(student);
				}
			});
			
			CheckBox expulsionCheckBox = new CheckBox();
			expulsionCheckBox.setData(student);
			if(app.storage.existsMiss(Miss.EXPULSION, (Date)datePopupDateField.getValue(), ((Classblock)classblockComboBox.getValue()).getID(), student.getID())) {
				expulsionCheckBox.setValue(true);
			}
			expulsionCheckBox.setImmediate(true);
			expulsionCheckBox.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					Student student = (Student)event.getButton().getData();
					Miss miss = new Miss(0, Miss.EXPULSION, (Date)datePopupDateField.getValue(), "", student.getID(), app.user.getID(), ((Classblock)classblockComboBox.getValue()).getIDSubject(), ((Classblock)classblockComboBox.getValue()).getID());

					if((Boolean)event.getButton().getValue()) {
						app.storage.addMiss(miss);
					} else {
						app.storage.deleteMiss(Miss.EXPULSION, miss.getDate(), miss.getIDStudent(), miss.getIDClassblock());
					}
					updateEnabledCheckBoxes(student);
				}
			});
			
			Object row[] = { student.getName(), student.getSurname1(), student.getSurname2(), missCheckBox, excusedmissCheckBox, delayCheckBox, expulsionCheckBox};
			
			table.addItem(row, new Integer(student.getID()));
			
			updateEnabledCheckBoxes(student);
		}
    }
    
    /* Updates the enabled/disabled state of checkboxes for a student */
    private void updateEnabledCheckBoxes(Student student) {
    	boolean missFlag = true;
    	boolean excusedFlag = true;
    	boolean delayFlag = true;
    	boolean expulsionFlag = true;
    	
    	CheckBox missCheckBox = (CheckBox)table.getItem(student.getID()).getItemProperty("Miss").getValue();
    	if((Boolean)missCheckBox.getValue()) {
    		excusedFlag = false;
    		delayFlag = false;
    		expulsionFlag = false;
    	}
    	
    	CheckBox excusedCheckBox = (CheckBox)table.getItem(student.getID()).getItemProperty("Excused").getValue();
    	if((Boolean)excusedCheckBox.getValue()) {
    		missFlag = false;
    		delayFlag = false;
    		expulsionFlag = false;
    	}
    	
    	CheckBox delayCheckBox = (CheckBox)table.getItem(student.getID()).getItemProperty("Delay").getValue();
    	if((Boolean)delayCheckBox.getValue()) {
    		missFlag = false;
    		excusedFlag = false;
    	}
    	
    	CheckBox expulsionCheckBox = (CheckBox)table.getItem(student.getID()).getItemProperty("Expulsion").getValue();
    	if((Boolean)expulsionCheckBox.getValue()) {
    		missFlag = false;
    		excusedFlag = false;
    	}
    	
    	if(missFlag != missCheckBox.isEnabled()) missCheckBox.setEnabled(missFlag);
    	if(excusedFlag != excusedCheckBox.isEnabled()) excusedCheckBox.setEnabled(excusedFlag);
    	if(delayFlag != delayCheckBox.isEnabled()) delayCheckBox.setEnabled(delayFlag);
    	if(expulsionFlag != expulsionCheckBox.isEnabled()) expulsionCheckBox.setEnabled(expulsionFlag);
    }
    
    private class PasswordChangeWindow extends Window {
    	
    	private final Form passwordForm = new Form();
    	private final PasswordInfo passwordInfo = new PasswordInfo();
    	
    	
    	public class PasswordInfo {
    		String passwordA = "";
    		String passwordB = "";
    		
			public String getPasswordA() {
				return passwordA;
			}
			public void setPasswordA(String passwordA) {
				this.passwordA = passwordA;
			}
			public String getPasswordB() {
				return passwordB;
			}
			public void setPasswordB(String passwordB) {
				this.passwordB = passwordB;
			}
    		
    		
    	}
    	
    	public PasswordChangeWindow() {
    		
    		BeanItem<PasswordInfo> passwordBean = new BeanItem<PasswordInfo>(passwordInfo);
    		passwordForm.setItemDataSource(passwordBean);
    		
    		TextField tf = (TextField)passwordForm.getField("passwordA");
    		
    		tf.setRequired(true);
    		tf.setCaption(app.locale.getString("TEACHERMAINLAYOUT_PASSWORDA_CAPTION"));
    		tf.setRequiredError(app.locale.getString("TEACHERMAINLAYOUT_PASSWORDA_REQUIREDERROR"));
    		tf.setSecret(true);
    		
    		tf = (TextField)passwordForm.getField("passwordB");
    		
    		tf.setRequired(true);
    		tf.setCaption(app.locale.getString("TEACHERMAINLAYOUT_PASSWORDB_CAPTION"));
    		tf.setRequiredError(app.locale.getString("TEACHERMAINLAYOUT_PASSWORDB_REQUIREDERROR"));
    		tf.setSecret(true);
    		
    		passwordForm.setImmediate(true);
    		
    		Button okButton = new Button("Ok");
    		okButton.setIcon(new ThemeResource("../runo/icons/16/ok.png"));
    		okButton.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					Window parentWindow = (Window) getWindow().getParent();
					if(passwordInfo.passwordA.equals(passwordInfo.passwordB)) {
						app.user.setPassword(app.storage.getPasswordHash(passwordInfo.passwordA));
						app.storage.updateTeacher(app.user);
						parentWindow.showNotification(app.locale.getString("TEACHERMAINLAYOUT_PASSWORDCHANGEOKNOTIFICATION_CAPTION"), app.locale.getString("TEACHERMAINLAYOUT_PASSWORDCHANGEOKNOTIFICATION_DESCRIPTION"), Notification.TYPE_TRAY_NOTIFICATION);
						
						// Close window
						parentWindow.removeWindow(passwordChangeWindow);
						
						passwordForm.getField("passwordA").setValue("");
						passwordForm.getField("passwordB").setValue("");
					} else {
						parentWindow.showNotification(app.locale.getString("TEACHERMAINLAYOUT_PASSWORDCHANGEERRORNOTIFICATION_CAPTION"), app.locale.getString("TEACHERMAINLAYOUT_PASSWORDCHANGEERRORNOTIFICATION_DESCRIPTION"), Notification.TYPE_WARNING_MESSAGE);
					}
					
				}
			});
    		
    		passwordForm.getFooter().addComponent(okButton);
    		
    		Button cancelButton = new Button("Cancel");
    		cancelButton.setIcon(new ThemeResource("../runo/icons/16/cancel.png"));
    		cancelButton.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// Close window
					((Window) passwordChangeWindow.getParent()).removeWindow(passwordChangeWindow);
					passwordForm.getField("passwordA").setValue("");
					passwordForm.getField("passwordB").setValue("");
				}
			});
    		
    		passwordForm.getFooter().addComponent(cancelButton);
    		
    		addComponent(passwordForm);
    		
    		VerticalLayout layout = (VerticalLayout) getContent();
    		layout.setSpacing(true);
    		layout.setMargin(true);
    		layout.setSizeUndefined(); // We want the window to has automatic size !!!
    		
    		setModal(true);
    	}
    }
}

