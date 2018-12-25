MWF.xApplication.cms.ColumnManager = MWF.xApplication.cms.ColumnManager || {};
MWF.xApplication.cms.ColumnManager.Actions = MWF.xApplication.cms.ColumnManager.Actions || {};
MWF.xDesktop.requireApp("cms.ColumnManager", "package", null, false);
MWF.require("MWF.xDesktop.Actions.RestActions", null, false);
MWF.require("MWF.widget.UUID", null, false);
MWF.xApplication.cms.ColumnManager.Actions.RestActions = new Class({
	initialize: function(){
		this.action = new MWF.xDesktop.Actions.RestActions("/Actions/action.json", "x_cms_assemble_control", "x_component_cms_ColumnManager");
        this.actionProcess = new MWF.xDesktop.Actions.RestActions("/Actions/action.json", "x_processplatform_assemble_designer", "x_component_cms_ColumnManager");
	},
	getId: function(count, success, failure, async){
		this.action.invoke({"name": "getId","async": async, "parameter": {"count": count},	"success": success,	"failure": failure});
	},
    getUUID: function(success){
        var id = "";
        this.action.invoke({"name": "getId","async": false, "parameter": {"count": "1"}, "success": function(ids){
            id = ids.data[0];
            if (success) success(id);
        },	"failure": null});
        return id;
    },

    listColumn: function( success, failure, async){
        this.action.invoke({"name": "listColumn","async": async, "success": success,	"failure": failure});
    },
    listColumnByPublish: function( success, failure, async){
        this.action.invoke({"name": "listColumnByPublish","async": async, "success": success,	"failure": failure});
    },
    listColumnByAdmin: function( success, failure, async){
        this.action.invoke({"name": "listColumnByAdmin","async": async, "success": success,	"failure": failure});
    },
    getColumn: function(columnData, success, failure){
        this.action.invoke({"name": "getColumn","parameter": {"id": columnData.id},"success": success,"failure": failure});
    },
    saveColumn: function(columnData, success, failure){
        if (!columnData.isNewColumn){
            this.updateColumn(columnData, success, failure);
        }else{
            this.addColumn(columnData, success, failure);
        }
    },
    addColumn: function(columnData, success, failure){
        this.action.invoke({"name": "addColumn","data": columnData,"success": success,"failure": failure});
    },
    updateColumn: function(columnData, success, failure){
        this.action.invoke({"name": "updateColumn","data": columnData,"parameter": {"id": columnData.id},"success": success,"failure": failure});
    },
    removeColumn: function(id, success, failure, async){
        this.action.invoke({"name": "removeColumn", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },
    getColumnIcon: function(columnId, success, failure){
        this.action.invoke({"name": "getColumnIcon", "parameter": {"id": columnId },"success": success,"failure": failure});
    },
    updataColumnIcon: function(columnId, success, failure, formData, file){
        this.action.invoke({"name": "updataColumnIcon", "parameter": {"id": columnId},"data": formData,"file": file,"success": success,"failure": failure});
    },

    listAllCategory: function( success, failure, async){
        this.action.invoke({"name": "listAllCategory","async": async, "success": success,	"failure": failure});
    },
    listCategory: function( columnId, success, failure, async){
        var _self = this;
        this.action.invoke({"name": "listCategory","parameter": {"appId": columnId },"async": async, "success": function(json){
            _self.transCategoryData(json);
            success.call(this,json);
        },"failure": failure});
    },
    listCategoryByPublisher: function( columnId, success, failure, async){
        var _self = this;
        this.action.invoke({"name": "listCategoryByPublisher","parameter": {"appId": columnId },"async": async, "success": function(json){
            _self.transCategoryData(json);
            success.call(this,json);
        },"failure": failure});
    },
    getCategory: function(categoryData, success, failure){
        var _self = this;
        this.action.invoke({"name": "getCategory", "parameter": {"id": categoryData },"success": function(json){
            _self.transCategoryData(json);
            success.call(this,json);
        },"failure": failure});
    },
    saveCategory: function(categoryData, success, failure){
        if (!categoryData.isNew){
            this.updateCategory(categoryData, success, failure);
        }else{
            this.addCategory(categoryData, success, failure);
        }
    },
    addCategory: function(categoryData, success, failure){
        this.action.invoke({"name": "addCategory","data": categoryData,"success": success,"failure": failure});
    },
    updateCategory: function(categoryData, success, failure){
        this.action.invoke({"name": "updateCategory","data": categoryData,"parameter": {"id": categoryData.id},"success": success,"failure": failure});
    },
    removeCategory: function(id, success, failure, async){
        this.action.invoke({"name": "removeCategory", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },
    transCategoryData : function( json ){
        var trans = function(category){
            if(!category.name)category.name = category.categoryName;
            if(!category.alias)category.alias = category.categoryAlias;
            if(!category.categoryName)category.categoryName = category.name;
            if(!category.categoryAlias)category.categoryAlias = category.alias;
        };
        if( json.data ){
            if( typeOf(json.data) == "array" ){
                json.data.each( function(category){
                    trans(category)
                })
            }else{
                trans(json.data)
            }
        }else{
            json.data = [];
        }
    },
    saveCategoryExtContent : function( categoryId, content, success, failure ){
        this.action.invoke({"name": "saveCategoryExtContent","data": { id : categoryId, content : content },"success": success,"failure": failure});
    },
    saveCategoryImportView : function( categoryId, data, success, failure ){
        this.action.invoke({"name": "saveCategoryImportView","parameter": { categoryId : categoryId }, "data": data, "success": success,"failure": failure});
    },

    listAllForm: function( success, failure, async){
        this.action.invoke({"name": "listAllForm","async": async, "success": success,	"failure": failure});
    },
    listForm: function(columnId, success, failure, async){
        this.action.invoke({"name": "listForm","async": async, "parameter": {"appId": columnId}, "success": success,	"failure": failure});
    },
    getForm: function(formId, success, failure, async){
        this.action.invoke({"name": "getForm","async": async, "parameter": {"id": formId},	"success": success,	"failure": failure});
    },
    saveForm: function(formData, mobileData, success, failure){
        if (!formData.isNewForm){
            this.updateForm(formData, mobileData, success, failure);
        }else{
            this.addForm(formData, mobileData, success, failure);
        }
    },
    updateForm: function(formData, mobileData, success, failure){
        var data, mobileData;
        if (formData) data = MWF.encodeJsonString(JSON.encode(formData));
        if (mobileData) mobileData = MWF.encodeJsonString(JSON.encode(mobileData));
        var json = {
            "id": formData.json.id,
            "name": formData.json.name,
            "alias": formData.json.name,
            "description": formData.json.description,
            "appId": formData.json.application
        };
        if (formData) json.data = data;
        if (mobileData) json.mobileData = mobileData;

        this.action.invoke({"name": "updataForm","data": json,"parameter": {"id": formData.json.id},"success": success,"failure": failure});
    },
    addForm: function(formData, mobileData, success, failure){
        var data, mobileData;
        if (!formData.json.id){
            this.getUUID(function(id){
                formData.json.id = id;

                if (formData) data = MWF.encodeJsonString(JSON.encode(formData));
                if (mobileData) mobileData = MWF.encodeJsonString(JSON.encode(mobileData));

                var json = {
                    "id": formData.json.id,
                    "name": formData.json.name,
                    "alias": formData.json.name,
                    "description": formData.json.description,
                    "appId": formData.json.application
                };
                if (formData) json.data = data;
                if (mobileData) json.mobileData = mobileData;
                this.action.invoke({"name": "addForm","data": json, "parameter": {"id": formData.json.id }, "success": success,"failure": failure});
            }.bind(this));
        }else{
            if (formData) data = MWF.encodeJsonString(JSON.encode(formData));
            if (mobileData) mobileData = MWF.encodeJsonString(JSON.encode(mobileData));

            var json = {
                "id": formData.json.id,
                "name": formData.json.name,
                "alias": formData.json.name,
                "description": formData.json.description,
                "appId": formData.json.application
            };
            if (formData) json.data = data;
            if (mobileData) json.mobileData = mobileData;
            this.action.invoke({"name": "addForm","data": json, "parameter": {"id": formData.json.categoryId}, "success": success,"failure": failure});
        }
    },
    deleteForm: function(id, success, failure, async){
        this.action.invoke({"name": "removeForm", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },

    getFormTemplate: function(id, success, failure, async){
        this.action.invoke({"name": "getFormTemplate","async": async, "parameter": {"id": id},	"success": success,	"failure": failure});
    },
    listFormTemplate: function(success, failure, async){
        this.action.invoke({"name": "listFormTemplate","async": async, "success": success,	"failure": failure});
    },
    listFormTemplateCategory: function(success, failure, async){
        this.action.invoke({"name": "listFormTemplateCategory","async": async, "success": success,	"failure": failure});
    },
    listFormTemplatByCategory: function(success, failure, async){
        this.action.invoke({"name": "listFormTemplatByCategory","async": async, "success": success,	"failure": failure});
    },
    addFormTemplate: function(formData, mobileData, templateData, success, failure){
        var data, mobileDataStr;
        this.getUUID(function(id){
            //formData.json.id = id;
            templateData.id = id;
            if (formData){
                var tFormData = Object.clone(formData);
                tFormData.json.id = id;
                tFormData.json.name = templateData.name;
                data = MWF.encodeJsonString(JSON.encode(tFormData));
            }
            if (mobileData){
                var tMobileData = Object.clone(mobileData);
                tMobileData.json.id = id;
                tMobileData.json.name = templateData.name;
                mobileDataStr = MWF.encodeJsonString(JSON.encode(tMobileData));
            }

            //templateData.name
            //templateData.category
            //templateData.description

            if (formData) templateData.data = data;
            if (mobileData) templateData.mobileData = mobileDataStr;
            this.action.invoke({"name": "addFormTemplate","data": templateData, "success": success,"failure": failure});
        }.bind(this));
    },
    deleteFormTemplate: function(id, success, failure, async){
        this.action.invoke({"name": "deleteFormTemplate", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },

    listDictionary: function(appId, success, failure, async){
        this.action.invoke({"name": "listDictionary","async": async, "parameter": {"appId": appId}, "success": success,	"failure": failure});
    },
    getDictionary: function(appDictId, success, failure, async){
        this.action.invoke({"name": "getDictionary","async": async, "parameter": {"appDictId": appDictId },	"success": success,	"failure": failure});
    },
    saveDictionary: function(data, success, failure){
        if (data.id || data.appDictId ){
            this.updateDictionary(data, success, failure);
        }else{
            this.addDictionary(data, success, failure);
        }
    },
    updateDictionary: function(data, success, failure){
        if(!data.alias || data.alias=="")data.alias=data.name;
        this.action.invoke({"name": "updataDictionary","data": data,"parameter": {"appDictId": data.id },"success": success,"failure": failure});
    },
    addDictionary: function(data, success, failure){
        if (!data.id){
            this.getUUID(function(id){
                data.id = id;
                data.appDictId = id;
                if(!data.alias || data.alias=="")data.alias=data.name;
                this.action.invoke({"name": "addDictionary","data": data, "parameter": {"appDictId": data.id },"success": success,"failure": failure});
            }.bind(this));
        }
    },
    deleteDictionary: function(appDictId, success, failure, async){
        this.action.invoke({"name": "removeDictionary", "async": async, "parameter": {"appDictId": appDictId }, "success": success, "failure": failure});
    },


    listScript: function(appId, success, failure, async){
        var _self = this;
        this.action.invoke({"name": "listScript","async": async, "parameter": {"appId": appId}, "success": success,	"failure": failure});
    },
    getScript: function(id, success, failure, async){
        var _self = this;
        this.action.invoke({"name": "getScript","async": async, "parameter": {"id": id},	"success": function(json){
            _self.transScriptData(json);
            success.call(this,json);
        },	"failure": failure});
    },
    getScriptByName: function(name, appId, success, failure, async){
        var _self = this;
        this.action.invoke({"name": "getScriptByName","async": async, "parameter": {"name": name, "appId": appId},	"success": function(json){
            _self.transScriptData(json);
            success.call(this,json);
        },	"failure": failure});
    },
    saveScript: function(data, success, failure){
        if (!data.isNewScript){
            this.updateScript(data, success, failure);
        }else{
            this.addScript(data, success, failure);
        }
    },
    updateScript: function(data, success, failure){
        this.transScriptData(data,"send");
        this.action.invoke({"name": "updataScript","data": data,"parameter": {"id": data.id},"success": success,"failure": failure});
    },
    addScript: function(data, success, failure){
        this.transScriptData(data,"send");
        var _self = this;
        if (!data.id){
            this.getUUID(function(id){
                data.id = id;
                this.action.invoke({"name": "addScript","data": data,"success": success,"failure": failure});
            }.bind(this));
        }else{
            this.action.invoke({"name": "addScript","data": data,"success": success,"failure": failure});
        }
    },
    deleteScript: function(id, success, failure, async){
        this.action.invoke({"name": "removeScript", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },
    transScriptData : function( json, type ){
        var trans = function(json){
            if( type && type == "send" ){
                if(!json.appId )json.appId = json.application;
                if(!json.appName )json.appName = json.applicationName;
                if( json.application )delete json.application;
                if( json.applicationName )delete json.applicationName;
            }else{
                if(!json.appId )json.appId = json.application;
                if(!json.appName )json.appName = json.applicationName;
                if(!json.application )json.application = json.appId;
                if(!json.applicationName )json.application = json.appName;
            }
        };
        if( typeOf(json) == "array" ){
            json.each( function(json){
                trans(json)
            })
        }else{
            trans(json)
        }
    },

    //根据用户帐号和权限类别查询配置记录
    //listController: function(person, objectType, success, failure, async){
    //    this.action.invoke({"name": "listController","async": async, "parameter": {"person": person,"objectType":objectType}, "success": success,	"failure": failure});
    //},
    //listAllController: function( success, failure, async){
    //    this.action.invoke({"name": "listAllController","async": async, "success": success,	"failure": failure});
    //},
    //listCategoryController: function(categoryId, success, failure, async){
    //    this.action.invoke({"name": "listCategoryController","async": async, "parameter": {"categoryId": categoryId}, "success": success,	"failure": failure});
    //},
    //listColumnController: function(appId,success, failure, async){
    //    this.action.invoke({"name": "listColumnController","async": async, "parameter": {"appId": appId}, "success": success,	"failure": failure});
    //},
    //listControllerByPerson: function(person, success, failure, async){
    //    this.action.invoke({"name": "listControllerByPerson","async": async, "parameter": {"person": person}, "success": success,	"failure": failure});
    //},
    //getController: function(id, success, failure, async){
    //    this.action.invoke({"name": "getController","async": async, "parameter": {"appDictId": appDictId },	"success": success,	"failure": failure});
    //},
    //saveController: function(data, success, failure,async){
    //    if (data.id ){
    //        this.updateController(data, success, failure,async);
    //    }else{
    //        this.addController(data, success, failure,async);
    //    }
    //},
    //updateController: function(data, success, failure,async){
    //    this.action.invoke({"name": "updateController","async": async,"data": data,"parameter": {"id": data.id },"success": success,"failure": failure});
    //},
    //addController: function(data, success, failure,async){
    //    if (!data.id){
    //        this.getUUID(function(id){
    //            data.id = id;
    //            this.action.invoke({"name": "addController","async": async,"data": data, "parameter": {"id": data.id },"success": success,"failure": failure});
    //        }.bind(this));
    //    }
    //},
    //removeController: function(id, success, failure, async){
    //    this.action.invoke({"name": "removeController", "async": async, "parameter": {"id": id }, "success": success, "failure": failure});
    //},

    listAppByManager : function(success, failure, async){
        this.action.invoke({"name": "listAppByManager", "async": async, "success": success, "failure": failure});
    },
    isAppInfoManager : function(appId, success, failure, async){
        this.action.invoke({"name": "isAppInfoManager", "async": async, "parameter": {"id": appId }, "success": success, "failure": failure});
    },

    isCategoryInfoManagers : function(categoryId, success, failure, async){
        this.action.invoke({"name": "isCategoryInfoManagers", "async": async, "parameter": {"id": categoryId }, "success": success, "failure": failure});
    },

    listAppInfoManagers : function(appId, success, failure, async){
        this.action.invoke({"name": "listAppInfoManagers", "async": async, "parameter": {"id": appId }, "success": success, "failure": failure});
    },
    listAppInfoPublishers: function(appId, success, failure, async){
        this.action.invoke({"name": "listAppInfoPublishers","async": async, "parameter": {"id": appId}, "success": success,	"failure": failure});
    },
    listAppInfoViewers: function(appId, success, failure, async){
        this.action.invoke({"name": "listAppInfoViewers","async": async, "parameter": {"id": appId}, "success": success,	"failure": failure});
    },

    listCategoryInfoManagers : function(categoryId, success, failure, async){
        this.action.invoke({"name": "listCategoryInfoManagers", "async": async, "parameter": {"id": categoryId }, "success": success, "failure": failure});
    },
    listCategoryInfoPublishers: function(categoryId, success, failure, async){
        this.action.invoke({"name": "listCategoryInfoPublishers","async": async, "parameter": {"id": categoryId}, "success": success,	"failure": failure});
    },
    listCategoryInfoViewers: function(categoryId, success, failure, async){
        this.action.invoke({"name": "listCategoryInfoViewers","async": async, "parameter": {"id": categoryId}, "success": success,	"failure": failure});
    },

    saveAppInfoManager: function( appId, data, success, failure, async){
        this.action.invoke({"name": "saveAppInfoManager","data": data,"parameter": {"id":  appId }, "async": async,"success": success,"failure": failure});
    },
    saveAppInfoPublisher: function( appId, data, success, failure, async){
        this.action.invoke({"name": "saveAppInfoPublisher","data": data,"parameter": {"id":  appId }, "async": async,"success": success,"failure": failure});
    },
    saveAppInfoViewer: function( appId, data, success, failure, async){
        this.action.invoke({"name": "saveAppInfoViewer","data": data,"parameter": {"id":  appId }, "async": async,"success": success,"failure": failure});
    },

    saveCategoryInfoManager: function( categoryId, data, success, failure, async){
        this.action.invoke({"name": "saveCategoryInfoManager","data": data,"parameter": {"id":  categoryId }, "async": async,"success": success,"failure": failure});
    },
    saveCategoryInfoPublisher: function( categoryId, data, success, failure, async){
        this.action.invoke({"name": "saveCategoryInfoPublisher","data": data,"parameter": {"id":  categoryId }, "async": async,"success": success,"failure": failure});
    },
    saveCategoryInfoViewer: function( categoryId, data, success, failure, async){
        this.action.invoke({"name": "saveCategoryInfoViewer","data": data,"parameter": {"id":  categoryId }, "async": async,"success": success,"failure": failure});
    },


    //根据用户帐号和权限类别查询配置记录
    //listPermission: function(person, objectType, success, failure, async){
    //    this.action.invoke({"name": "listPermission","async": async, "parameter": {"person": person,"objectType":objectType}, "success": success,	"failure": failure});
    //},
    //listAllPermission: function( success, failure, async){
    //    this.action.invoke({"name": "listAllPermission","async": async, "success": success,	"failure": failure});
    //},
    //listCategoryController: function(categoryId, success, failure, async){
     //   this.action.invoke({"name": "listCategoryController","async": async, "parameter": {"categoryId": categoryId}, "success": success,	"failure": failure});
    //},
    //listColumnPermission: function(appId, success, failure, async){
    //    this.action.invoke({"name": "listColumnPermission","async": async, "parameter": {"appId": appId }, "success": success,	"failure": failure});
    //},
    //listPermissionByPerson: function(person, success, failure, async){
    //    this.action.invoke({"name": "listPermissionByPerson","async": async, "parameter": {"person": person}, "success": success,	"failure": failure});
    //},
    //getPermission: function(id, success, failure, async){
    //    this.action.invoke({"name": "getPermission","async": async, "parameter": {"appDictId": appDictId },	"success": success,	"failure": failure});
    //},
    //savePermission: function(data, success, failure, async){
    //    if (data.id ){
    //        this.updatePermission(data, success, failure,async);
    //    }else{
    //        this.addPermission(data, success, failure, async);
    //    }
    //},
    //updatePermission: function(data, success, failure, async){
    //    this.action.invoke({"name": "updatePermission","data": data,"parameter": {"id": data.id }, "async": async,"success": success,"failure": failure});
    //},
    //addPermission: function(data, success, failure, async){
    //    if (!data.id){
    //        this.getUUID(function(id){
    //            data.id = id;
    //            this.action.invoke({"name": "addPermission","data": data, "parameter": {"id": data.id },"async": async,"success": success,"failure": failure});
    //        }.bind(this));
    //    }
    //},
    //removePermission: function(id, success, failure, async){
    //    this.action.invoke({"name": "removePermission", "async": async, "parameter": {"id": id }, "success": success, "failure": failure});
    //},


    //listColumnPublisher: function(appId, success, failure, async){
    //    this.action.invoke({"name": "listColumnPublisher","async": async, "parameter": {"appId": appId }, "success": success,	"failure": failure});
    //},
    //listCategoryPermission: function(categoryId, success, failure, async){
    //    this.action.invoke({"name": "listCategoryPermission","async": async, "parameter": {"categoryId": categoryId }, "success": success,	"failure": failure});
    //},
    //listCategoryPublisher: function(categoryId, success, failure, async){
    //        this.action.invoke({"name": "listCategoryPublisher","async": async, "parameter": {"categoryId": categoryId }, "success": success,	"failure": failure});
    //},


    listView: function(appId, success, failure, async){
        this.action.invoke({"name": "listView","async": async, "parameter": {"appId": appId}, "success": success,	"failure": failure});
    },
    listViewByCategory: function(categoryId, success, failure, async){
        this.action.invoke({"name": "listViewByCategory","async": async, "parameter": {"categoryId": categoryId}, "success": success,	"failure": failure});
    },
    listViewByForm: function(formId, success, failure, async){
        this.action.invoke({"name": "listViewByForm","async": async, "parameter": {"formId": formId}, "success": success,	"failure": failure});
    },
    getView: function(id, success, failure){
        this.action.invoke({"name": "getView", "parameter": {"id": id },"success": success,"failure": failure});
    },
    saveView: function(viewData, success, failure){
        if (!viewData.isNew){
            this.updateView(viewData, success, failure);
        }else{
            this.addView(viewData, success, failure);
        }
    },
    addView: function(viewData, success, failure){
        this.action.invoke({"name": "addView","data": viewData,"success": success,"failure": failure});
    },
    updateView: function(viewData, success, failure){
        this.action.invoke({"name": "updateView","data": viewData,"parameter": {"id": viewData.id},"success": success,"failure": failure});
    },
    deleteView: function(id, success, failure, async){
        this.action.invoke({"name": "deleteView", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },


    listViewColumn: function(viewId, success, failure, async){
        this.action.invoke({"name": "listViewColumn","async": async, "parameter": {"viewId": viewId}, "success": success,	"failure": failure});
    },
    getViewColumn: function(id, success, failure){
        this.action.invoke({"name": "getViewColumn", "parameter": {"id": id },"success": success,"failure": failure});
    },
    saveViewColumn: function(data, success, failure,async){
        if (!data.isNew){
            this.updateViewColumn(data, success, failure,async);
        }else{
            this.addViewColumn(data, success, failure,async);
        }
    },
    addViewColumn: function(data, success, failure, async){
        this.action.invoke({"name": "addViewColumn","async": async,"data": data,"success": success,"failure": failure});
    },
    updateViewColumn: function(data, success, failure, async){
        this.action.invoke({"name": "updateViewColumn","async": async,"data": data,"parameter": {"id": data.id},"success": success,"failure": failure});
    },
    deleteViewColumn: function(id, success, failure, async){
        this.action.invoke({"name": "deleteViewColumn", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },



    listCategoryViewByView: function(viewId, success, failure, async){
        this.action.invoke({"name": "listCategoryViewByView","async": async, "parameter": {"viewId": viewId}, "success": success,	"failure": failure});
    },
    listCategoryViewByCategory: function(categoryId, success, failure, async){
        this.action.invoke({"name": "listCategoryViewByCategory","async": async, "parameter": {"categoryId": categoryId}, "success": success,	"failure": failure});
    },
    getCategoryView: function(id, success, failure){
        this.action.invoke({"name": "getCategoryView", "parameter": {"id": id },"success": success,"failure": failure});
    },
    saveCategoryView: function(data, success, failure,async){
        if (!data.isNew){
            this.updateViewColumn(data, success, failure,async);
        }else{
            this.addViewColumn(data, success, failure,async);
        }
    },
    addCategoryView: function(data, success, failure, async){
        this.action.invoke({"name": "addCategoryView","async": async,"data": data,"success": success,"failure": failure});
    },
    updateCategoryView: function(data, success, failure, async){
        this.action.invoke({"name": "updateCategoryView","async": async,"data": data,"parameter": {"id": data.id},"success": success,"failure": failure});
    },
    deleteCategoryView: function(id, success, failure, async){
        this.action.invoke({"name": "deleteCategoryView", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },

    listQueryView: function(applicationId, success, failure, async){
        this.action.invoke({"name": "listQueryView","async": async, "parameter": {"applicationId": applicationId}, "success": success,	"failure": failure});
    },
    listQueryViewNextPage: function(id, count, success, failure, async){
        this.action.invoke({"name": "listQueryViewNextPage","async": async, "parameter": {"id": id, "count":count}, "success": success,	"failure": failure});
    },
    listQueryViewPrevPage: function(id, count, success, failure, async){
        this.action.invoke({"name": "listQueryViewPrevPage","async": async, "parameter": {"id": id, "count":count}, "success": success,	"failure": failure});
    },
    getQueryView: function(id, success, failure){
        this.action.invoke({"name": "getQueryView", "parameter": {"id": id },"success": success,"failure": failure});
    },
    saveQueryView: function(data, success, failure,async){
        if ( data.isNewView ){
            data.isNewView = false;
            this.addQueryView(data, success, failure,async);
        }else{
            this.updateQueryView(data, success, failure,async);
        }
    },
    addQueryView: function(viewData, success, failure, async){
        var data =viewData.data;
        var dataStr = JSON.encode(viewData.data);
        viewData.data = dataStr;
        if (!data.id){
            this.getUUID(function(id){
                viewData.id = id;
                this.action.invoke({"name": "addQueryView","data": viewData, "success": success,"failure": failure,"async": async});
                viewData.data = data;
            }.bind(this));
        }else{
            this.action.invoke({"name": "addQueryView","data": viewData, "success": success,"failure": failure,"async": async});
            viewData.data = data;
        }
    },
    updateQueryView: function(viewData, success, failure, async){
        var data =viewData.data;
        var dataStr = JSON.encode(viewData.data);
        viewData.data = dataStr;
        this.action.invoke({"name": "updateQueryView","data": viewData,"async": async,"parameter": {"id": viewData.id},"success": success,"failure": failure});
        viewData.data = data;
    },
    deleteQueryView: function(id, success, failure, async){
        this.action.invoke({"name": "deleteQueryView", "async": async, "parameter": {"id": id}, "success": success, "failure": failure});
    },
    getQueryViewContent: function(flag, success, failure, async){
        this.action.invoke({"name": "getQueryViewContent","async": async, "parameter": {"flag": flag}, "success": success,	"failure": failure});
    },
    loadQueryView: function(flag, success, failure, async){
        this.action.invoke({"name": "loadQueryView","async": async, "data": {}, "parameter": {"flag": flag}, "success": success,	"failure": failure});
    },

    listApplication: function(categoryName, success, failure, async){
        if (categoryName){
            this.actionProcess.invoke({"name": "listApplicationByCategory","async": async, "parameter": {"applicationCategory": categoryName}, "success": success,	"failure": failure});
        }else{
            this.actionProcess.invoke({"name": "listApplication","async": async, "success": success,	"failure": failure});
        }
    },
    listProcess: function(application, success, failure, async){
        this.actionProcess.invoke({"name": "listProcess","async": async, "parameter": {"id": application},	"success": success,	"failure": failure});
    }
	
});