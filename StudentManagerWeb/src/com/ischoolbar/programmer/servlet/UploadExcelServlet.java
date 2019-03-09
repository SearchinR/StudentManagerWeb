package com.ischoolbar.programmer.servlet;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
  
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
  
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Sheet;

import com.ischoolbar.programmer.dao.ScoreDao;
import com.ischoolbar.programmer.model.Score;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
 
 
 
 
/**
 * Servlet implementation class UpLoadExcelServlet
 */
 
public class UploadExcelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
        
    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadExcelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
 
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	doPost(request, response);
    	
    	
    }
 
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	  // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }
 
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
 
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        
        // 中文处理
        upload.setHeaderEncoding("UTF-8"); 

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = getServletContext().getRealPath("/") + File.separator + UPLOAD_DIRECTORY;
       
         
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        // 自定义，找文件的目录
        String myPath="";
        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
 
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        System.out.println(filePath);
                        myPath=filePath;
                        // 保存文件到硬盘
                        item.write(storeFile);
                        request.setAttribute("message",
                            "文件上传成功!");
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message",
                    "错误信息: " + ex.getMessage());
        }
        // 跳转到 message.jsp
//        getServletContext().getRequestDispatcher("/message.jsp").forward(
//                request, response);

        List<Score> list = new ArrayList<>();
        Workbook book=null;
		try {
			book = book.getWorkbook(new File(myPath));
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
        }
        
           
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>\n");
        out.println("<head><title>PageTitle</title></head>");
        out.println("<body>");
        out.println("<p>导入成功</p>");
        out.println("</body>");
  
    }
    
}