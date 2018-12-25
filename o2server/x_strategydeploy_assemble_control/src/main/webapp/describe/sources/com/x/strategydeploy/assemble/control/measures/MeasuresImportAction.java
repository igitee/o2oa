package com.x.strategydeploy.assemble.control.measures;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.annotation.JaxrsDescribe;
import com.x.base.core.project.annotation.JaxrsMethodDescribe;
import com.x.base.core.project.annotation.JaxrsParameterDescribe;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.ResponseFactory;
import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.StringTools;
import com.x.organization.core.express.unit.UnitFactory;
import com.x.strategydeploy.assemble.control.Business;
import com.x.strategydeploy.assemble.control.measures.tools.NumberValidationUtils;
import com.x.strategydeploy.assemble.control.measures.tools.VerifySequenceNumberTools;
import com.x.strategydeploy.core.entity.MeasuresInfo;

@Path("measuresimport")
@JaxrsDescribe("战略管理，战略信息配置导入")
public class MeasuresImportAction extends StandardJaxrsAction {
	private static Logger logger = LoggerFactory.getLogger(MeasuresImportAction.class);
	private static String DEPT_SEPARATOR = "、";

	@JaxrsMethodDescribe(value = "上传Excel导入战略管理.", action = StandardJaxrsAction.class)
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("measures")
	public void input(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request, @FormDataParam(FILE_FIELD) final byte[] bytes, @JaxrsParameterDescribe("Excel文件") @FormDataParam(FILE_FIELD) final FormDataContentDisposition disposition,
			@JaxrsParameterDescribe("年份") @FormDataParam("year") String year, @JaxrsParameterDescribe("公司工作重点id") @FormDataParam("parentid") String parentid, @JaxrsParameterDescribe("sheet页的序号") @FormDataParam("sheetsequence") String sheetsequence) {
		ActionResult<ActionImportExcelXLSX.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		//校验sheetsequence
		boolean isPositiveInteger = NumberValidationUtils.isPositiveInteger(sheetsequence);
		if (!isPositiveInteger) {

		}

		//年份格式校验
		boolean isPass = true;
		String eL = "[1-9]{1}[0-9]{3}";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(year);
		isPass = m.matches();

		/*		try {
					checkExcelData(bytes, disposition, sheetsequence);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					result.error(e1);
					logger.error(e1, effectivePerson, request, null);
				}
				logger.info("checkExcelData over ");
				isPass = false;
		*/

		if (isPass) {
			try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
				//判断id对应的公司工作重点是否存在。
				Business business = new Business(emc);
				boolean _IsExist = business.strategyDeployFactory().IsExistById(parentid);
				if (!_IsExist) {
					isPass = false;
					Exception e = new Exception("Can not find the work with id:" + parentid);
					result.error(e);
					logger.error(e, effectivePerson, request, null);
				}
			} catch (Exception e) {
				isPass = false;
				logger.error(e, effectivePerson, request, null);
				result.error(e);
			}
		} else {
			isPass = false;
			Exception e = new Exception("Please Input Correct date format.eg:2018");
			result.error(e);
			logger.error(e, effectivePerson, request, null);
		}

		try {
			result = new ActionImportExcelXLSX().execute(effectivePerson, bytes, disposition, year, parentid,sheetsequence);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}

