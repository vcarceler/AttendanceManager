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

public class Constants {
	public static final int SHORT_VARCHAR_LENGTH = 45;
	public static final int LONG_VARCHAR_LENGTH = 255;
	
	/**Returns String s with a max of Constants.SHORT_VARCHAR_LENGTH characters*/
	public static String shortString(String s) {
		return s.length()>=SHORT_VARCHAR_LENGTH?s.substring(0, SHORT_VARCHAR_LENGTH):s;
	}
	
	/**Returns String s with a max of Constants.LONG_VARCHAR_LENGTH characters*/
	public static String longString(String s) {
		return s.length()>=LONG_VARCHAR_LENGTH?s.substring(0, LONG_VARCHAR_LENGTH):s;
	}
}
