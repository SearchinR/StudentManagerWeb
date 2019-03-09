package com.ischoolbar.programmer.util;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportExcelUtil {
	
	private final static String Excel_2003 = ".xls"; //2003 �汾��excel
	private final static String Excel_2007 = ".xlsx"; //2007 �汾��excel
	
	/**
	 * @param in
	 * @param fileName
	 * @param columNum �Զ�������
	 * @return
	 * */
	public List<List<Object>> getBankListByExcel(InputStream in,String fileName) throws Exception{
		List<List<Object>> list = null;
		
		//����Excel������
		Workbook work = this.getWorkbook(in, fileName);
		if(work == null) {
			throw new Exception("����Excel������Ϊ�գ�");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		list = new ArrayList<List<Object>>();
		//����Excel�е�����sheet
		for(int i = 0; i<work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if(sheet == null) {continue;}
			//������ǰsheet�е�������
			//int totalRow = sheet.getPhysicalNumberOfRows();//���excel�и�ʽ�����ַ�ʽȡֵ��׼ȷ
			int totalRow = sheet.getPhysicalNumberOfRows();
			for(int j = sheet.getFirstRowNum(); j<totalRow; j++) {
				row = sheet.getRow(j);
				if(!isRowEmpty(row)) {
					//if(row != null && !"".equals(row)) {
					//��ȡ��һ����Ԫ��������Ƿ����
					Cell fristCell=row.getCell(0);
					if(fristCell!=null){
						//�������е���
						List<Object> li = new ArrayList<Object>();
						//int totalColum = row.getLastCellNum();
						for(int y = row.getFirstCellNum(); y<row.getLastCellNum(); y++) {
							cell = row.getCell(y);
							String callCal = this.getCellValue(cell)+"";
							li.add(callCal);
						}
						list.add(li);
					}
 
				}else if(isRowEmpty(row)){
					continue;
				}
				
			}
		}
		in.close();
		return list;
	}
	/**
	 * �ж����Ƿ�Ϊ��
	 * @param row
	 * @return
	 */
	public static boolean isRowEmpty(Row row) {
		   for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
		       Cell cell = row.getCell(c);
		       if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
		           return false;
		   }
		   return true;
		}
	/**
	 * �����������ļ���׺���Զ���Ӧ�ϴ��ļ��İ汾
	 * @param inStr,fileName
	 * @return
	 * @throws Exception
	 * */
	public Workbook getWorkbook(InputStream inStr,String fileName) throws Exception {
		Workbook work = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if(Excel_2003.equals(fileType)){
			work=new HSSFWorkbook(inStr);//2003 �汾��excel
		}else if(Excel_2007.equals(fileType)) {
			work=new XSSFWorkbook(inStr);//2007 �汾��excel
		}else {
			throw new Exception("�����ļ���ʽ����");
		}
		return work;
	}
	
	/**
	 * �������Ա������ֵ���и�ʽ��
	 * @param cell
	 * @return
	 * */
	public Object getCellValue(Cell cell) {
		/*Object value = null;
		DecimalFormat df1 = new DecimalFormat("0.00");//��ʽ��number��string�ַ�
		SimpleDateFormat sdf = new  SimpleDateFormat("yyy-MM-dd");//���ڸ�ʽ��
		DecimalFormat df2 = new DecimalFormat("0.00");//��ʽ������
		if(cell !=null && !"".equals(cell)) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				value = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if("General".equals(cell.getCellStyle().getDataFormatString())) {
					value = df1.format(cell.getNumericCellValue());
				}else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
					value = sdf.format(cell.getDateCellValue());
				}else if(HSSFDateUtil.isCellDateFormatted(cell)){
					Date date = cell.getDateCellValue();
					value = sdf.format(date);				
				}
				else {
					value = df2.format(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				value = "";
				break;
			default:
				break;
			}
		}		
		return value;*/
		String result = new String();  
        switch (cell.getCellType()) {  
        case HSSFCell.CELL_TYPE_FORMULA:  //Excel��ʽ
            try {  
            	result = String.valueOf(cell.getNumericCellValue());  
            } catch (IllegalStateException e) {  
            	result = String.valueOf(cell.getRichStringCellValue());
            }  
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:// ��������  
            if (HSSFDateUtil.isCellDateFormatted(cell)) {// �������ڸ�ʽ��ʱ���ʽ  
                SimpleDateFormat sdf;  
                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
                        .getBuiltinFormat("h:mm")) {  
                    sdf = new SimpleDateFormat("HH:mm");  
                } else {// ����  
                    sdf = new SimpleDateFormat("yyyy-MM-dd");  
                }  
                Date date = cell.getDateCellValue();  
                result = sdf.format(date);  
            } else if (cell.getCellStyle().getDataFormat() == 58) {  
                // �����Զ������ڸ�ʽ��m��d��(ͨ���жϵ�Ԫ��ĸ�ʽid�����id��ֵ��58)  
                SimpleDateFormat sdf = new SimpleDateFormat("M��d��");  
                double value = cell.getNumericCellValue();  
                Date date = org.apache.poi.ss.usermodel.DateUtil  
                        .getJavaDate(value);  
                result = sdf.format(date);  
            } else {  
                double value = cell.getNumericCellValue();  
                CellStyle style = cell.getCellStyle();  
                DecimalFormat format = new DecimalFormat();  
                String temp = style.getDataFormatString();  
                // ��Ԫ�����óɳ���  
                if (temp.equals("General")) {  
                    format.applyPattern("#.##");  
                }  
                result = format.format(value);  
            }  
            break;  
        case HSSFCell.CELL_TYPE_STRING:// String����  
            result = cell.getRichStringCellValue().toString();  
            break;  
        case HSSFCell.CELL_TYPE_BLANK:  
            result = "";  
        default:  
            result = "";  
            break;  
        }  
        return result;  
	}
	
	public String getFormat(String str) {
		if(str.equals("null")) {
			str="";
			return str;
		}else{
			return str;
		}	
	}
	public Integer getFormats(Integer str) {
		if(str==null) {
			str=0;
			return str;
		}else{
			return str;
		}	
	}
	
	/**
	 * ��ȡ�ַ����е����ֶ����š����ֽ��ȣ����"USD 374.69"�л�ȡ��374.69���ӡ����׵��ţ�66666666666����ȡ��66666666666
	 * @param receiptAmountString
	 * @return
	 */
	public static String getFormatNumber(String str){
		str = str.trim();
		 Pattern p = Pattern.compile("[0-9]");
		 int indexNum = 0;
		 int lenght = str.length();
		 String num = "";
		 for(int i=0;i<lenght;i++){
			num += str.charAt(i);
		 	Matcher m = p.matcher(num);
		 	if(m.find()){
		 		indexNum = i;
		 		break;
		 	}
		  }
		 String formatNumber = str.substring(indexNum,lenght);
		 return formatNumber;
	}
}
