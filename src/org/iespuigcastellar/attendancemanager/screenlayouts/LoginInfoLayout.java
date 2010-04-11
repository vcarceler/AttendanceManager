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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public class LoginInfoLayout extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5798352602846152576L;

	public LoginInfoLayout() {
		
	}
	
	public void attach() {
		
		// Show explicit logininfo
		//addComponent(new org.iespuigcastellar.attendancemanager.screenlayouts.logininfo.BlackHawkLoginInfoLayout());
		
		// Show random logininfo
		try {
			Class classes[] = getClasses("org.iespuigcastellar.attendancemanager.screenlayouts.logininfo");
			int i = (int)(Math.random()*classes.length);
			addComponent((Layout)classes[i].newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get classess from a package
	 * 
	 * http://snippets.dzone.com/posts/show/4831
	 */
	private static Class[] getClasses(String packageName) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
	}
	
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
