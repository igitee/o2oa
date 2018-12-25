package com.x.component.assemble.control.jaxrs.component;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.annotation.CheckRemoveType;
import com.x.base.core.project.cache.ApplicationCache;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WrapBoolean;
import com.x.component.core.entity.Component;

class ActionDelete extends ActionBase {
	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Component component = emc.find(id, Component.class);
			if (null == component) {
				throw new ExceptionEntityNotExist(id, Component.class);
			}
			emc.beginTransaction(Component.class);
			emc.remove(component, CheckRemoveType.all);
			emc.commit();
			Wo wo = new Wo();
			wo.setValue(true);
			result.setData(wo);
			ApplicationCache.notify(Component.class);
			return result;
		}
	}

	public static class Wo extends WrapBoolean {

	}
}