		asyncResponse.resume(ResponseFactory.getDefaultActionResultResponse(result));
	}

	@JaxrsMethodDescribe(value = "校验上传Excel导入战略管理.", action = StandardJaxrsAction.class)
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("inputchek")
	public void inputcheck(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request, @FormDataParam(FILE_FIELD) final byte[] bytes, @JaxrsParameterDescribe("Excel文件") @FormDataParam(FILE_FIELD) final FormDataContentDisposition disposition,
			@JaxrsParameterDescribe("年份") @FormDataParam("year") String year, @JaxrsParameterDescribe("公司工作重点id") @FormDataParam("parentid") String parentid, @JaxrsParameterDescribe("sheet页的序号") @FormDataParam("sheetsequence") String sheetsequence) {
		ActionResult<ActionImportExcelXLSX.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		//校验sheetsequence
		boolean isPositiveInteger = NumberValidationUtils.isPositiveInteger(sheetsequence);
		if (!isPositiveInteger) {

		}

		//年份格式校验
		boolean isPass = true;
		String eL = "[1-9]{1}[0-9]{3}";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(year);
		isPass = m.matches();

		try {
			checkExcelData(bytes, disposition, sheetsequence);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.error(e);
			logger.error(e, effectivePerson, request, null);
		}
		logger.info("checkExcelData over ");
		//isPass = false;
		asyncResponse.resume(ResponseFactory.getDefaultActionResultResponse(result));
	}

	@JaxrsMethodDescribe(value = "获取导入结果.", action = StandardJaxrsAction.class)
	@GET
	@Path("result/flag/{flag}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getResult(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request, @JaxrsParameterDescribe("导入文件返回的结果标记") @PathParam("flag") String flag) {
		ActionResult<ActionGetCheckResult.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionGetCheckResult().execute(effectivePerson, flag);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getDefaultActionResultResponse(result));
	}
	
	public boolean checkExcelData(byte[] bytes, FormDataContentDisposition disposition, String sheetsequence) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create(); InputStream is = new ByteArrayInputStream(bytes); XSSFWorkbook workbook = new XSSFWorkbook(is); ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			Business business = new Business(emc);
			int rowIndexNumber = 0;
			int columnIndexNumber = 0;
			List<String> SequenceNumberList = new ArrayList<String>();
			boolean isRightNumber = true;

			String measuresTitle = "";
			String workContent = "";
			String targetValue = "";
			String deptStringList = "";

			logger.info("getNumberOfSheets:" + workbook.getNumberOfSheets());
			//int numberOfSheets = workbook.getNumberOfSheets();
			int numberOfSheets = Integer.parseInt(sheetsequence);
			numberOfSheets--;
			if (numberOfSheets >= 0) {
				Sheet sheet = workbook.getSheetAt(numberOfSheets);
				logger.info("-----------------sheet.getSheetName():::" + sheet.getSheetName());
				int lastRowNum = sheet.getLastRowNum();
				int startLine = 3;
				Row row = null;
				for (int i = startLine; i <= lastRowNum; i++) {
					row = sheet.getRow(i);
					logger.info("---------------------------row number:" + i);
					MeasuresInfo measuresinfo = new MeasuresInfo();
					for (Cell cell : row) {
						int ColumnIndex = cell.getColumnIndex();
						logger.info("第:" + (i + 1) + "行，序号校验");
						if (ColumnIndex == 0) {
							//序号校验
							logger.info("第:" + (i + 1) + "行，序号:" + this.getCellValue(cell));
							isRightNumber = VerifySequenceNumberTools.VerifySequenceNumber(this.getCellValue(cell));
							if (!isRightNumber) {
								throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，序号不符合格式.");
							} else {
								if (SequenceNumberList.indexOf(this.getCellValue(cell)) >= 0) {
									throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，序号重复.");
								} else {
									SequenceNumberList.add(this.getCellValue(cell));
								}
							}

						}
						logger.info("第:" + (i + 1) + "行，工作内容，校验。");

						if (ColumnIndex == 1) {
							//关键举措校验
							measuresTitle = this.getCellValue(cell);
							if (null == StringUtils.trimToNull(measuresTitle)) {
								throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，关键举措为空.");
							}
						}

						if (ColumnIndex == 2) {
							//工作内容校验
							workContent = this.getCellValue(cell);
							if (null == StringUtils.trimToNull(workContent)) {
								throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，工作内容为空.");
							}
						}
						if (ColumnIndex == 3) {
							//目标值校验
							targetValue = this.getCellValue(cell);
							if (null == StringUtils.trimToNull(targetValue)) {
								throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，目标值为空.");
							}
						}
						if (ColumnIndex == 4) {
							//牵头部门
							deptStringList = this.getCellValue(cell);
							if (StringUtils.isEmpty(StringUtils.trim(deptStringList))) {
								//牵头部门，如果字段为空，就什么都不做。
							} else {
								List<String> deptlist = new ArrayList<String>();
								deptlist.addAll(Arrays.asList(deptStringList.split(DEPT_SEPARATOR)));
								for (String deptstring : deptlist) {
									deptstring = StringUtils.trim(deptstring);
									if (!checkUnitByUnitName(business, deptstring)) {
										throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，牵头部门中的:" + deptstring + "名称与组织名称不对相应。");
									}
								}
							}
						}

						if (ColumnIndex == 5) {
							//责任部门
							deptStringList = this.getCellValue(cell);
							if (StringUtils.isEmpty(StringUtils.trim(deptStringList))) {
								throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，责任部门，不能为空。");
							} else {
								List<String> deptlist = new ArrayList<String>();
								deptlist.addAll(Arrays.asList(deptStringList.split(DEPT_SEPARATOR)));
								for (String deptstring : deptlist) {
									deptstring = StringUtils.trim(deptstring);
									if (!checkUnitByUnitName(business, deptstring)) {
										throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，责任部门中的:" + deptstring + "名称与组织名称不对相应。");
									}
								}
							}
						}

						if (ColumnIndex == 6) {
							//支撑部门
							deptStringList = this.getCellValue(cell);
							if (StringUtils.isEmpty(StringUtils.trim(deptStringList))) {
								//支撑部门，如果字段为空，就什么都不做。
							} else {
								List<String> deptlist = new ArrayList<String>();
								deptlist.addAll(Arrays.asList(deptStringList.split(DEPT_SEPARATOR)));
								for (String deptstring : deptlist) {
									deptstring = StringUtils.trim(deptstring);
									if (!checkUnitByUnitName(business, deptstring)) {
										throw new Exception("第:" + (i + 1) + "行，第:" + ColumnIndex + "列，支撑部门中的:" + deptstring + "名称与组织名称不对相应。");
									}
								}
							}
						}

					}
				}
			}

			//			String flag = StringTools.uniqueToken();
			//			Wo wo = new Wo();
			//			wo.setFlag(flag);
		}
		return true;
	}

	public boolean checkUnitByUnitName(Business business, String unitName) throws Exception {
		UnitFactory unitFactory = business.organization().unit();
		//获取unitName的distinguishedName
		String distinguishedName = "";
		distinguishedName = unitFactory.get(unitName);
		logger.info("distinguishedName:" + distinguishedName);
		if (StringUtils.isEmpty(distinguishedName)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return cell.getCellFormula();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		}
		return "";
	}

}
