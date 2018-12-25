package com.x.bbs.assemble.control.jaxrs.subjectinfo.exception;

import com.x.base.core.project.exception.PromptException;

public class ExceptionSubjectContentQueryById extends PromptException {

	private static final long serialVersionUID = 1859164370743532895L;

	public ExceptionSubjectContentQueryById( Throwable e, String id ) {
		super("系统在根据ID查询主题的内容时发生异常.ID:" + id, e );
	}
}
