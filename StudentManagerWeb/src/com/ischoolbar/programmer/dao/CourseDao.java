package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Course;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * 
 * @author llq
 *课程信息数据库操作
 */
public class CourseDao extends BaseDao {
	
	public List<Course> getCourseList(Course course,Page page){
		List<Course> ret = new ArrayList<Course>();
		String sql = "select * from s_course ";
		if(!StringUtil.isEmpty(course.getCourseName())){
			sql += "where course_name like '%" + course.getCourseName() + "%'";
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				Course cl = new Course();
				cl.setId(resultSet.getInt("id"));
				cl.setCourseName(resultSet.getString("course_name"));
				cl.setCourseType(resultSet.getInt("course_type"));
				cl.setCourseTeacher(resultSet.getInt("course_teacher"));
				cl.setCourseClass(resultSet.getInt("course_class"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public int getCourseListTotal(Course course){
		int total = 0;
		String sql = "select count(*)as total from s_course ";
		if(!StringUtil.isEmpty(course.getCourseName())){
			sql += "where course_name like '%" + course.getCourseName() + "%'";
		}
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	public int getNum() {
		int total = 0;
		String sql = "select count(*)as total from s_course ";
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	public int getPNum() {
		int total = 0;
		String sql = "select count(*)as total from s_course where course_type=1";
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	
	// 专业理工科 数量
	public int getMNum() {
		int total = 0;
		String sql = "select count(*)as total from s_course where course_type=0 ";
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	// 专业 文科数量
	public int getM1Num() {
		int total = 0;
		String sql = "select count(*)as total from s_course where course_type=3";
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	// 选修课数量
	public int getENum() {
		int total = 0;
		String sql = "select count(*)as total from s_course where course_type=2 ";
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	public boolean addCourse(Course course){
		System.out.println("进来了");
		String sql = "insert into s_course values(null,'"+course.getCourseName()+"',"+course.getCourseType()+","+course.getCourseTeacher()+","+course.getCourseClass()+") ";
		System.out.println(sql);
		return update(sql);
	}
	public boolean deleteCourse(int id){
		String sql = "delete from s_course where id = "+id;
		return update(sql);
	}
	public boolean editCourse(Course course) {
		// TODO Auto-generated method stub
		String sql = "update s_course set course_name = '"+course.getCourseName()+"',course_type = "+course.getCourseType()+",course_teacher="+course.getCourseTeacher()+",course_class="+course.getCourseClass()+" where id = " + course.getId();
		return update(sql);
	}
	
}
