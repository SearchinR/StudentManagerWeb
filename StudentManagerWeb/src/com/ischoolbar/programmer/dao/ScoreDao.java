package com.ischoolbar.programmer.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.Score;
import com.ischoolbar.programmer.util.StringUtil;

public class ScoreDao extends BaseDao {
	public boolean addScore(Score score){
		String sql = "insert into s_score values(null,"+score.getScore()+","+score.getStudentId()+","+score.getCourseId()+",'"+score.getStudentName()+"',"+score.getClazzId()+")";
		System.out.println(sql);
		return update(sql);
	}
	public boolean editScore(Score score) {
		// TODO Auto-generated method stub
		String sql = "update s_score set score = "+score.getScore();
		sql += ",student_id = " + score.getStudentId();
		sql += ",student_name = '" + score.getStudentName()+"'";
		sql += ",course_id = " + score.getCourseId();
		sql += ",clazz_id = " + score.getClazzId();
		sql += " where id = " + score.getId();
		System.out.println(sql);
		return update(sql);
	}

	public boolean deleteScore(String ids) {
		// TODO Auto-generated method stub
		String sql = "delete from s_score where id in("+ids+")";
		return update(sql);
	}
	
	public Score getScore(int id){
		String sql = "select * from s_score where id = " + id;
		Score score = null;
		ResultSet resultSet = query(sql);
		try {
			if(resultSet.next()){
				score = new Score();
				score.setId(resultSet.getInt("id"));
				score.setScore(resultSet.getDouble("score"));
				score.setStudentId(resultSet.getInt("student_id"));
				score.setCourseId(resultSet.getInt("course_id"));
				score.setStudentName(resultSet.getString("student_name"));
				score.setClazzId(resultSet.getInt("clazz_id"));
				return score;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return score;
	}
	/**
	 * 批量插入数据
	 * @param list
	 * @return
	 */
	public boolean InsertScoreList(List<Score> list){
		int length=list.size();
		boolean key=false;
		
		for(int i=0;i<length;i++) {
		//	String sql="insert into s_score(id,student_id,course_id,student_name,clazz_id,score) values (#{"+list.get(i).getId()+"},#{"+list.get(i).getStudentId()+"},#{"+list.get(i).getCourseId()+"},#{"+list.get(i).getStudentName()+"},#{"+list.get(i).getClazzId()+"},#{"+list.get(i).getScore()+"})";
			// VALUES(#{id}, #{name}, #{delFlag})
			String sql="insert into s_score values (null,"+list.get(i).getScore()+","+list.get(i).getStudentId()+","+list.get(i).getCourseId()+",'"+list.get(i).getStudentName()+"',"+list.get(i).getClazzId()+")";
		//	String sql = "insert into s_score values(null,"+score.getScore()+","+score.getStudentId()+","+score.getCourseId()+",'"+score.getStudentName()+"',"+score.getClazzId()+")";

			System.out.println(sql);
			key=update(sql);
		}
		return key;
		
	}
	
	/**
	 * 导出表格
	 * @return
	 */
	public List<Score> ScoreExcel(){
		List<Score> ret = new ArrayList<Score>();
		String sql = "select * from s_score ";
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				Score s = new Score();
				s.setId(resultSet.getInt("id"));
				s.setScore(resultSet.getDouble("score"));
				s.setStudentId(resultSet.getInt("student_id"));
				s.setCourseId(resultSet.getInt("course_id"));
				s.setStudentName(resultSet.getString("student_name"));
				s.setClazzId(resultSet.getInt("clazz_id"));
				ret.add(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public List<Score> getScoreList(Score score,Page page){
		List<Score> ret = new ArrayList<Score>();
		String sql = "select * from s_score ";
		if(!StringUtil.isEmpty(score.getStudentName())){
			sql += "and student_name like '%" + score.getStudentName() + "%'";
		}
		if(score.getCourseId() != 0){
			sql += " and course_id = " + score.getCourseId();
		}
		if(score.getId() != 0){
			sql += " and id = " + score.getId();
		}
		if(score.getStudentId() != 0){
			sql += " and student_id = " + score.getStudentId();
		}
		if(score.getCourseId() != 0){
			sql += " and courese_id = " + score.getCourseId();
		}
		if(score.getClazzId() != 0){
			sql += " and clazz_id = " + score.getClazzId();
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				Score s = new Score();
				s.setId(resultSet.getInt("id"));
				s.setScore(resultSet.getDouble("score"));
				s.setStudentId(resultSet.getInt("student_id"));
				s.setCourseId(resultSet.getInt("course_id"));
				s.setStudentName(resultSet.getString("student_name"));
				s.setClazzId(resultSet.getInt("clazz_id"));
				ret.add(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	
	public int getScoreListTotal(Score score){
		int total = 0;
		String sql = "select count(*)as total from s_score ";
		if(!StringUtil.isEmpty(score.getStudentName())){
			sql += "and student_name like '%" + score.getStudentName() + "%'";
		}
		if(score.getStudentId() != 0){
			sql += " and student_id = " + score.getStudentId();
		}
		if(score.getId() != 0){
			sql += " and id = " + score.getId();
		}
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
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
	public double getManScore(int i){
		List<Score> ret = new ArrayList<Score>();
		String sql = "select * from\r\n" + 
				"s_score\r\n" + 
				"INNER JOIN s_student ON s_score.student_id = s_student.id\r\n" + 
				"INNER JOIN s_course ON s_score.course_id = s_course.id\r\n" + 
				"where s_student.sex='男' and s_course.course_type="+i;
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				Score s = new Score();
				s.setId(resultSet.getInt("id"));
				s.setScore(resultSet.getDouble("score"));
				s.setStudentId(resultSet.getInt("student_id"));
				s.setCourseId(resultSet.getInt("course_id"));
				s.setStudentName(resultSet.getString("student_name"));
				s.setClazzId(resultSet.getInt("clazz_id"));
				ret.add(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int length=ret.size();
		System.out.println(length);
		double sum=0;
		for(int j=0;j<length;j++) {
			sum+=ret.get(j).getScore();
			System.out.println(j);
		}
		
		return sum/length;
	}
	public double getWomanScore(int i){
		List<Score> ret = new ArrayList<Score>();
		String sql = "select * from\r\n" + 
				"s_score\r\n" + 
				"INNER JOIN s_student ON s_score.student_id = s_student.id\r\n" + 
				"INNER JOIN s_course ON s_score.course_id = s_course.id\r\n" + 
				"where s_student.sex='女'  and s_course.course_type="+i;
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				Score s = new Score();
				s.setId(resultSet.getInt("id"));
				s.setScore(resultSet.getDouble("score"));
				s.setStudentId(resultSet.getInt("student_id"));
				s.setCourseId(resultSet.getInt("course_id"));
				s.setStudentName(resultSet.getString("student_name"));
				s.setClazzId(resultSet.getInt("clazz_id"));
				ret.add(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int length=ret.size();
		double sum=0;
		for(int j=0;j<length;j++) {
			sum+=ret.get(j).getScore();
		}
		return sum/length;
	}
	
	public double getEducationScore(String edu,String sex){
		List<Score> ret = new ArrayList<Score>();
		String sql = "select * from s_score\r\n" + 
				"INNER JOIN s_course ON s_score.course_id = s_course.id\r\n" + 
				"INNER JOIN s_teacher ON s_course.course_teacher = s_teacher.id\r\n" + 
				"INNER JOIN s_student ON s_score.student_id = s_student.id\r\n" + 
				"where s_teacher.education='"+edu+"' and s_student.sex='"+sex+"'";
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				Score s = new Score();
				s.setId(resultSet.getInt("id"));
				s.setScore(resultSet.getDouble("score"));
				s.setStudentId(resultSet.getInt("student_id"));
				s.setCourseId(resultSet.getInt("course_id"));
				s.setStudentName(resultSet.getString("student_name"));
				s.setClazzId(resultSet.getInt("clazz_id"));
				ret.add(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int length=ret.size();
		double sum=0;
		for(int j=0;j<length;j++) {
			sum+=ret.get(j).getScore();
		}
		return sum/length;
	}

}
