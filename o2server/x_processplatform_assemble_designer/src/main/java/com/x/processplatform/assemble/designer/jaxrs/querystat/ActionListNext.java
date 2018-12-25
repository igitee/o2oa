package com.x.processplatform.assemble.designer.jaxrs.querystat;

import java.util.List;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.processplatform.core.entity.element.QueryStat;

class ActionListNext extends BaseAction {
	ActionResult<List<Wo>> execute(String id, Integer count) throws Exception {
		ActionResult<List<Wo>> result = new ActionResult<>();
		result = this.standardListNext(Wo.copier, id, count, "sequence", null, null, null, null, null, null, null, null,
				true, DESC);
		return result;
	}

	public static class Wo extends QueryStat {

		private static final long serialVersionUID = -5755898083219447939L;

		static WrapCopier<QueryStat, Wo> copier = WrapCopierFactory.wo(QueryStat.class, Wo.class, null,
				JpaObject.FieldsInvisible);

		@FieldDescribe("排序号")
		private Long rank;

		public Long getRank() {
			return rank;
		}

		public void setRank(Long rank) {
			this.rank = rank;
		}

	}

}
