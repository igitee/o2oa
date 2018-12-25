package com.x.cms.assemble.control.jaxrs.input;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.dataitem.DataItemConverter;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.cms.assemble.control.Business;
import com.x.cms.core.entity.AppInfo;
import com.x.cms.core.entity.CategoryInfo;
import com.x.cms.core.entity.element.AppDict;
import com.x.cms.core.entity.element.AppDictItem;
import com.x.cms.core.entity.element.Form;
import com.x.cms.core.entity.element.Script;
import com.x.cms.core.entity.element.wrap.WrapAppDict;
import com.x.cms.core.entity.element.wrap.WrapCategoryInfo;
import com.x.cms.core.entity.element.wrap.WrapCms;
import com.x.cms.core.entity.element.wrap.WrapForm;
import com.x.cms.core.entity.element.wrap.WrapScript;

class ActionCreate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		logger.debug(effectivePerson, "jsonElement:{}.", jsonElement);
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wo wo = new Wo();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			AppInfo appInfo = this.create(business, wi);
			wo.setId(appInfo.getId());
			result.setData(wo);
			return result;
		}
	}

	private AppInfo create(Business business, Wi wi) throws Exception {
		List<JpaObject> persistObjects = new ArrayList<>();
		AppInfo appInfo = business.entityManagerContainer().find(wi.getId(), AppInfo.class);
		if (null != appInfo) {
			throw new ExceptionAppInfoExist(wi.getId());
		}
		appInfo = WrapCms.inCopier.copy(wi);
		appInfo.setAppName(this.idleAppInfoName(business, appInfo.getAppName(), appInfo.getId()));
		appInfo.setAppAlias(this.idleAppInfoAlias(business, appInfo.getAppAlias(), appInfo.getId()));
		persistObjects.add(appInfo);
		for (WrapForm _o : wi.getFormList()) {
			Form obj = business.entityManagerContainer().find(_o.getId(), Form.class);
			if (null != obj) {
				throw new ExceptionEntityExistForCreate(_o.getId(), Form.class);
			}
			obj = WrapForm.inCopier.copy(_o);
			obj.setAppId(appInfo.getId());
			persistObjects.add(obj);
		}
		for (WrapScript _o : wi.getScriptList()) {
			Script obj = business.entityManagerContainer().find(_o.getId(), Script.class);
			if (null != obj) {
				throw new ExceptionEntityExistForCreate(_o.getId(), Script.class);
			}
			obj = WrapScript.inCopier.copy(_o);
			obj.setAppId(appInfo.getId());
			persistObjects.add(obj);
		}
		
		for (WrapAppDict _o : wi.getAppDictList()) {
			AppDict obj = business.entityManagerContainer().find(_o.getId(), AppDict.class);
			if (null != obj) {
				throw new ExceptionEntityExistForCreate(_o.getId(), AppDict.class);
			}
			obj = WrapAppDict.inCopier.copy(_o);
			obj.setAppId(appInfo.getId());
			persistObjects.add(obj);
			DataItemConverter<AppDictItem> converter = new DataItemConverter<>(AppDictItem.class);
			List<AppDictItem> list = converter.disassemble(_o.getData());
			for (AppDictItem o : list) {
				o.setBundle(obj.getId());
				/** 将数据字典和数据存放在同一个分区 */
				o.setDistributeFactor(obj.getDistributeFactor());
				o.setAppId(obj.getAppId());
				persistObjects.add(o);
			}
		}
		
		for (WrapCategoryInfo wrapCategoryInfo : wi.getCategoryInfoList() ) {
			CategoryInfo categoryInfo = business.entityManagerContainer().find(wrapCategoryInfo.getId(), CategoryInfo.class);
			if (null != categoryInfo) {
				throw new ExceptionEntityExistForCreate(wrapCategoryInfo.getId(), CategoryInfo.class);
			}
			categoryInfo = WrapCategoryInfo.inCopier.copy(wrapCategoryInfo);
			categoryInfo.setAppId(appInfo.getId());
			persistObjects.add(categoryInfo);
		}
		business.entityManagerContainer().beginTransaction(AppInfo.class);
		business.entityManagerContainer().beginTransaction(Script.class);
		business.entityManagerContainer().beginTransaction(Form.class);
		business.entityManagerContainer().beginTransaction(AppDict.class);
		business.entityManagerContainer().beginTransaction(AppDictItem.class);
		business.entityManagerContainer().beginTransaction(CategoryInfo.class);
		for (JpaObject o : persistObjects) {
			business.entityManagerContainer().persist(o);
		}
		business.entityManagerContainer().commit();
		return appInfo;
	}

	public static class Wi extends WrapCms {
		private static final long serialVersionUID = -4612391443319365035L;
	}

	public static class Wo extends WoId {

	}
}