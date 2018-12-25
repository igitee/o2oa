package com.x.cms.assemble.control.jaxrs.categoryinfo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.cache.ApplicationCache;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.cms.assemble.control.service.LogService;
import com.x.cms.core.entity.AppInfo;
import com.x.cms.core.entity.CategoryInfo;
import com.x.cms.core.entity.Document;
import com.x.cms.core.entity.element.View;
import com.x.cms.core.entity.element.ViewCategory;


public class ActionSave extends BaseAction {

	private static  Logger logger = LoggerFactory.getLogger(ActionSave.class);

	protected ActionResult<Wo> execute(HttpServletRequest request, EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();
		String identityName = null;
		String unitName = null;
		String topUnitName = null;
		Wi wi = null;
		CategoryInfo categoryInfo = null;
		Boolean check = true;

		try {
			wi = this.convertToWrapIn(jsonElement, Wi.class);
			identityName = wi.getIdentity();
		} catch (Exception e) {
			check = false;
			Exception exception = new ExceptionCategoryInfoProcess(e,
					"系统在将JSON信息转换为对象时发生异常。JSON:" + jsonElement.toString());
			result.error(exception);
			logger.error(e, effectivePerson, request, null);
		}
		
		if (check) {
			if ( StringUtils.isEmpty( identityName ) && !"xadmin".equalsIgnoreCase(effectivePerson.getDistinguishedName())) {
				try {
					identityName = userManagerService.getPersonIdentity( effectivePerson.getDistinguishedName(), identityName);
				} catch (Exception e) {
					check = false;
					Exception exception = new ExceptionCategoryInfoProcess(e, "系统获取人员身份名称时发生异常，指定身份：" + identityName);
					result.error(exception);
					logger.error(e, effectivePerson, request, null);
				}
			} else {
				identityName = "xadmin";
				unitName = "xadmin";
				topUnitName = "xadmin";
			}
		}
		
		if( check ) {
			if (StringUtils.isEmpty( identityName )) {
				identityName = wi.getCreatorIdentity();
			}
		}
		
		if (check && !"xadmin".equals(identityName)) {
			try {
				unitName = userManagerService.getUnitNameByIdentity(identityName);
			} catch (Exception e) {
				check = false;
				Exception exception = new ExceptionCategoryInfoProcess(e,
						"系统在根据用户身份信息查询所属组织名称时发生异常。Identity:" + identityName);
				result.error(exception);
				logger.error(e, effectivePerson, request, null);
			}
		}
		
		if (check && !"xadmin".equals(identityName)) {
			try {
				topUnitName = userManagerService.getTopUnitNameByIdentity(identityName);
			} catch (Exception e) {
				check = false;
				Exception exception = new ExceptionCategoryInfoProcess(e,
						"系统在根据用户身份信息查询所属顶层组织名称时发生异常。Identity:" + identityName);
				result.error(exception);
				logger.error(e, effectivePerson, request, null);
			}
		}
		
		if (check) {
			if (wi.getDefaultViewId() != null && !wi.getDefaultViewId().isEmpty()) {
				try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
					View queryView = emc.find(wi.getDefaultViewId(), View.class);
					if (queryView != null) {
						wi.setDefaultViewName(queryView.getName());
					} else {
						check = false;
						Exception exception = new ExceptionQueryViewNotExists(wi.getDefaultViewId());
						result.error(exception);
					}
				}
			}
		}
		
		if (check) {
			wi.setCreatorIdentity(identityName);
			wi.setCreatorPerson(effectivePerson.getDistinguishedName());
			wi.setCreatorUnitName(unitName);
			wi.setCreatorTopUnitName(topUnitName);
			
			try {

				categoryInfo = Wi.copier.copy(wi);
				categoryInfo.setId(wi.getId());
				categoryInfo = categoryInfoServiceAdv.save(categoryInfo, wi.getExtContent(), effectivePerson);

				ApplicationCache.notify(AppInfo.class);
				ApplicationCache.notify(CategoryInfo.class);
				ApplicationCache.notify(ViewCategory.class);
				ApplicationCache.notify(Document.class);

				new LogService().log(null, effectivePerson.getDistinguishedName(),
						categoryInfo.getAppName() + "-" + categoryInfo.getCategoryName(), categoryInfo.getId(), "", "",
						"", "CATEGORY", "新增");
				Wo wo = new Wo();
				wo.setId(categoryInfo.getId());
				result.setData(wo);
			} catch (Exception e) {
				check = false;
				Exception exception = new ExceptionCategoryInfoProcess(e, "分类信息在保存时发生异常.");
				result.error(exception);
				logger.error(e, effectivePerson, request, null);
			}
		}
		return result;
	}

	public static class Wi extends CategoryInfo {

		private static final long serialVersionUID = -5076990764713538973L;

		public static List<String> Excludes = new ArrayList<>(JpaObject.FieldsUnmodify);

		public static WrapCopier<Wi, CategoryInfo> copier = WrapCopierFactory.wi(Wi.class, CategoryInfo.class, null,
				JpaObject.FieldsUnmodify);

		@FieldDescribe("用户保存的身份")
		private String identity = null;
		
		@FieldDescribe("扩展信息JSON内容")
		private String extContent = null;

		public String getIdentity() {
			return identity;
		}

		public void setIdentity(String identity) {
			this.identity = identity;
		}

		public String getExtContent() {
			return extContent;
		}

		public void setExtContent(String extContent) {
			this.extContent = extContent;
		}

	}

	public static class Wo extends WoId {

	}
}