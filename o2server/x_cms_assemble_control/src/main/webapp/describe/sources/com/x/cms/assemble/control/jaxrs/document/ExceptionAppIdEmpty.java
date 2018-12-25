package com.x.cms.assemble.control.jaxrs.document;

import com.x.base.core.project.exception.PromptException;

class ExceptionAppIdEmpty extends PromptException {

	private static final long serialVersionUID = 1859164370743532895L;

	ExceptionAppIdEmpty() {
		super("栏目信息ID为空，无法进行查询操作。" );
	}
}
