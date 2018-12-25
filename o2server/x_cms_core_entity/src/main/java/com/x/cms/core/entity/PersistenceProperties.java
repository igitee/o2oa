package com.x.cms.core.entity;

import com.x.base.core.entity.AbstractPersistenceProperties;

public final class PersistenceProperties extends AbstractPersistenceProperties {
	
	public static class Element {
		public static class QueryView {
			public static final String table = "CMS_E_QUERYVIEW";
		}
		public static class View {
			public static final String table = "CMS_VIEW";
		}
		public static class ViewFieldConfig {
			public static final String table = "CMS_VIEWFIELD_CONFIG";
		}
		public static class ViewCategory {
			public static final String table = "CMS_VIEWCATEGORY";
		}
		public static class Form {
			public static final String table = "CMS_FORM";
		}
		public static class FormField {
			public static final String table = "CMS_FORMFIELD";
		}
		public static class TemplateForm {
			public static final String table = "CMS_TEMPLATEFORM";
		}
		public static class AppDict {
			public static final String table = "CMS_APPNDICT";
		}
		public static class AppDictItem {
			public static final String table = "CMS_APPDICTITEM";
		}
		public static class Script {
			public static final String table = "CMS_SCRIPT";
		}
	}

	public static class AppInfo {
		public static final String table = "CMS_APPINFO";
	}
	
	public static class CategoryInfo {
		public static final String table = "CMS_CATEGORYINFO";
	}
	
	public static class CategoryExt {
		public static final String table = "CMS_CATEGORYEXT";
	}
	
	public static class Document {
		public static final String table = "CMS_DOCUMENT";
	}
	
	public static class FileInfo {
		public static final String table = "CMS_FILEINFO";
	}
	
	public static class Log {
		public static final String table = "CMS_LOG";
	}
	
	public static class DocumentViewRecord {
		public static final String table = "CMS_DOCUMENT_VIEWRECORD";
	}
	
	public static class Review {
		public static final String table = "CMS_REVIEW";
	}
	
	/**
	 * 不用了，后续会删除
	 * @author O2LEE
	 *
	 */
	public static class AppCategoryAdmin {
		public static final String table = "CMS_APPCATEGORY_ADMIN";
	}
	
	/**
	 * 不用了，后续会删除
	 * @author O2LEE
	 *
	 */
	public static class AppCategoryPermission {
		public static final String table = "CMS_APPCATEGORY_PERMISSION";
	}
	
	/**
	 * 不用了，后续会删除
	 * @author O2LEE
	 *
	 */
	public static class DocumentPermission {
		public static final String table = "CMS_DOCUMENT_PERMISSION";
	}

}