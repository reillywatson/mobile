package com.vasken.util;

// Java's calendar stuff is suuuuuper broken (2010-05-01.add(-1)==2010-04-31!!!!),
// and I don't need very much fancy stuff (ie Joda Time), so let's just write it!
//Doesn't handle crazy old years, Gregorian rules!
public class YearMonthDay {
	private int year;
	private int month;
	private int day;
	public YearMonthDay(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public boolean isLeapYear(int year) {
		return year % 4 == 0 && (year % 100 > 0 || year % 400 == 0);
	}
	
	// February has 29 here to facilitate 
	int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	
	public YearMonthDay addDays(int numDays) {
		int year = this.year;
		int month = this.month;
		int day = this.day;
		for (int i = 0; i < numDays; i++) {
			day++;
			if (isLeapYear(year) && month == 2 && day == 29) {
				// woo, leap day!
			}
			else if (day > daysInMonth[month - 1]) {
				day = 1;
				month++;
				if (month > 12) {
					month = 1;
					year++;
				}
			}
			numDays--;
		}
		return new YearMonthDay(year, month, day);
	}
	
	public YearMonthDay subtractDays(int numDays) {
		int year = this.year;
		int month = this.month;
		int day = this.day;
		for (int i = 0; i < numDays; i++) {
			day--;
			if (day == 0) {
				month--;
				if (month == 0) {
					year--;
					month = 12;
				}
				day = daysInMonth[month - 1];
				if (month == 2 && isLeapYear(year))
					day = 29;
			}
		}
		return new YearMonthDay(year, month, day);
	}
}