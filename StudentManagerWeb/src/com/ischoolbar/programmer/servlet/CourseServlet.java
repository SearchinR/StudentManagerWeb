package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.ischoolbar.programmer.dao.ClazzDao;
import com.ischoolbar.programmer.dao.CourseDao;
import com.ischoolbar.programmer.model.Clazz;
import com.ischoolbar.programmer.model.Course;
import com.ischoolbar.programmer.model.Page;
/**
 * 
 * @author llq
 *班级信息管理servlet
 */
public class CourseServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8130552623042470543L;
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("toCourseListView".equals(method)){
			courseList(request,response);
		}else if("getCourseList".equals(method)){
			getCourseList(request, response);
		}else if("AddCourse".equals(method)){
			addCourse(request, response);
		}else if("DeleteCourse".equals(method)){
			deleteCourse(request, response);
		}else if("EditCourse".equals(method)){
			editCourse(request, response);
		}
	}
	private void editCourse(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(request.getParameter("id"));
		String courseName = request.getParameter("courseName"); 
		int courseType = Integer.parseInt(request.getParameter("courseType"));
		int courseTeacher = Integer.parseInt(request.getParameter("courseTeacher"));
		int courseClass = Integer.parseInt(request.getParameter("courseClass"));
		Course course = new Course();
		course.setCourseName(courseName);
		course.setCourseType(courseType);
		course.setCourseTeacher(courseTeacher);
		course.setCourseClass(courseClass);
		course.setId(id);
		CourseDao courseDao = new CourseDao();
		if(courseDao.editCourse(course)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				courseDao.closeCon();
			}
		}
	}
	/**
	 * 删除课程
	 * @param request
	 * @param response
	 */
	private void deleteCourse(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(request.getParameter("id"));
		CourseDao courseDao = new CourseDao();
		if(courseDao.deleteCourse(id)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				courseDao.closeCon();
			}
		}
	}
	
	private void addCourse(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String courseName = request.getParameter("courseName"); 
		int courseType = Integer.parseInt(request.getParameter("courseType"));
		int courseTeacher = Integer.parseInt(request.getParameter("courseTeacher"));
		int courseClass = Integer.parseInt(request.getParameter("courseClass"));
		Course course = new Course();
		course.setCourseName(courseName);
		course.setCourseType(courseType);
		course.setCourseTeacher(courseTeacher);
		course.setCourseClass(courseClass);

		CourseDao courseDao = new CourseDao();
		if(courseDao.addCourse(course)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				courseDao.closeCon();
			}
		}
		
	}
	private void courseList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		try {
			request.getRequestDispatcher("view/courseList.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getCourseList(HttpServletRequest request,HttpServletResponse response){
		String name = request.getParameter("courseName");
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Course course = new Course();
		course.setCourseName(name);
		CourseDao courseDao = new CourseDao();
		List<Course> courseList = courseDao.getCourseList(course, new Page(currentPage, pageSize));
		int total = courseDao.getCourseListTotal(course);
		courseDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", courseList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)){
				response.getWriter().write(JSONArray.fromObject(courseList).toString());
			}else{
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
