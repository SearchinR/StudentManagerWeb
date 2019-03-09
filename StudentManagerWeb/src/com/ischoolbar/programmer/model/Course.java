package com.ischoolbar.programmer.model;

/**
 * 课程实体类
 * @author Administrator
 *
 */
public class Course {
	private	int id;
	private String courseName;
	private	int courseType;
	private int courseTeacher;
	private int courseClass;
	public Course() {
		
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCourseType() {
		return courseType;
	}
	public void setCourseType(int courseType) {
		this.courseType = courseType;
	}
	public int getCourseTeacher() {
		return courseTeacher;
	}
	public void setCourseTeacher(int courseTeacher) {
		this.courseTeacher = courseTeacher;
	}
	public int getCourseClass() {
		return courseClass;
	}
	public void setCourseClass(int courseClass) {
		this.courseClass = courseClass;
	}
	
}
