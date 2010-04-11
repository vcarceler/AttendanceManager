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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class BlackHawkLoginInfoLayout extends VerticalLayout {
	
	public void attach() {
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(true);
		
		Embedded item = new Embedded("", new ClassResource("screenlayouts/logininfo/black_hawk.jpg", getApplication())); 
		hlayout.addComponent(item);
		hlayout.setComponentAlignment(item, Alignment.MIDDLE_CENTER);
		
		Label haikuText = new Label(
				"<p>\"Línea de gansos en vuelo;<br> al pie de la colina,<br> la luna puesta por sello\"</p>"
				+ "<p>\"Iba yo a los cerezos en flor<br> Dormía bajo ellos<br> Así era mi pasatiempo\"</p>"
				+ "<p>\"La lluvia en invierno<br> Muestra lo que los ojos ven<br> Como si fuera cosa antigua\"</p>"
				+ "<p>Yosa Buson</p>");
		
		haikuText.setContentMode(Label.CONTENT_XHTML);
		
		hlayout.addComponent(haikuText);
		
		addComponent(hlayout);
	}

}
