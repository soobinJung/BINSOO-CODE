package com.rsn.POMS.api.dictionary.advanced.categorywaiting.component;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author 정수빈
 * 2021 05 12
 * 
 * Excel 공통적인 Excel Style 지정.
 */
public class ExcelStyle {

	public CellStyle getTitlestyle(CellStyle titleStyle) {

		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);
		titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);

		return titleStyle;
	}
}