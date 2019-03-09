package com.ischoolbar.programmer.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.ischoolbar.programmer.dao.ScoreDao;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.Score;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 
 * @author llq
 *学生成绩管理功能实现servlet
 */
public class ScoreServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6195661804275602785L;
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("toScoreListView".equals(method)){
			scoreList(request,response);
		}else if("AddScore".equals(method)){
			addScore(request,response);
		}else if("ScoreList".equals(method)){
			getScoreList(request,response);
		}else if("EditScore".equals(method)){
			editScore(request,response);
		}else if("DeleteScore".equals(method)){
			deleteScore(request,response);
		}else if("excelImport".equals(method)) {
			
			try {
				Import(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		else if("exportAccountData".equals(method)) {
			exportAccountData(request,response);
		}
		/*else if("excelExport".equals(method)) {
			excelExport(request,response);
		}*/
	}
	private void deleteScore(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String[] ids = request.getParameterValues("ids[]");
		String idStr = "";
		for(String id : ids){
			idStr += id + ",";
		}
		idStr = idStr.substring(0, idStr.length()-1);
		ScoreDao scoreDao = new ScoreDao();
		if(scoreDao.deleteScore(idStr)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				scoreDao.closeCon();
			}
		}
	}
	
	
	private void editScore(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String studentName = request.getParameter("name");
		System.out.println(studentName);
		int id = Integer.parseInt(request.getParameter("id"));
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		int clazzId = Integer.parseInt(request.getParameter("clazzid"));
		double score = Double.parseDouble(request.getParameter("score"));
		Score s = new Score();
		s.setScore(score);
		s.setStudentId(studentId);
		s.setStudentName(studentName);
		s.setId(id);
		s.setCourseId(courseId);
		s.setClazzId(clazzId);
		ScoreDao scoreDao = new ScoreDao();
		if(scoreDao.editScore(s)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				scoreDao.closeCon();
			}
		}
	}
	
	
	private void getScoreList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String studentName = request.getParameter("name");
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Integer courseId = request.getParameter("courseId") == null ? 0 : Integer.parseInt(request.getParameter("courseId"));
		Integer clazzId = request.getParameter("clazzid") == null ? 0 : Integer.parseInt(request.getParameter("clazzid"));
		
		Integer studentId = request.getParameter("studentId") == null ? 0 : Integer.parseInt(request.getParameter("studentId"));
		//获取当前登录用户类型
		// int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		Score score = new Score();
		score.setStudentName(studentName);
		score.setStudentId(studentId);
		score.setCourseId(courseId);
		score.setClazzId(clazzId);
		ScoreDao scoreDao = new ScoreDao();
		System.out.println(currentPage+" "+pageSize);
		List<Score> clazzList = scoreDao.getScoreList(score, new Page(currentPage, pageSize));
		int total = scoreDao.getScoreListTotal(score);
		scoreDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", clazzList);
	//	ret.put("courseList", courseList);
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
	
	/**
	 * 添加成绩
	 * @param request
	 * @param response
	 */
	private void addScore(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		double score = Double.parseDouble(request.getParameter("score"));
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		System.out.println(request.getParameter("studentId"));
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		int clazzId = Integer.parseInt(request.getParameter("clazzid"));
		Score s = new Score();
		s.setScore(score);
		s.setStudentId(studentId);
		s.setStudentName(name);
		s.setCourseId(courseId);
		s.setClazzId(clazzId);
		ScoreDao scoreDao = new ScoreDao();
		if(scoreDao.addScore(s)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				scoreDao.closeCon();
			}
		}
	}
	/**
	 * 跳转到分数页面
	 * @param request
	 * @param response
	 * @throws IOException
	 */

	private void scoreList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		try {
			System.out.println("进来啦！");
			request.getRequestDispatcher("view/score.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
   
    /**
     * 导入Excel到数据库
     * @param path
     * @return
     */
    public static void excelImport(HttpServletRequest req,
            HttpServletResponse resp)throws IOException {
    	
    	String path= req.getParameter("Path");
    //	System.out.println("path="+path);
   
        List<Score> list = new ArrayList<>();
        Workbook book = null;
        try {
        	System.out.println(path);
                // 获取Excel对象
               book = book.getWorkbook(new File(path));
        	
        //	book = book.getWorkbook(new File("C:\\\\Users\\\\Administrator\\\\Desktop\\\\text(8).xls"));
                // 获取Excel第一个选项卡对象
                Sheet sheet = book.getSheet(0);
                // 遍历选项卡，第一行是表头，所以索引数-1
                for (int i = 0; i < sheet.getRows() - 1; i++) {
                	    Score score = new Score();
                        // 获取第一列第二行单元格对象
                        Cell cell = sheet.getCell(0, i + 1);
     
                        score.setId(Integer.parseInt(cell.getContents()));
                     
                        // 获取第二行其他数据
                        score.setStudentId(Integer.parseInt(sheet.getCell(1, i + 1).getContents()));

                        score.setStudentName(sheet.getCell(2, i + 1).getContents());
                        score.setCourseId(Integer.parseInt(sheet.getCell(3, i + 1).getContents()));
                        score.setClazzId(Integer.parseInt(sheet.getCell(4, i + 1).getContents()));
                        score.setScore(Double.parseDouble(sheet.getCell(5, i + 1).getContents()));
                       
                       
                        list.add(score);
                }
            
                // 批量插入数据库
                ScoreDao scoreDao=new ScoreDao();
                scoreDao.InsertScoreList(list);
                
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
                try {
                        // 关闭
                        book.close();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
    }
    
    
    /**
     * excel导出
     * @param req
     * @param resp
     * void
     * @throws IOException 
     */
    private void exportAccountData(HttpServletRequest req,
            HttpServletResponse resp) throws IOException {
    
      //  String sql=" select id,score,student_id,course_id,student_name,clazz_id,`from` s_score";
    	String sql="select * from s_score";
        System.out.println(sql);
       // User user=(User) req.getSession().getAttribute("user");
        ScoreDao scoreDao=new ScoreDao();
        List<Score> list= scoreDao.ScoreExcel();
        
        if(null!=list&&list.size()>0){
            
            String fileName="测试数据.xls";
            resp.setHeader("Content-disposition","attachment;filename="
                +new String(fileName.getBytes("gb2312"),"ISO8859-1"));    //设置文件头编码格式        
            resp.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");//设置类型
            resp.setHeader("Cache-Control","no-cache");//设置头
            resp.setDateHeader("Expires", 0);//设置日期头
            
            HSSFWorkbook book=new HSSFWorkbook();
            HSSFSheet sheet=book.createSheet();
            CellStyle cellStyle=book.createCellStyle(); 
            // 设置表头居中
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            
            cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
            // 设置表头
            Score s=list.get(0);
            Row r=sheet.createRow(0);
        	org.apache.poi.ss.usermodel.Cell cell11= r.createCell(0);
            ((HSSFCell) cell11).setCellValue("编号");
            org.apache.poi.ss.usermodel.Cell cell22=r.createCell(1);
            ((HSSFCell) cell22).setCellValue("学号");
            org.apache.poi.ss.usermodel.Cell cell33=r.createCell(2);
            ((HSSFCell) cell33).setCellValue("姓名");
            org.apache.poi.ss.usermodel.Cell cell44=r.createCell(3);
            ((HSSFCell) cell44).setCellValue("课程编号");
            org.apache.poi.ss.usermodel.Cell cell55= r.createCell(4);
            ((HSSFCell) cell55).setCellValue("班级编号");
            org.apache.poi.ss.usermodel.Cell cell66= r.createCell(5);
            ((HSSFCell) cell66).setCellValue("成绩");
            
            for(int i=0;i<list.size();i++){
                Score score=list.get(i);
                Row row=sheet.createRow(i+1);
	        	 org.apache.poi.ss.usermodel.Cell cell1= row.createCell(0);
	             ((HSSFCell) cell1).setCellValue(score.getId());
	             org.apache.poi.ss.usermodel.Cell cell2=row.createCell(1);
	             ((HSSFCell) cell2).setCellValue(score.getStudentId());
	             org.apache.poi.ss.usermodel.Cell cell3=row.createCell(2);
	             ((HSSFCell) cell3).setCellValue(score.getStudentName());
	             org.apache.poi.ss.usermodel.Cell cell4=row.createCell(3);
	             ((HSSFCell) cell4).setCellValue(score.getCourseId());
	             org.apache.poi.ss.usermodel.Cell cell5= row.createCell(4);
	             ((HSSFCell) cell5).setCellValue(score.getClazzId());
	             org.apache.poi.ss.usermodel.Cell cell6= row.createCell(5);
	             ((HSSFCell) cell6).setCellValue(score.getScore());
            }        
            book.write(resp.getOutputStream());
            resp.getOutputStream().flush();
            resp.getOutputStream().close();
        }
    	resp.getWriter().write("success");
        
    }
    
    
    private ServletConfig config;
    public void init(ServletConfig config)throws ServletException {
    	super.init(config);
    	this.config=config;
    }
    final public ServletConfig getServletConfig() {
    	return config;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    private boolean isMultipart;
    private int maxFileSize = 1024 * 1024 * 10;
    private int maxMemSize = 100 * 1024;

    private void Import(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查是否有一个文件上传请求
    	
    	isMultipart = ServletFileUpload.isMultipartContent(request);
        String result = "";
        response.setContentType("text/html;charset=utf-8");
        if (!isMultipart) {
            result = "未获取到文件";
            response.getWriter().println(result);
            return;
        }
        // 工厂模式
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 文件大小的最大值将被存储在内存中
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        String path = getServletContext().getRealPath("/") + "/";
        System.out.println(path);
        factory.setRepository(new File(path));
        // System.out.println(path);
        // 创建一个新的文件上传处理程序
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 允许上传的文件大小的最大值
        upload.setSizeMax(maxFileSize);
        FileItem file1=null;
        try {
            // 解析请求，获取文件项
            List fileItems = upload.parseRequest(request);
            // 处理上传的文件项
            Iterator i = fileItems.iterator();
            System.out.println(i.hasNext());
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    // 获取上传文件的参数
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    // 写入文件
                    File file = new File(path  + ".xsl");
                    System.out.println(path);
                    fi.write(file);
                 
                    file1=fi;
                }
                System.out.println(fi.getFieldName());
            }
            result = "上传成功";
         
        } catch (Exception ex) {
            System.out.println("ex:" + ex.getMessage());
            result = "上传失败";
        }
        

        /*
        
        List<Score> list = new ArrayList<>();
        Workbook book=null;
		try {
			book = book.getWorkbook(new File(path.xsl));
		} catch (BiffException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
                // 获取Excel对象
               // book = book.getWorkbook(new File(path));
        	
        //	book = book.getWorkbook(new File("C:\\\\Users\\\\Administrator\\\\Desktop\\\\text(8).xls"));
                // 获取Excel第一个选项卡对象
                Sheet sheet = book.getSheet(0);
                // 遍历选项卡，第一行是表头，所以索引数-1
                for (int i = 0; i < sheet.getRows() - 1; i++) {
                	    Score score = new Score();
                        // 获取第一列第二行单元格对象
                        Cell cell = sheet.getCell(0, i + 1);
     
                        score.setId(Integer.parseInt(cell.getContents()));
                     
                        // 获取第二行其他数据
                        score.setStudentId(Integer.parseInt(sheet.getCell(1, i + 1).getContents()));

                        score.setStudentName(sheet.getCell(2, i + 1).getContents());
                        score.setCourseId(Integer.parseInt(sheet.getCell(3, i + 1).getContents()));
                        score.setClazzId(Integer.parseInt(sheet.getCell(4, i + 1).getContents()));
                        score.setScore(Double.parseDouble(sheet.getCell(5, i + 1).getContents()));
                       
                       
                        list.add(score);
                }
            
                // 批量插入数据库
                ScoreDao scoreDao=new ScoreDao();
                scoreDao.InsertScoreList(list);
                
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
                try {
                        // 关闭
                        book.close();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }*/

        response.getWriter().println(result);
    }
	
}
