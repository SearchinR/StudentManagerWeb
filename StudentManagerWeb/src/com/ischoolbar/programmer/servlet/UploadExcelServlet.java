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
        
    // �ϴ��ļ��洢Ŀ¼
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // �ϴ�����
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
    	
    	  // ����Ƿ�Ϊ��ý���ϴ�
        if (!ServletFileUpload.isMultipartContent(request)) {
            // ���������ֹͣ
            PrintWriter writer = response.getWriter();
            writer.println("Error: ��������� enctype=multipart/form-data");
            writer.flush();
            return;
        }
 
        // �����ϴ�����
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // �����ڴ��ٽ�ֵ - �����󽫲�����ʱ�ļ����洢����ʱĿ¼��
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // ������ʱ�洢Ŀ¼
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
 
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // ��������ļ��ϴ�ֵ
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // �����������ֵ (�����ļ��ͱ�����)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        
        // ���Ĵ���
        upload.setHeaderEncoding("UTF-8"); 

        // ������ʱ·�����洢�ϴ����ļ�
        // ���·����Ե�ǰӦ�õ�Ŀ¼
        String uploadPath = getServletContext().getRealPath("/") + File.separator + UPLOAD_DIRECTORY;
       
         
        // ���Ŀ¼�������򴴽�
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        // �Զ��壬���ļ���Ŀ¼
        String myPath="";
        try {
            // ���������������ȡ�ļ�����
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
 
            if (formItems != null && formItems.size() > 0) {
                // ����������
                for (FileItem item : formItems) {
                    // �����ڱ��е��ֶ�
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // �ڿ���̨����ļ����ϴ�·��
                        System.out.println(filePath);
                        myPath=filePath;
                        // �����ļ���Ӳ��
                        item.write(storeFile);
                        request.setAttribute("message",
                            "�ļ��ϴ��ɹ�!");
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message",
                    "������Ϣ: " + ex.getMessage());
        }
        // ��ת�� message.jsp
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
                // ��ȡExcel����
               // book = book.getWorkbook(new File(path));
        	
        //	book = book.getWorkbook(new File("C:\\\\Users\\\\Administrator\\\\Desktop\\\\text(8).xls"));
                // ��ȡExcel��һ��ѡ�����
                Sheet sheet = book.getSheet(0);
                // ����ѡ�����һ���Ǳ�ͷ������������-1
                for (int i = 0; i < sheet.getRows() - 1; i++) {
                	    Score score = new Score();
                        // ��ȡ��һ�еڶ��е�Ԫ�����
                        Cell cell = sheet.getCell(0, i + 1);
     
                        score.setId(Integer.parseInt(cell.getContents()));
                     
                        // ��ȡ�ڶ�����������
                        score.setStudentId(Integer.parseInt(sheet.getCell(1, i + 1).getContents()));

                        score.setStudentName(sheet.getCell(2, i + 1).getContents());
                        score.setCourseId(Integer.parseInt(sheet.getCell(3, i + 1).getContents()));
                        score.setClazzId(Integer.parseInt(sheet.getCell(4, i + 1).getContents()));
                        score.setScore(Double.parseDouble(sheet.getCell(5, i + 1).getContents()));
                       
                       
                        list.add(score);
                }
            
                // �����������ݿ�
                ScoreDao scoreDao=new ScoreDao();
                scoreDao.InsertScoreList(list);
                
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
                try {
                        // �ر�
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
        out.println("<p>����ɹ�</p>");
        out.println("</body>");
  
    }
    
}