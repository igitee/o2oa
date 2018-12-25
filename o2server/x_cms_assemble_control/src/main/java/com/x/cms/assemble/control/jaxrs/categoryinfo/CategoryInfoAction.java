package com.x.cms.assemble.control.jaxrs.categoryinfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonElement;
import com.x.base.core.project.annotation.JaxrsDescribe;
import com.x.base.core.project.annotation.JaxrsMethodDescribe;
import com.x.base.core.project.annotation.JaxrsParameterDescribe;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.http.HttpMediaType;
import com.x.base.core.project.jaxrs.ResponseFactory;
import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;


@Path("categoryinfo")
@JaxrsDescribe("信息发布内容分类管理")
public class CategoryInfoAction extends StandardJaxrsAction{
	
	private static  Logger logger = LoggerFactory.getLogger( CategoryInfoAction.class );
	
	@JaxrsMethodDescribe(value = "创建或者更新信息分类信息对象.", action = ActionSave.class)
	@POST
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save( @Context HttpServletRequest request, JsonElement jsonElement ) {
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<ActionSave.Wo> result = new ActionResult<>();
		Boolean check = true;
		if( check ){
			try {
				result = new ActionSave().execute( request, effectivePerson, jsonElement );
			} catch (Exception e) {
				result = new ActionResult<>();
				Exception exception = new ExceptionCategoryInfoProcess( e, "分类信息在保存时发生异常." );
				result.error( exception );
				logger.error( e, effectivePerson, request, null);
			}
		}
		return ResponseFactory.getDefaultActionResultResponse(result);
	}
	
