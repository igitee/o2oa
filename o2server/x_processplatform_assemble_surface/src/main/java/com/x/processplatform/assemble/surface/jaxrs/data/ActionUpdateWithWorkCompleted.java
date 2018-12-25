package com.x.processplatform.assemble.surface.jaxrs.data;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.processplatform.assemble.surface.Business;
import com.x.processplatform.assemble.surface.WorkControl;
import com.x.processplatform.core.entity.content.WorkCompleted;
import com.x.processplatform.core.entity.element.Application;
import com.x.processplatform.core.entity.element.Process;

class ActionUpdateWithWorkCompleted extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			/** 防止提交空数据清空data */
			if (jsonElement.isJsonNull()) {
				throw new ExceptionNullData();
			}
			if (jsonElement.isJsonArray()) {
				throw new ExceptionArrayData();
			}
			if (jsonElement.isJsonPrimitive()) {
				throw new ExceptionPrimitiveData();
			}
			if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().entrySet().isEmpty()) {
				throw new ExceptionEmptyData();
			}
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			WorkCompleted workCompleted = emc.find(id, WorkCompleted.class);
			if (null == workCompleted) {
				throw new ExceptionEntityNotExist(id, WorkCompleted.class);
			}
			/** 允许创建者在完成后再次修改内容,与前台的可修改不一致,所以单独判断,为的是不影响前台显示. */
			Application application = business.application().pick(workCompleted.getApplication());
			Process process = business.process().pick(workCompleted.getProcess());
			if (!business.canManageApplicationOrProcess(effectivePerson, application, process)
					&& (!effectivePerson.isUser(workCompleted.getCreatorPerson()))) {
				throw new ExceptionWorkCompletedAccessDenied(effectivePerson.getDistinguishedName(),
						workCompleted.getTitle(), workCompleted.getId());
			}
			this.updateData(business, workCompleted, jsonElement);
			/** 在方法内进行了commit不需要再次进行commit */
			// emc.commit();
			Wo wo = new Wo();
			wo.setId(workCompleted.getId());
			result.setData(wo);
			return result;
		}
	}

	public static class Wo extends WoId {

	}

	public static class WoControl extends WorkControl {
	}
}
