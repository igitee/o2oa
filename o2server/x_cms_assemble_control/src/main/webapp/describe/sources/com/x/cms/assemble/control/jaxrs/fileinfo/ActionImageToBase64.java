package com.x.cms.assemble.control.jaxrs.fileinfo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.math.NumberUtils;
import org.imgscalr.Scalr;

import com.x.base.core.project.cache.ApplicationCache;
import com.x.base.core.project.config.StorageMapping;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.http.WrapOutString;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.cms.assemble.control.ThisApplication;
import com.x.cms.assemble.control.jaxrs.fileinfo.exception.ExceptionFileInfoBase64Encode;
import com.x.cms.assemble.control.jaxrs.fileinfo.exception.ExceptionFileInfoIdEmpty;
import com.x.cms.assemble.control.jaxrs.fileinfo.exception.ExceptionFileInfoIsNotImage;
import com.x.cms.assemble.control.jaxrs.fileinfo.exception.ExceptionFileInfoNotExists;
import com.x.cms.assemble.control.jaxrs.fileinfo.exception.ExceptionFileInfoQueryById;
import com.x.cms.assemble.control.jaxrs.fileinfo.exception.ExceptionFileInfoSizeInvalid;
import com.x.cms.core.entity.FileInfo;

import net.sf.ehcache.Element;

public class ActionImageToBase64 extends BaseAction {

	private static  Logger logger = LoggerFactory.getLogger( ActionImageToBase64.class );
	
	protected ActionResult<WrapOutString> execute( HttpServletRequest request, EffectivePerson effectivePerson, String id, String size ) throws Exception {
		ActionResult<WrapOutString> result = new ActionResult<>();
		WrapOutString wrap = null;
		FileInfo fileInfo = null;
		Integer sizeNum = null;
		Boolean check = true;
		String cacheKey = ApplicationCache.concreteCacheKey( "base64", id, size );
		Element element = cache.get(cacheKey);
		
		if ((null != element) && ( null != element.getObjectValue()) ) {
			wrap = ( WrapOutString ) element.getObjectValue();
			result.setData(wrap);
		} else {
			if( check ){
				if( id == null || id.isEmpty() ){
					check = false;
					Exception exception = new ExceptionFileInfoIdEmpty();
					result.error( exception );
					//logger.error( e, effectivePerson, request, null);
				}
			}
			if( check ){
				if( size != null && !size.isEmpty() ){
					if ( NumberUtils.isNumber( size ) ) {
						sizeNum = Integer.parseInt( size );
					}else{
						check = false;
						Exception exception = new ExceptionFileInfoSizeInvalid();
						result.error( exception );
						//logger.error( e, effectivePerson, request, null);
					}
				}else{
					sizeNum = 800;
				}
			}
			if( check ){
				try {
					fileInfo = fileInfoServiceAdv.get( id );
					if( fileInfo == null ){
						check = false;
						Exception exception = new ExceptionFileInfoNotExists( id );
						result.error( exception );
						//logger.error( e, effectivePerson, request, null);
					}
				} catch (Exception e) {
					check = false;
					Exception exception = new ExceptionFileInfoQueryById( e, id );
					result.error( exception );
					logger.error( e, effectivePerson, request, null);
				}
				
			}
			if( check ){
				if ( !isImage( fileInfo ) ){
					check = false;
					Exception exception = new ExceptionFileInfoIsNotImage( id );
					result.error( exception );
					//logger.error( e, effectivePerson, request, null);
				}
			}
			BufferedImage image = null;
			ByteArrayInputStream input = null;
			ByteArrayOutputStream output_for_ftp = new ByteArrayOutputStream();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			StorageMapping mapping = ThisApplication.context().storageMappings().get( FileInfo.class, fileInfo.getStorage());
			try{
				fileInfo.readContent( mapping, output_for_ftp );
				input = new ByteArrayInputStream( output_for_ftp.toByteArray() );
				image = ImageIO.read( input );
				int width = image.getWidth();
				int height = image.getHeight();
				if ( sizeNum > 0 ) {
					if( width * height > sizeNum * sizeNum ){
						image = Scalr.resize( image, sizeNum );
					}
				}							
				ImageIO.write( image, "png", output );
				wrap = new WrapOutString();
				wrap.setValue(Base64.encodeBase64String( output.toByteArray() ));
				cache.put(new Element( cacheKey, wrap ));
				result.setData( wrap );
			}catch( Exception e ){
				check = false;
				Exception exception = new ExceptionFileInfoBase64Encode( e, id );
				result.error( exception );
				logger.error( e, effectivePerson, request, null);
			}
		}
		
		return result;
	}

	private boolean isImage(FileInfo fileInfo) {
		if( fileInfo == null || fileInfo.getExtension() == null || fileInfo.getExtension().isEmpty() ){
			return false;
		}
		if( "jpg".equalsIgnoreCase( fileInfo.getExtension() )){
			return true;
		}else if( "png".equalsIgnoreCase( fileInfo.getExtension() )){
			return true;
		}else if( "jpeg".equalsIgnoreCase( fileInfo.getExtension() )){
			return true;
		}else if( "tiff".equalsIgnoreCase( fileInfo.getExtension() )){
			return true;
		}else if( "gif".equalsIgnoreCase( fileInfo.getExtension() )){
			return true;
		}else if( "bmp".equalsIgnoreCase( fileInfo.getExtension() )){
			return true;
		}
		return false;
	}
	
}