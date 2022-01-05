package com.rsn.POMS.api.dictionary.advanced.categorywaiting.component;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;


/**
 * @create @author 정수빈 @Date 2021. 12. 10
 * 
 * 패턴 업로드 양식 다운로드
 */
@Component("pattern.upload.sample")
public class UploadExcelSample extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		createExcel(workbook, model);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode("Lucy2.0_패턴_업로드_샘플.xlsx", "UTF-8") + "\";");

	}

	private final String[] title = new String[] { "분류 정보", "패턴 타입", "패턴", "패턴식", "언어" };

	private void createExcel(Workbook workbook, Map<String, Object> model) {
		Sheet sheet = workbook.createSheet("패턴 업로드 양식");

		Row row = null;
		Cell cell = null;
		int rowIndex = 0;

		ExcelStyle style = new ExcelStyle();

		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle = style.getTitlestyle(titleStyle);
 
		row = sheet.createRow(rowIndex);

		for (int i = 0; i < title.length; i++) {
			sheet.setColumnWidth(i, 3500);
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(titleStyle);
		}
	}
}
