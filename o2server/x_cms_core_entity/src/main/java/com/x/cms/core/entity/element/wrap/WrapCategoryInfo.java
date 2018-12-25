package com.x.cms.core.entity.element.wrap;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.cms.core.entity.CategoryInfo;

public class WrapCategoryInfo extends CategoryInfo {

	private static final long serialVersionUID = 1439909268641168987L;

	public static WrapCopier<CategoryInfo, WrapCategoryInfo> outCopier = WrapCopierFactory.wo(CategoryInfo.class, WrapCategoryInfo.class,
			null, JpaObject.FieldsInvisible);

	public static WrapCopier<WrapCategoryInfo, CategoryInfo> inCopier = WrapCopierFactory.wi(WrapCategoryInfo.class, CategoryInfo.class,
			null, JpaObject.FieldsUnmodifyExcludeId);
}
