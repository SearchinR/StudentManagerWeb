package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
 *班级信息管理servlet
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
		}else if("toCountListViewSex".equals(method)){
			toCountListViewSex(request, response);
		}else if("toCountListViewTeacher".equals(method)){
			toCountListViewTeacher(request, response);
		}else if("toCountListViewClass".equals(method)){
			toCountListViewClass(request, response);
		}
		else if("toCountListViewAddress".equals(method)){
			toCountListViewAddress(request, response);
		}
		
		
	}
	
	
	private void clazzList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("count");
		//拿到老师数量
		TeacherDao teacherDao=new TeacherDao();
		int teacherNum=teacherDao.getNum();
		// 课程数量
		CourseDao courseDao=new CourseDao();
		int courseNum=courseDao.getNum();
		// 学生数量
		StudentDao studentDao=new StudentDao();
		int studentNum=studentDao.getNum();
		// 班级数量
		ClazzDao clazzDao=new ClazzDao();
		int clazzNum=clazzDao.getNum();
		// 男女比例以及成绩对比
		int stuMNum=studentDao.getMNum();
		int stuWNum=studentDao.getWNum();
		ScoreDao scoreDao=new ScoreDao();
		// 专业课理工科男生、女生平均成绩，课程平均成绩
		double majorM =scoreDao.getManScore(0);
		double majorW =scoreDao.getWomanScore(0);
		double majorB=(majorM+majorW)/2;
		// 专业课文科男生、女生平均成绩，课程平均成绩
		double majorMW =scoreDao.getManScore(3);
		double majorWW =scoreDao.getWomanScore(3);
		double majorBW=majorMW/2+majorWW/2;
		
		// 公共课男生、女生平均成绩 课程平均成绩
		double publicM=scoreDao.getManScore(1);
		double publicW=scoreDao.getWomanScore(1);
		double publicB=(publicM+publicW)/2;
		// 选修课男生、女生平均成绩 课程平均成绩
		double electiveM=scoreDao.getManScore(2);
		double electiveW=scoreDao.getWomanScore(2);
		double electiveB=(electiveM+electiveW)/2;
		//博士生 研究生 本科生教师的数量
		int BenNum=teacherDao.getBenNum();
		int YanNum=teacherDao.getYanNum();
		int BoNum=teacherDao.getBoNum();
		// 各类课程的数量
		int PNum=courseDao.getPNum();
		int MNum=courseDao.getMNum();
		int ENum=courseDao.getENum();
		int M1Num=courseDao.getM1Num();
		// 博士生 研究生 本科 所带班级中男女平均分 以及总平均分
		double BoSM=scoreDao.getEducationScore("博士", "男");
		double BoSW=scoreDao.getEducationScore("博士", "女");
		double YSM=scoreDao.getEducationScore("研究生", "男");
		double YSW=scoreDao.getEducationScore("研究生", "女");
		double BSM=scoreDao.getEducationScore("本科生", "男");
		double BSW=scoreDao.getEducationScore("本科生", "女");
		double BoB=(BoSM+BoSW)/2;
		double YB=(YSM+YSW)/2;
		double BB=(BSM+BSW)/2;
		// 存储数据
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
		request.setAttribute("M1Num", M1Num);
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
		request.setAttribute("majorMW", majorMW);
		request.setAttribute("majorWW", majorWW);
		request.setAttribute("publicM", publicM);
		request.setAttribute("publicW", publicW);
		request.setAttribute("electiveM", electiveM);
		request.setAttribute("electiveW", electiveW);
		request.setAttribute("publicB", publicB);
		request.setAttribute("electiveB", electiveB);
		request.setAttribute("majorB", majorB);
		request.setAttribute("majorBW", majorBW);
		
		// 测试
		Map<String ,String> map=scoreDao.getAddressScoreNum1();
		System.out.println("吕梁"+map.get("吕梁"));
		Iterator it = map.entrySet().iterator();//取得map集合的中每个键值对所对应的iterator对象
		while(it.hasNext()){
		   String str = it.next().toString();
		   if(str.length()>1){
		    String s[] = str.split("=");
		    System.out.println(s[1]);//如果直接是System.out.println(str);则会打印出1=a 2=b,这样的效果。这里的s[1]只是为了得到map的值，而没有打印键
		   }
		}
		// 按地区比较成绩
		int LLNum=Integer.parseInt(map.get("吕梁"));
		int BJNum=Integer.parseInt(map.get("北京"));
		int HBNum=Integer.parseInt(map.get("河北"));
		int HNNum=Integer.parseInt(map.get("河南"));
		double LLS=scoreDao.getAddressScoreB("吕梁");
		double BJS=scoreDao.getAddressScoreB("北京");
		double HBS=scoreDao.getAddressScoreB("河北");
		double HNS=scoreDao.getAddressScoreB("河南");
		request.setAttribute("LLNum", LLNum);
		request.setAttribute("BJNum", BJNum);
		request.setAttribute("HBNum", HBNum);
		request.setAttribute("HNNum", HNNum);
		request.setAttribute("LLS", LLS);
		request.setAttribute("BJS", BJS);
		request.setAttribute("HBS", HBS);
		request.setAttribute("HNS", HNS);
		//测试结束
		

		
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

	private void toCountListViewSex(HttpServletRequest request,
						   HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("count");
		//拿到老师数量
		TeacherDao teacherDao=new TeacherDao();
		int teacherNum=teacherDao.getNum();
		// 课程数量
		CourseDao courseDao=new CourseDao();
		int courseNum=courseDao.getNum();
		// 学生数量
		StudentDao studentDao=new StudentDao();
		int studentNum=studentDao.getNum();
		// 班级数量
		ClazzDao clazzDao=new ClazzDao();
		int clazzNum=clazzDao.getNum();
		// 男女比例以及成绩对比
		int stuMNum=studentDao.getMNum();
		int stuWNum=studentDao.getWNum();
		ScoreDao scoreDao=new ScoreDao();
		// 专业课理工科男生、女生平均成绩，课程平均成绩
		double majorM =scoreDao.getManScore(0);
		double majorW =scoreDao.getWomanScore(0);
		double majorB=(majorM+majorW)/2;
		// 专业课文科男生、女生平均成绩，课程平均成绩
		double majorMW =scoreDao.getManScore(3);
		double majorWW =scoreDao.getWomanScore(3);
		double majorBW=majorMW/2+majorWW/2;
		
		// 公共课男生、女生平均成绩 课程平均成绩
		double publicM=scoreDao.getManScore(1);
		double publicW=scoreDao.getWomanScore(1);
		double publicB=(publicM+publicW)/2;
		// 选修课男生、女生平均成绩 课程平均成绩
		double electiveM=scoreDao.getManScore(2);
		double electiveW=scoreDao.getWomanScore(2);
		double electiveB=(electiveM+electiveW)/2;
		//博士生 研究生 本科生教师的数量
		int BenNum=teacherDao.getBenNum();
		int YanNum=teacherDao.getYanNum();
		int BoNum=teacherDao.getBoNum();
		// 各类课程的数量
		int PNum=courseDao.getPNum();
		int MNum=courseDao.getMNum();
		int ENum=courseDao.getENum();
		int M1Num=courseDao.getM1Num();
		// 博士生 研究生 本科 所带班级中男女平均分 以及总平均分
		double BoSM=scoreDao.getEducationScore("博士", "男");
		double BoSW=scoreDao.getEducationScore("博士", "女");
		double YSM=scoreDao.getEducationScore("研究生", "男");
		double YSW=scoreDao.getEducationScore("研究生", "女");
		double BSM=scoreDao.getEducationScore("本科生", "男");
		double BSW=scoreDao.getEducationScore("本科生", "女");
		double BoB=(BoSM+BoSW)/2;
		double YB=(YSM+YSW)/2;
		double BB=(BSM+BSW)/2;
		// 存储数据
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
		request.setAttribute("M1Num", M1Num);
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
		request.setAttribute("majorMW", majorMW);
		request.setAttribute("majorWW", majorWW);
		request.setAttribute("publicM", publicM);
		request.setAttribute("publicW", publicW);
		request.setAttribute("electiveM", electiveM);
		request.setAttribute("electiveW", electiveW);
		request.setAttribute("publicB", publicB);
		request.setAttribute("electiveB", electiveB);
		request.setAttribute("majorB", majorB);
		request.setAttribute("majorBW", majorBW);
		
		


		try {
			request.getRequestDispatcher("view/count-sex.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void toCountListViewTeacher(HttpServletRequest request,
						   HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("count");
		//拿到老师数量
		TeacherDao teacherDao=new TeacherDao();
		int teacherNum=teacherDao.getNum();
		// 课程数量
		CourseDao courseDao=new CourseDao();
		int courseNum=courseDao.getNum();
		// 学生数量
		StudentDao studentDao=new StudentDao();
		int studentNum=studentDao.getNum();
		// 班级数量
		ClazzDao clazzDao=new ClazzDao();
		int clazzNum=clazzDao.getNum();
		// 男女比例以及成绩对比
		int stuMNum=studentDao.getMNum();
		int stuWNum=studentDao.getWNum();
		ScoreDao scoreDao=new ScoreDao();
		// 专业课理工科男生、女生平均成绩，课程平均成绩
		double majorM =scoreDao.getManScore(0);
		double majorW =scoreDao.getWomanScore(0);
		double majorB=(majorM+majorW)/2;
		// 专业课文科男生、女生平均成绩，课程平均成绩
		double majorMW =scoreDao.getManScore(3);
		double majorWW =scoreDao.getWomanScore(3);
		double majorBW=majorMW/2+majorWW/2;
		
		// 公共课男生、女生平均成绩 课程平均成绩
		double publicM=scoreDao.getManScore(1);
		double publicW=scoreDao.getWomanScore(1);
		double publicB=(publicM+publicW)/2;
		// 选修课男生、女生平均成绩 课程平均成绩
		double electiveM=scoreDao.getManScore(2);
		double electiveW=scoreDao.getWomanScore(2);
		double electiveB=(electiveM+electiveW)/2;
		//博士生 研究生 本科生教师的数量
		int BenNum=teacherDao.getBenNum();
		int YanNum=teacherDao.getYanNum();
		int BoNum=teacherDao.getBoNum();
		// 各类课程的数量
		int PNum=courseDao.getPNum();
		int MNum=courseDao.getMNum();
		int ENum=courseDao.getENum();
		int M1Num=courseDao.getM1Num();
		// 博士生 研究生 本科 所带班级中男女平均分 以及总平均分
		double BoSM=scoreDao.getEducationScore("博士", "男");
		double BoSW=scoreDao.getEducationScore("博士", "女");
		double YSM=scoreDao.getEducationScore("研究生", "男");
		double YSW=scoreDao.getEducationScore("研究生", "女");
		double BSM=scoreDao.getEducationScore("本科生", "男");
		double BSW=scoreDao.getEducationScore("本科生", "女");
		double BoB=(BoSM+BoSW)/2;
		double YB=(YSM+YSW)/2;
		double BB=(BSM+BSW)/2;
		// 存储数据
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
		request.setAttribute("M1Num", M1Num);
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
		request.setAttribute("majorMW", majorMW);
		request.setAttribute("majorWW", majorWW);
		request.setAttribute("publicM", publicM);
		request.setAttribute("publicW", publicW);
		request.setAttribute("electiveM", electiveM);
		request.setAttribute("electiveW", electiveW);
		request.setAttribute("publicB", publicB);
		request.setAttribute("electiveB", electiveB);
		request.setAttribute("majorB", majorB);
		request.setAttribute("majorBW", majorBW);
		
		
		try {
			
			request.getRequestDispatcher("view/count-teacher.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void toCountListViewClass(HttpServletRequest request,
									HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("count");
		//拿到老师数量
		TeacherDao teacherDao=new TeacherDao();
		int teacherNum=teacherDao.getNum();
		// 课程数量
		CourseDao courseDao=new CourseDao();
		int courseNum=courseDao.getNum();
		// 学生数量
		StudentDao studentDao=new StudentDao();
		int studentNum=studentDao.getNum();
		// 班级数量
		ClazzDao clazzDao=new ClazzDao();
		int clazzNum=clazzDao.getNum();
		// 男女比例以及成绩对比
		int stuMNum=studentDao.getMNum();
		int stuWNum=studentDao.getWNum();
		ScoreDao scoreDao=new ScoreDao();
		// 专业课理工科男生、女生平均成绩，课程平均成绩
		double majorM =scoreDao.getManScore(0);
		double majorW =scoreDao.getWomanScore(0);
		double majorB=(majorM+majorW)/2;
		// 专业课文科男生、女生平均成绩，课程平均成绩
		double majorMW =scoreDao.getManScore(3);
		double majorWW =scoreDao.getWomanScore(3);
		double majorBW=majorMW/2+majorWW/2;
		
		// 公共课男生、女生平均成绩 课程平均成绩
		double publicM=scoreDao.getManScore(1);
		double publicW=scoreDao.getWomanScore(1);
		double publicB=(publicM+publicW)/2;
		// 选修课男生、女生平均成绩 课程平均成绩
		double electiveM=scoreDao.getManScore(2);
		double electiveW=scoreDao.getWomanScore(2);
		double electiveB=(electiveM+electiveW)/2;
		//博士生 研究生 本科生教师的数量
		int BenNum=teacherDao.getBenNum();
		int YanNum=teacherDao.getYanNum();
		int BoNum=teacherDao.getBoNum();
		// 各类课程的数量
		int PNum=courseDao.getPNum();
		int MNum=courseDao.getMNum();
		int ENum=courseDao.getENum();
		int M1Num=courseDao.getM1Num();
		// 博士生 研究生 本科 所带班级中男女平均分 以及总平均分
		double BoSM=scoreDao.getEducationScore("博士", "男");
		double BoSW=scoreDao.getEducationScore("博士", "女");
		double YSM=scoreDao.getEducationScore("研究生", "男");
		double YSW=scoreDao.getEducationScore("研究生", "女");
		double BSM=scoreDao.getEducationScore("本科生", "男");
		double BSW=scoreDao.getEducationScore("本科生", "女");
		double BoB=(BoSM+BoSW)/2;
		double YB=(YSM+YSW)/2;
		double BB=(BSM+BSW)/2;
		// 存储数据
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
		request.setAttribute("M1Num", M1Num);
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
		request.setAttribute("majorMW", majorMW);
		request.setAttribute("majorWW", majorWW);
		request.setAttribute("publicM", publicM);
		request.setAttribute("publicW", publicW);
		request.setAttribute("electiveM", electiveM);
		request.setAttribute("electiveW", electiveW);
		request.setAttribute("publicB", publicB);
		request.setAttribute("electiveB", electiveB);
		request.setAttribute("majorB", majorB);
		request.setAttribute("majorBW", majorBW);

		try {
			request.getRequestDispatcher("view/count-class.jsp").forward(request, response);
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
	
	
	
	private void toCountListViewAddress(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("count");
		//拿到老师数量
		TeacherDao teacherDao=new TeacherDao();
		int teacherNum=teacherDao.getNum();
		// 课程数量
		CourseDao courseDao=new CourseDao();
		int courseNum=courseDao.getNum();
		// 学生数量
		StudentDao studentDao=new StudentDao();
		int studentNum=studentDao.getNum();
		// 班级数量
		ClazzDao clazzDao=new ClazzDao();
		int clazzNum=clazzDao.getNum();
		// 男女比例以及成绩对比
		int stuMNum=studentDao.getMNum();
		int stuWNum=studentDao.getWNum();
		ScoreDao scoreDao=new ScoreDao();
		// 专业课理工科男生、女生平均成绩，课程平均成绩
		double majorM =scoreDao.getManScore(0);
		double majorW =scoreDao.getWomanScore(0);
		double majorB=(majorM+majorW)/2;
		// 专业课文科男生、女生平均成绩，课程平均成绩
		double majorMW =scoreDao.getManScore(3);
		double majorWW =scoreDao.getWomanScore(3);
		double majorBW=majorMW/2+majorWW/2;
		
		// 公共课男生、女生平均成绩 课程平均成绩
		double publicM=scoreDao.getManScore(1);
		double publicW=scoreDao.getWomanScore(1);
		double publicB=(publicM+publicW)/2;
		// 选修课男生、女生平均成绩 课程平均成绩
		double electiveM=scoreDao.getManScore(2);
		double electiveW=scoreDao.getWomanScore(2);
		double electiveB=(electiveM+electiveW)/2;
		//博士生 研究生 本科生教师的数量
		int BenNum=teacherDao.getBenNum();
		int YanNum=teacherDao.getYanNum();
		int BoNum=teacherDao.getBoNum();
		// 各类课程的数量
		int PNum=courseDao.getPNum();
		int MNum=courseDao.getMNum();
		int ENum=courseDao.getENum();
		int M1Num=courseDao.getM1Num();
		// 博士生 研究生 本科 所带班级中男女平均分 以及总平均分
		double BoSM=scoreDao.getEducationScore("博士", "男");
		double BoSW=scoreDao.getEducationScore("博士", "女");
		double YSM=scoreDao.getEducationScore("研究生", "男");
		double YSW=scoreDao.getEducationScore("研究生", "女");
		double BSM=scoreDao.getEducationScore("本科生", "男");
		double BSW=scoreDao.getEducationScore("本科生", "女");
		double BoB=(BoSM+BoSW)/2;
		double YB=(YSM+YSW)/2;
		double BB=(BSM+BSW)/2;
		// 存储数据
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
		request.setAttribute("M1Num", M1Num);
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
		request.setAttribute("majorMW", majorMW);
		request.setAttribute("majorWW", majorWW);
		request.setAttribute("publicM", publicM);
		request.setAttribute("publicW", publicW);
		request.setAttribute("electiveM", electiveM);
		request.setAttribute("electiveW", electiveW);
		request.setAttribute("publicB", publicB);
		request.setAttribute("electiveB", electiveB);
		request.setAttribute("majorB", majorB);
		request.setAttribute("majorBW", majorBW);
		
		// 测试
		Map<String ,String> map=scoreDao.getAddressScoreNum1();
		System.out.println("吕梁"+map.get("吕梁"));
		Iterator it = map.entrySet().iterator();//取得map集合的中每个键值对所对应的iterator对象
		while(it.hasNext()){
		   String str = it.next().toString();
		   if(str.length()>1){
		    String s[] = str.split("=");
		    System.out.println(s[1]);//如果直接是System.out.println(str);则会打印出1=a 2=b,这样的效果。这里的s[1]只是为了得到map的值，而没有打印键
		   }
		}
		// 按地区比较成绩
		int LLNum=Integer.parseInt(map.get("吕梁"));
		int BJNum=Integer.parseInt(map.get("北京"));
		int HBNum=Integer.parseInt(map.get("河北"));
		int HNNum=Integer.parseInt(map.get("河南"));
		double LLS=scoreDao.getAddressScoreB("吕梁");
		double BJS=scoreDao.getAddressScoreB("北京");
		double HBS=scoreDao.getAddressScoreB("河北");
		double HNS=scoreDao.getAddressScoreB("河南");
		request.setAttribute("LLNum", LLNum);
		request.setAttribute("BJNum", BJNum);
		request.setAttribute("HBNum", HBNum);
		request.setAttribute("HNNum", HNNum);
		request.setAttribute("LLS", LLS);
		request.setAttribute("BJS", BJS);
		request.setAttribute("HBS", HBS);
		request.setAttribute("HNS", HNS);
		//测试结束
		try {
			request.getRequestDispatcher("view/count-address.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