	@JaxrsMethodDescribe(value = "为分类绑定导入数据的列表ID.", action = ActionSaveImportView.class)
	@PUT
	@Path("bind/{categoryId}/view")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response bindImportView(@Context HttpServletRequest request, 
			@JaxrsParameterDescribe("分类ID") @PathParam("categoryId") String categoryId,
			 JsonElement jsonElement ) {
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<ActionSaveImportView.Wo> result = null;
		try {
			result = new ActionSaveImportView().execute( request, effectivePerson, categoryId, jsonElement );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "为分类绑定导入数据的列表ID时发生异常。categoryId:"+ categoryId );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
	@JaxrsMethodDescribe(value = "创建或者更新分类扩展信息对象.", action = ActionSaveExtContent.class)
	@POST
	@Path("extContent")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveExtContent( @Context HttpServletRequest request, JsonElement jsonElement ) {
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<ActionSaveExtContent.Wo> result = new ActionResult<>();
		Boolean check = true;
		if( check ){
			try {
				result = new ActionSaveExtContent().execute( request, effectivePerson, jsonElement );
			} catch (Exception e) {
				result = new ActionResult<>();
				Exception exception = new ExceptionCategoryInfoProcess( e, "分类扩展信息在保存时发生异常." );
				result.error( exception );
				logger.error( e, effectivePerson, request, null);
			}
		}
		return ResponseFactory.getDefaultActionResultResponse(result);
	}
	
	@JaxrsMethodDescribe(value = "根据ID删除信息分类信息对象.", action = ActionDelete.class)
	@DELETE
	@Path("{id}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete( @Context HttpServletRequest request, 
			@JaxrsParameterDescribe("分类ID")@PathParam("id") String id) {
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<ActionDelete.Wo> result = new ActionResult<>();
		try {
			result = new ActionDelete().execute( request, id, effectivePerson );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "分类信息在删除时发生异常。ID:" + id );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse(result);
	}
	
	@JaxrsMethodDescribe(value = "获取用户有查看访问文章信息的所有分类列表.", action = ActionListWhatICanView_Article.class)
	@GET
	@Path("list/view/app/{appId}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listViewableCategoryInfo_Article( @Context HttpServletRequest request, 
			@JaxrsParameterDescribe("栏目ID") @PathParam("appId")String appId ) {		
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<List<ActionListWhatICanView_Article.Wo>> result = new ActionResult<>();
		try {
			result = new ActionListWhatICanView_Article().execute( request, appId, effectivePerson );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "根据指定应用栏目ID查询分类信息列表时发生异常。ID:" + appId );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
	@JaxrsMethodDescribe(value = "获取用户有查看访问数据信息的所有分类列表.", action = ActionListWhatICanView_Data.class)
	@GET
	@Path("list/view/app/{appId}/data")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listViewableCategoryInfo_Data( @Context HttpServletRequest request, 
			@JaxrsParameterDescribe("栏目ID") @PathParam("appId")String appId ) {		
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<List<ActionListWhatICanView_Data.Wo>> result = new ActionResult<>();
		try {
			result = new ActionListWhatICanView_Data().execute( request, appId, effectivePerson );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "根据指定应用栏目ID查询分类信息列表时发生异常。ID:" + appId );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
	@JaxrsMethodDescribe(value = "获取用户有查看访问信息的所有分类列表.", action = ActionListWhatICanView_AllType.class)
	@GET
	@Path("list/view/app/{appId}/all")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listViewableCategoryInfo_AllType( @Context HttpServletRequest request, 
			@JaxrsParameterDescribe("栏目ID") @PathParam("appId")String appId ) {		
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<List<ActionListWhatICanView_AllType.Wo>> result = new ActionResult<>();
		try {
			result = new ActionListWhatICanView_AllType().execute( request, appId, effectivePerson );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "根据指定应用栏目ID查询分类信息列表时发生异常。ID:" + appId );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
	@JaxrsMethodDescribe(value = "获取用户有权限发布信息的所有分类列表.", action = ActionListWhatICanPublish.class)
	@GET
	@Path("list/publish/app/{appId}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listPublishableCategoryInfo( @Context HttpServletRequest request, 
			@JaxrsParameterDescribe("栏目ID") @PathParam("appId")String appId ) {		
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<List<ActionListWhatICanPublish.Wo>> result = new ActionResult<>();
		try {
			result = new ActionListWhatICanPublish().execute( request, appId, effectivePerson );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "根据应用栏目ID查询分类信息对象时发生异常。AppId:" + appId );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
	@JaxrsMethodDescribe(value = "获取用户有权限访问信息的所有分类列表.", action = ActionListAll.class)
	@GET
	@Path("list/all")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listAllCategoryInfo( @Context HttpServletRequest request ) {		
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<List<ActionListAll.Wo>> result = null;
		try {
			result = new ActionListAll().execute( request, effectivePerson );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "查询所有分类信息对象时发生异常。" );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
	@JaxrsMethodDescribe(value = "根据Flag获取分类信息对象.", action = ActionGet.class)
	@GET
	@Path("{flag}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response get(@Context HttpServletRequest request, 
			@JaxrsParameterDescribe("栏目标识") @PathParam("flag") String flag) {
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<ActionGet.Wo> result = null;
		try {
			result = new ActionGet().execute( request, flag, effectivePerson );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "根据ID查询分类信息对象时发生异常。flag:" + flag );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
	@JaxrsMethodDescribe(value = "根据标识强制更新指定的分类Review.", action = ActionUpdateReviewForce.class)
	@GET
	@Path("review/{flag}/update")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateReviewForce(@Context HttpServletRequest request, @JaxrsParameterDescribe("分类ID") @PathParam("flag") String flag) {
		EffectivePerson effectivePerson = this.effectivePerson(request);
		ActionResult<WoId> result = new ActionResult<>();
		try {
			result = new ActionUpdateReviewForce().execute( request, effectivePerson, flag );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess(e, "根据指定ID查询分类栏目信息对象时发生异常。flag:" + flag );
			result.error(exception);
			logger.error(e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse(result);
	}
	
	@JaxrsMethodDescribe(value = "根据别名获取分类信息对象.", action = ActionGet.class)
	@GET
	@Path("alias/{alias}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getByAlias(@Context HttpServletRequest request, 
			@JaxrsParameterDescribe("栏目别名") @PathParam("alias") String alias ) {
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<ActionGetByAlias.Wo> result = null;
		try {
			result = new ActionGetByAlias().execute( request, alias, effectivePerson );
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "根据标识查询分类信息对象时发生异常。ALIAS:"+ alias );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
	@JaxrsMethodDescribe(value = "列示根据过滤条件的信息分类,下一页.", action = ActionListNextWithFilter.class)
	@PUT
	@Path("filter/list/{id}/next/{count}/app/{appId}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listNextWithFilter(@Context HttpServletRequest request, 
			@JaxrsParameterDescribe("最后一条信息ID，如果是第一页，则可以用(0)代替") @PathParam("id") String id, 
			@JaxrsParameterDescribe("每页显示的条目数量") @PathParam("count") Integer count, 
			@JaxrsParameterDescribe("栏目ID")  @PathParam("appId") String appId, 
			JsonElement jsonElement ) {
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<List<ActionListNextWithFilter.Wo>> result = null;
		try {
			result = new ActionListNextWithFilter().execute( request, effectivePerson, id, count, jsonElement);
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "列示根据过滤条件的信息分类时发生异常。" );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}

	@JaxrsMethodDescribe(value = "列示根据过滤条件的信息分类,上一页.", action = ActionListPrevWithFilter.class)
	@PUT
	@Path("filter/list/{id}/prev/{count}/app/{appId}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listPrevWithFilter( @Context HttpServletRequest request, 
			@JaxrsParameterDescribe("最后一条信息ID，如果是第一页，则可以用(0)代替") @PathParam("id") String id, 
			@JaxrsParameterDescribe("每页显示的条目数量") @PathParam("count") Integer count, 
			@JaxrsParameterDescribe("栏目ID") @PathParam("appId") Integer appId, 
			JsonElement jsonElement ) {
		EffectivePerson effectivePerson = this.effectivePerson( request );
		ActionResult<List<ActionListPrevWithFilter.Wo>> result = null;
		try {
			result = new ActionListPrevWithFilter().execute( request, effectivePerson, id, count, jsonElement);
		} catch (Exception e) {
			result = new ActionResult<>();
			Exception exception = new ExceptionCategoryInfoProcess( e, "列示根据过滤条件的信息分类时发生异常。" );
			result.error( exception );
			logger.error( e, effectivePerson, request, null);
		}
		return ResponseFactory.getDefaultActionResultResponse( result );
	}
	
}