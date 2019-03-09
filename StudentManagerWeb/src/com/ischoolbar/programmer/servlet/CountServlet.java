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
import com.ischoolbar.programmer.dao.ScoreDao;
import com.ischoolbar.programmer.dao.StudentDao;
import com.ischoolbar.programmer.dao.TeacherDao;
import com.ischoolbar.programmer.model.Clazz;
import com.ischoolbar.programmer.model.Page;
/**
 * 
 * @author llq
 *�༶��Ϣ����servlet
 */
public class CountServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6766790379706670781L;
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("toCountListView".equals(method)){
			clazzList(request,response);
		}else if("getClazzList".equals(method)){
			getClazzList(request, response);
		}
		
	}
	
	
	private void clazzList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("count");
		//�õ���ʦ����
		TeacherDao teacherDao=new TeacherDao();
		int teacherNum=teacherDao.getNum();
		// �γ�����
		CourseDao courseDao=new CourseDao();
		int courseNum=courseDao.getNum();
		// ѧ������
		StudentDao studentDao=new StudentDao();
		int studentNum=studentDao.getNum();
		// �༶����
		ClazzDao clazzDao=new ClazzDao();
		int clazzNum=clazzDao.getNum();
		// ��Ů�����Լ��ɼ��Ա�
		int stuMNum=studentDao.getMNum();
		int stuWNum=studentDao.getWNum();
		ScoreDao scoreDao=new ScoreDao();
		// רҵ��������Ů��ƽ���ɼ����γ�ƽ���ɼ�
		double majorM =scoreDao.getManScore(0);
		double majorW =scoreDao.getWomanScore(0);
		double majorB=(majorM+majorW)/2;
		// ������������Ů��ƽ���ɼ� �γ�ƽ���ɼ�
		double publicM=scoreDao.getManScore(1);
		double publicW=scoreDao.getWomanScore(1);
		double publicB=(publicM+publicW)/2;
		// ѡ�޿�������Ů��ƽ���ɼ� �γ�ƽ���ɼ�
		double electiveM=scoreDao.getManScore(2);
		double electiveW=scoreDao.getWomanScore(2);
		double electiveB=(electiveM+electiveW)/2;
		//��ʿ�� �о��� ��������ʦ������
		int BenNum=teacherDao.getBenNum();
		int YanNum=teacherDao.getYanNum();
		int BoNum=teacherDao.getBoNum();
		// ����γ̵�����
		int PNum=courseDao.getPNum();
		int MNum=courseDao.getMNum();
		int ENum=courseDao.getENum();
		// ��ʿ�� �о��� ���� �����༶����Ůƽ���� �Լ���ƽ����
		double BoSM=scoreDao.getEducationScore("��ʿ", "��");
		double BoSW=scoreDao.getEducationScore("��ʿ", "Ů");
		double YSM=scoreDao.getEducationScore("�о���", "��");
		double YSW=scoreDao.getEducationScore("�о���", "Ů");
		double BSM=scoreDao.getEducationScore("������", "��");
		double BSW=scoreDao.getEducationScore("������", "Ů");
		double BoB=(BoSM+BoSW)/2;
		double YB=(YSM+YSW)/2;
		double BB=(BSM+BSW)/2;
		// �洢����
		request.setAttribute("BoSM", BoSM);
		request.setAttribute("BoSW", BoSW);
		request.setAttribute("YSM", YSM);
		request.setAttribute("YSW", YSW);
		request.setAttribute("BSM", BSM);
		request.setAttribute("BSW", BSW);
		request.setAttribute("BoB", BoB);
		request.setAttribute("YB", YB);
		request.setAttribute("BB", BB);
		request.setAttribute("PNum", PNum);
		request.setAttribute("MNum", MNum);
		request.setAttribute("ENum", ENum);
		request.setAttribute("BenNum", BenNum);
		request.setAttribute("YanNum", YanNum);
		request.setAttribute("BoNum", BoNum);
		request.setAttribute("teacherNum", teacherNum);
		request.setAttribute("studentNum", studentNum);
		request.setAttribute("clazzNum", clazzNum);
		request.setAttribute("courseNum", courseNum);
		request.setAttribute("stuMNum", stuMNum);
		request.setAttribute("stuWNum", stuWNum);
		request.setAttribute("majorM", majorM);
		request.setAttribute("majorW", majorW);
		request.setAttribute("publicM", publicM);
		request.setAttribute("publicW", publicW);
		request.setAttribute("electiveM", electiveM);
		request.setAttribute("electiveW", electiveW);
		request.setAttribute("publicB", publicW);
		request.setAttribute("electiveB", electiveM);
		request.setAttribute("majorB", electiveW);
	
		
		try {
			request.getRequestDispatcher("view/count.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getClazzList(HttpServletRequest request,HttpServletResponse response){
		String name = request.getParameter("clazzName");
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Clazz clazz = new Clazz();
		clazz.setName(name);
		ClazzDao clazzDao = new ClazzDao();
		List<Clazz> clazzList = clazzDao.getClazzList(clazz, new Page(currentPage, pageSize));
		int total = clazzDao.getClazzListTotal(clazz);
		clazzDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", clazzList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)){
				response.getWriter().write(JSONArray.fromObject(clazzList).toString());
			}else{
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}