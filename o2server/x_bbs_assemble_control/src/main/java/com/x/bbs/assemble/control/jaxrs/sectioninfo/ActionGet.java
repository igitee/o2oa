package com.x.bbs.assemble.control.jaxrs.sectioninfo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.bbs.assemble.control.jaxrs.sectioninfo.exception.ExceptionSectionIdEmpty;
import com.x.bbs.assemble.control.jaxrs.sectioninfo.exception.ExceptionSectionInfoProcess;
import com.x.bbs.assemble.control.jaxrs.sectioninfo.exception.ExceptionSectionNotExists;
import com.x.bbs.entity.BBSSectionInfo;

public class ActionGet extends BaseAction {
	
	private static  Logger logger = LoggerFactory.getLogger( ActionGet.class );
	
	protected ActionResult<Wo> execute( HttpServletRequest request, EffectivePerson effectivePerson, String id ) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();
		Wo wrap = null;
		BBSSectionInfo sectionInfo = null;
		Boolean check = true;
		
		if( check ){
			if( id == null || id.isEmpty() ){
				check = false;
				Exception exception = new ExceptionSectionIdEmpty();
				result.error( exception );
			}
		}
		if( check ){
			try {
				sectionInfo = sectionInfoServiceAdv.get( id );
			} catch (Exception e) {
				check = false;
				Exception exception = new ExceptionSectionInfoProcess( e, "根据指定ID查询版块信息时发生异常.ID:" + id );
				result.error( exception );
				logger.error( e, effectivePerson, request, null);
			}
		}
		if( check ){
			if( sectionInfo != null ){
				try {
					wrap = Wo.copier.copy( sectionInfo );
					result.setData( wrap );
				} catch (Exception e) {
					check = false;
					Exception exception = new ExceptionSectionInfoProcess( e, "系统在转换所有BBS版块信息为输出对象时发生异常." );
					result.error( exception );
					logger.error( e, effectivePerson, request, null);
				}
			}else{
				Exception exception = new ExceptionSectionNotExists( id );
				result.error( exception );
			}
		}
		return result;
	}

	public static class Wo extends BBSSectionInfo{
		
		private static final long serialVersionUID = -5076990764713538973L;
		
		public static List<String> Excludes = new ArrayList<String>();
		
		public static WrapCopier< BBSSectionInfo, Wo > copier = WrapCopierFactory.wo( BBSSectionInfo.class, Wo.class, null, JpaObject.FieldsInvisible);
		
		//版块的子版块信息列表
		private List<Wo> subSections = null;

		public List<Wo> getSubSections() {
			return subSections;
		}
		public void setSubSections(List<Wo> subSections) {
			this.subSections = subSections;
		}	
	}
}