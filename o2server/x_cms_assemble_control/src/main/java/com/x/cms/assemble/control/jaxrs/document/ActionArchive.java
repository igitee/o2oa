package com.x.cms.assemble.control.jaxrs.document;

import javax.servlet.http.HttpServletRequest;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.cache.ApplicationCache;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.cms.assemble.control.Business;
import com.x.cms.core.entity.Document;

/**
 * 文档归档操作执行类
 * @author O2LEE
 *
 */
public class ActionArchive extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger( ActionArchive.class );
	
	/**
	 * 文档归档操作
	 * @param request
	 * @param id  文档ID
	 * @param effectivePerson 系统登录用户
	 * @return
	 * @throws Exception
	 */
	protected ActionResult<Wo> execute( HttpServletRequest request, String id, EffectivePerson effectivePerson ) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();
		try ( EntityManagerContainer emc = EntityManagerContainerFactory.instance().create() ) {
			Business business = new Business( emc );
			Document document = business.getDocumentFactory().get( id );
			if ( null == document ) {
				Exception exception = new ExceptionDocumentNotExists( id );
				result.error( exception );
				throw exception;
			}
			try {
				modifyDocStatus( id, "archived", effectivePerson.getDistinguishedName() );
				document.setDocStatus( "archived" );
				
				ApplicationCache.notify( Document.class );
			} catch ( Exception e ) {
				Exception exception = new ExceptionDocumentInfoProcess( e, "系统将文档状态修改为归档状态时发生异常。Id:" + id );
				result.error( exception );
				logger.error( e, effectivePerson, request, null );
				throw exception;
			}
			
			//信息操作日志记录
			logService.log( emc, effectivePerson.getDistinguishedName(), document.getCategoryAlias() + ":" + document.getTitle(), document.getAppId(), document.getCategoryId(), document.getId(), "", "DOCUMENT", "删除" );
			
			Wo wo = new Wo();
			wo.setId( document.getId() );
			result.setData( wo );
		} catch ( Exception e ) {
			Exception exception = new ExceptionDocumentInfoProcess( e, "文档归档操作发生异常。Id:" + id );
			result.error( exception );
			logger.error( e, effectivePerson, request, null );
			throw exception;
		}
		return result;
	}
	
	public static class Wo extends WoId {
	}

}