package com.x.processplatform.core.entity.content;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.apache.openjpa.persistence.PersistentCollection;
import org.apache.openjpa.persistence.jdbc.ContainerTable;
import org.apache.openjpa.persistence.jdbc.ElementColumn;
import org.apache.openjpa.persistence.jdbc.ElementIndex;
import org.apache.openjpa.persistence.jdbc.Index;

import com.x.base.core.entity.AbstractPersistenceProperties;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.SliceJpaObject;
import com.x.base.core.entity.annotation.CheckPersist;
import com.x.base.core.entity.annotation.ContainerEntity;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.tools.DateTools;
import com.x.base.core.project.tools.StringTools;
import com.x.processplatform.core.entity.PersistenceProperties;
import com.x.processplatform.core.entity.element.ActivityType;

@Entity
@ContainerEntity
@Table(name = PersistenceProperties.Content.Work.table, uniqueConstraints = {
		@UniqueConstraint(name = PersistenceProperties.Content.Work.table + JpaObject.IndexNameMiddle
				+ JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN,
						JpaObject.CREATETIMECOLUMN, JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Work extends SliceJpaObject {

	private static final long serialVersionUID = 7668822947307502058L;
	private static final String TABLE = PersistenceProperties.Content.Work.table;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@FieldDescribe("数据库主键,自动生成.")
	@Id
	@Column(length = length_id, name = ColumnNamePrefix + id_FIELDNAME)
	private String id = createId();

	/* 以上为 JpaObject 默认字段 */

	public void onPersist() throws Exception {
		if (StringUtils.isEmpty(this.startTimeMonth) && (null != this.startTime)) {
			this.startTimeMonth = DateTools.format(this.startTime, DateTools.format_yyyyMM);
		}
		if (null == this.serial) {
			this.serial = "";
		}
	}

	/* 更新运行方法 */

	public Work() {

	}

	public Work(Work work) throws Exception {
		work.copyTo(this, JpaObject.id_FIELDNAME);
	}

	public void setTitle(String title) {
		if (StringTools.utf8Length(title) > length_255B) {
			this.title = StringTools.utf8SubString(this.title, 252) + "...";
		} else {
			this.title = title;
		}
	}

	/* 修改过的Set Get 方法 */

	public static final String job_FIELDNAME = "job";
	@FieldDescribe("工作")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + job_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + job_FIELDNAME)
	// @KeyIndex(name = TABLE + IndexNameMiddle + job_FIELDNAME + "111")
	@CheckPersist(allowEmpty = false)
	private String job;

	public static final String title_FIELDNAME = "title";
	@FieldDescribe("标题")
	@Column(length = length_255B, name = ColumnNamePrefix + title_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + title_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String title;

	public static final String startTime_FIELDNAME = "startTime";
	@FieldDescribe("工作开始时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = ColumnNamePrefix + startTime_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + startTime_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Date startTime;

	public static final String startTimeMonth_FIELDNAME = "startTimeMonth";
	@FieldDescribe("用于在Filter中分类使用.")
	@Column(length = JpaObject.length_16B, name = ColumnNamePrefix + startTimeMonth_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + startTimeMonth_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String startTimeMonth;

	public static final String creatorPerson_FIELDNAME = "creatorPerson";
	@FieldDescribe("创建人，可能为空，如果由系统创建。")
	@Column(length = length_255B, name = ColumnNamePrefix + creatorPerson_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + creatorPerson_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String creatorPerson;

	public static final String creatorIdentity_FIELDNAME = "creatorIdentity";
	@FieldDescribe("创建人Identity,可能为空,如果由系统创建。")
	@Column(length = length_255B, name = ColumnNamePrefix + creatorIdentity_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + creatorIdentity_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String creatorIdentity;

	public static final String creatorUnit_FIELDNAME = "creatorUnit";
	@FieldDescribe("创建人组织,可能为空,如果由系统创建。")
	@Column(length = length_255B, name = ColumnNamePrefix + creatorUnit_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + creatorUnit_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String creatorUnit;

	public static final String creatorUnitLevelName_FIELDNAME = "creatorUnitLevelName";
	@FieldDescribe("创建人组织层级名.")
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@CheckPersist(allowEmpty = true)
	@Column(length = JpaObject.length_2K, name = ColumnNamePrefix + creatorUnitLevelName_FIELDNAME)
	private String creatorUnitLevelName;

	public static final String application_FIELDNAME = "application";
	@FieldDescribe("应用ID")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + application_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + application_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String application;

	public static final String applicationName_FIELDNAME = "applicationName";
	@FieldDescribe("应用名称.")
	@Column(length = length_255B, name = ColumnNamePrefix + applicationName_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + applicationName_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String applicationName;

	public static final String applicationAlias_FIELDNAME = "applicationAlias";
	@FieldDescribe("应用别名.")
	@Column(length = length_255B, name = ColumnNamePrefix + applicationAlias_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + applicationAlias_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String applicationAlias;

	public static final String process_FIELDNAME = "process";
	@FieldDescribe("流程ID")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + process_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + process_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String process;

	public static final String processName_FIELDNAME = "processName";
	@FieldDescribe("流程名称")
	@Column(length = length_255B, name = ColumnNamePrefix + processName_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + processName_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String processName;

	public static final String processAlias_FIELDNAME = "processAlias";
	@FieldDescribe("流程别名")
	@Column(length = length_255B, name = ColumnNamePrefix + processAlias_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + processAlias_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String processAlias;

	public static final String activity_FIELDNAME = "activity";
	@FieldDescribe("当前活动ID")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + activity_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + activity_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String activity;

	public static final String activityType_FIELDNAME = "activityType";
	@FieldDescribe("活动类型.")
	@Enumerated(EnumType.STRING)
	@Column(length = ActivityType.length, name = ColumnNamePrefix + activityType_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + activityType_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private ActivityType activityType;

	public static final String activityName_FIELDNAME = "activityName";
	@FieldDescribe("活动名称")
	@Column(length = length_255B, name = ColumnNamePrefix + activityName_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + activityName_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String activityName;

	public static final String activityAlias_FIELDNAME = "activityAlias";
	@FieldDescribe("活动别名")
	@Column(length = length_255B, name = ColumnNamePrefix + activityAlias_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + activityAlias_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String activityAlias;

	public static final String activityDescription_FIELDNAME = "activityDescription";
	@FieldDescribe("活动名称")
	@Column(length = length_255B, name = ColumnNamePrefix + activityDescription_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + activityDescription_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String activityDescription;

	public static final String activityToken_FIELDNAME = "activityToken";
	@FieldDescribe("活动的标识号，每进入一次活动将重新生成一次")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + activityToken_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + activityToken_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String activityToken;

	public static final String activityArrivedTime_FIELDNAME = "activityArrivedTime";
	@FieldDescribe("活动到达时间")
	@Column(name = ColumnNamePrefix + activityArrivedTime_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + activityArrivedTime_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Date activityArrivedTime;

	public static final String serial_FIELDNAME = "serial";
	@FieldDescribe("编号")
	@Column(length = JpaObject.length_128B, name = ColumnNamePrefix + serial_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + serial_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String serial;

	public static final String dataChanged_FIELDNAME = "dataChanged";
	@FieldDescribe("当前工作是否经过保存修改的操作,用于判断是否是默认生成的未经修改的.")
	@Column(name = ColumnNamePrefix + dataChanged_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + dataChanged_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Boolean dataChanged;

	public static final String workStatus_FIELDNAME = "workStatus";
	@FieldDescribe("工作状态")
	@Enumerated(EnumType.STRING)
	@Column(length = WorkStatus.length, name = ColumnNamePrefix + workStatus_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + workStatus_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private WorkStatus workStatus;

	public static final String errorRetry_FIELDNAME = "errorRetry";
	@FieldDescribe("重试次数.")
	@Column(name = ColumnNamePrefix + errorRetry_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Integer errorRetry;

	public static final String beforeExecuted_FIELDNAME = "beforeExecuted";
	@FieldDescribe("是否已经通过执行前")
	@Column(name = ColumnNamePrefix + beforeExecuted_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Boolean beforeExecuted;

	/** Manual Attribute */
	public static final String manualTaskIdentityList_FIELDNAME = "manualTaskIdentityList";
	@FieldDescribe("预期的处理人")
	@PersistentCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = AbstractPersistenceProperties.orderColumn)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ manualTaskIdentityList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle
					+ manualTaskIdentityList_FIELDNAME + JoinIndexNameSuffix))
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + manualTaskIdentityList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + manualTaskIdentityList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> manualTaskIdentityList;

	/** Split Attribute */
	public static final String splitting_FIELDNAME = "splitting";
	@FieldDescribe("是否是拆分中的工作")
	@Index(name = TABLE + IndexNameMiddle + splitting_FIELDNAME)
	@Column(name = ColumnNamePrefix + splitting_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Boolean splitting;

	public static final String splitToken_FIELDNAME = "splitToken";
	@FieldDescribe("拆分工作令牌")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + splitToken_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String splitToken;

	public static final String splitTokenList_FIELDNAME = "splitTokenList";
	@FieldDescribe("拆分工作产生的Token")
	@PersistentCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = AbstractPersistenceProperties.orderColumn)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle + splitTokenList_FIELDNAME, joinIndex = @Index(name = TABLE
			+ IndexNameMiddle + splitTokenList_FIELDNAME + JoinIndexNameSuffix))
	@ElementColumn(length = JpaObject.length_id, name = ColumnNamePrefix + splitTokenList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + splitTokenList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> splitTokenList;

	public static final String splitValue_FIELDNAME = "splitValue";
	@FieldDescribe("拆分值")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + splitValue_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String splitValue;

	public static final String serviceValue_FIELDNAME = "serviceValue";
	@FieldDescribe("Service活动环节回写的值ID")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + serviceValue_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + serviceValue_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String serviceValue;

	public static final String form_FIELDNAME = "form";
	@FieldDescribe("使用表单")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + form_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String form;

	public static final String destinationRoute_FIELDNAME = "destinationRoute";
	@FieldDescribe("到达使用的路由")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + destinationRoute_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String destinationRoute;

	public static final String destinationRouteName_FIELDNAME = "destinationRouteName";
	@FieldDescribe("到达使用的路由")
	@Column(length = length_255B, name = ColumnNamePrefix + destinationRouteName_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String destinationRouteName;

	public static final String destinationActivityType_FIELDNAME = "destinationActivityType";
	@Enumerated(EnumType.STRING)
	@FieldDescribe("当前活动类型")
	@Column(length = ActivityType.length, name = ColumnNamePrefix + destinationActivityType_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + destinationActivityType_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private ActivityType destinationActivityType;

	public static final String destinationActivity_FIELDNAME = "destinationActivity";
	@FieldDescribe("目标活动的ID")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + destinationActivity_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + destinationActivity_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String destinationActivity;

	public static final String forceRoute_FIELDNAME = "forceRoute";
	@FieldDescribe("强制路由，用于调度等需要跳过执行环节直接进行的.")
	@Column(name = ColumnNamePrefix + forceRoute_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Boolean forceRoute;

	public static final String forceRouteArriveCurrentActivity_FIELDNAME = "forceRouteArriveCurrentActivity";
	@FieldDescribe("是否是强制路由进入当前节点.")
	@Column(name = ColumnNamePrefix + forceRouteArriveCurrentActivity_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Boolean forceRouteArriveCurrentActivity;

	public static final String expireTime_FIELDNAME = "expireTime";
	@FieldDescribe("任务截止时间.")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = ColumnNamePrefix + expireTime_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + expireTime_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Date expireTime;

	public static final String embedTargetWork_FIELDNAME = "embedTargetWork";
	@FieldDescribe("Embed活动生成的WorkId，用于在embed生成targetWork之后在inquire环节进行推动。")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + embedTargetWork_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + embedTargetWork_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String embedTargetWork;

	// public static final String extensionString01_FIELDNAME = "extensionString01";
	// @FieldDescribe("扩充文本字段01.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString01_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString01_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString01;
	//
	// public static final String extensionString02_FIELDNAME = "extensionString02";
	// @FieldDescribe("扩充文本字段02.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString02_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString02_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString02;
	//
	// public static final String extensionString03_FIELDNAME = "extensionString03";
	// @FieldDescribe("扩充文本字段03.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString03_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString03_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString03;
	//
	// public static final String extensionString04_FIELDNAME = "extensionString04";
	// @FieldDescribe("扩充文本字段04.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString04_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString04_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString04;
	//
	// public static final String extensionString05_FIELDNAME = "extensionString05";
	// @FieldDescribe("扩充文本字段05.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString05_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString05_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString05;
	//
	// public static final String extensionString06_FIELDNAME = "extensionString06";
	// @FieldDescribe("扩充文本字段06.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString06_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString06_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString06;
	//
	// public static final String extensionString07_FIELDNAME = "extensionString07";
	// @FieldDescribe("扩充文本字段07.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString07_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString07_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString07;
	//
	// public static final String extensionString08_FIELDNAME = "extensionString08";
	// @FieldDescribe("扩充文本字段08.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString08_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString08_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString08;
	//
	// public static final String extensionString09_FIELDNAME = "extensionString09";
	// @FieldDescribe("扩充文本字段09.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString09_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString09_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString09;
	//
	// public static final String extensionString10_FIELDNAME = "extensionString10";
	// @FieldDescribe("扩充文本字段10.")
	// @Column(length = length_255B, name = ColumnNamePrefix +
	// extensionString10_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionString10_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private String extensionString10;
	//
	// public static final String extensionDouble01_FIELDNAME = "extensionDouble01";
	// @FieldDescribe("扩充数字字段01.")
	// @Column(name = ColumnNamePrefix + extensionDouble01_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionDouble01_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Double extensionDouble01;
	//
	// public static final String extensionDouble02_FIELDNAME = "extensionDouble02";
	// @FieldDescribe("扩充数字字段02.")
	// @Column(name = ColumnNamePrefix + extensionDouble02_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionDouble02_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Double extensionDouble02;
	//
	// public static final String extensionDouble03_FIELDNAME = "extensionDouble03";
	// @FieldDescribe("扩充数字字段03.")
	// @Column(name = ColumnNamePrefix + extensionDouble03_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionDouble03_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Double extensionDouble03;
	//
	// public static final String extensionDouble04_FIELDNAME = "extensionDouble04";
	// @FieldDescribe("扩充数字字段04.")
	// @Column(name = ColumnNamePrefix + extensionDouble04_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionDouble04_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Double extensionDouble04;
	//
	// public static final String extensionDouble05_FIELDNAME = "extensionDouble05";
	// @FieldDescribe("扩充数字字段05.")
	// @Column(name = ColumnNamePrefix + extensionDouble05_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionDouble05_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Double extensionDouble05;
	//
	// public static final String extensionBoolean01_FIELDNAME =
	// "extensionBoolean01";
	// @FieldDescribe("扩充布尔字段01.")
	// @Column(name = ColumnNamePrefix + extensionBoolean01_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionBoolean01_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Boolean extensionBoolean01;
	//
	// public static final String extensionBoolean02_FIELDNAME =
	// "extensionBoolean02";
	// @FieldDescribe("扩充布尔字段02.")
	// @Column(name = ColumnNamePrefix + extensionBoolean02_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionBoolean02_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Boolean extensionBoolean02;
	//
	// public static final String extensionBoolean03_FIELDNAME =
	// "extensionBoolean03";
	// @FieldDescribe("扩充布尔字段03.")
	// @Column(name = ColumnNamePrefix + extensionBoolean03_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionBoolean03_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Boolean extensionBoolean03;
	//
	// public static final String extensionBoolean04_FIELDNAME =
	// "extensionBoolean04";
	// @FieldDescribe("扩充布尔字段04.")
	// @Column(name = ColumnNamePrefix + extensionBoolean04_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionBoolean04_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Boolean extensionBoolean04;
	//
	// public static final String extensionBoolean05_FIELDNAME =
	// "extensionBoolean05";
	// @FieldDescribe("扩充布尔字段05.")
	// @Column(name = ColumnNamePrefix + extensionBoolean05_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + extensionBoolean05_FIELDNAME)
	// @CheckPersist(allowEmpty = true)
	// private Boolean extensionBoolean05;

	public String getTitle() {
		return title;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public WorkStatus getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(WorkStatus workStatus) {
		this.workStatus = workStatus;
	}

	public String getCreatorIdentity() {
		return creatorIdentity;
	}

	public void setCreatorIdentity(String creatorIdentity) {
		this.creatorIdentity = creatorIdentity;
	}

	public String getCreatorUnit() {
		return creatorUnit;
	}

	public void setCreatorUnit(String creatorUnit) {
		this.creatorUnit = creatorUnit;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityToken() {
		return activityToken;
	}

	public void setActivityToken(String activityToken) {
		this.activityToken = activityToken;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Boolean getSplitting() {
		return splitting;
	}

	public void setSplitting(Boolean splitting) {
		this.splitting = splitting;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCreatorPerson() {
		return creatorPerson;
	}

	public void setCreatorPerson(String creatorPerson) {
		this.creatorPerson = creatorPerson;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getSplitToken() {
		return splitToken;
	}

	public void setSplitToken(String splitToken) {
		this.splitToken = splitToken;
	}

	public String getSplitValue() {
		return splitValue;
	}

	public void setSplitValue(String splitValue) {
		this.splitValue = splitValue;
	}

	public List<String> getSplitTokenList() {
		return splitTokenList;
	}

	public void setSplitTokenList(List<String> splitTokenList) {
		this.splitTokenList = splitTokenList;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	// public Boolean getExecuted() {
	// return executed;
	// }
	//
	// public void setExecuted(Boolean executed) {
	// this.executed = executed;
	// }

	public Integer getErrorRetry() {
		return errorRetry;
	}

	public void setErrorRetry(Integer errorRetry) {
		this.errorRetry = errorRetry;
	}

	public String getDestinationRoute() {
		return destinationRoute;
	}

	public void setDestinationRoute(String destinationRoute) {
		this.destinationRoute = destinationRoute;
	}

	public String getDestinationActivity() {
		return destinationActivity;
	}

	public void setDestinationActivity(String destinationActivity) {
		this.destinationActivity = destinationActivity;
	}

	public String getDestinationRouteName() {
		return destinationRouteName;
	}

	public void setDestinationRouteName(String destinationRouteName) {
		this.destinationRouteName = destinationRouteName;
	}

	public Date getActivityArrivedTime() {
		return activityArrivedTime;
	}

	public void setActivityArrivedTime(Date activityArrivedTime) {
		this.activityArrivedTime = activityArrivedTime;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public List<String> getManualTaskIdentityList() {
		return manualTaskIdentityList;
	}

	public void setManualTaskIdentityList(List<String> manualTaskIdentityList) {
		this.manualTaskIdentityList = manualTaskIdentityList;
	}

	public ActivityType getDestinationActivityType() {
		return destinationActivityType;
	}

	public void setDestinationActivityType(ActivityType destinationActivityType) {
		this.destinationActivityType = destinationActivityType;
	}

	public String getStartTimeMonth() {
		return startTimeMonth;
	}

	public void setStartTimeMonth(String startTimeMonth) {
		this.startTimeMonth = startTimeMonth;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getProcessAlias() {
		return processAlias;
	}

	public void setProcessAlias(String processAlias) {
		this.processAlias = processAlias;
	}

	public String getApplicationAlias() {
		return applicationAlias;
	}

	public void setApplicationAlias(String applicationAlias) {
		this.applicationAlias = applicationAlias;
	}

	public String getServiceValue() {
		return serviceValue;
	}

	public void setServiceValue(String serviceValue) {
		this.serviceValue = serviceValue;
	}

	public Boolean getForceRoute() {
		return forceRoute;
	}

	public void setForceRoute(Boolean forceRoute) {
		this.forceRoute = forceRoute;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getEmbedTargetWork() {
		return embedTargetWork;
	}

	public void setEmbedTargetWork(String embedTargetWork) {
		this.embedTargetWork = embedTargetWork;
	}

	public Boolean getBeforeExecuted() {
		return beforeExecuted;
	}

	public void setBeforeExecuted(Boolean beforeExecuted) {
		this.beforeExecuted = beforeExecuted;
	}

	public String getCreatorUnitLevelName() {
		return creatorUnitLevelName;
	}

	public void setCreatorUnitLevelName(String creatorUnitLevelName) {
		this.creatorUnitLevelName = creatorUnitLevelName;
	}

	public String getActivityAlias() {
		return activityAlias;
	}

	public static String getActivitydescriptionFieldname() {
		return activityDescription_FIELDNAME;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityAlias(String activityAlias) {
		this.activityAlias = activityAlias;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public Boolean getForceRouteArriveCurrentActivity() {
		return forceRouteArriveCurrentActivity;
	}

	public void setForceRouteArriveCurrentActivity(Boolean forceRouteArriveCurrentActivity) {
		this.forceRouteArriveCurrentActivity = forceRouteArriveCurrentActivity;
	}

	public Boolean getDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(Boolean dataChanged) {
		this.dataChanged = dataChanged;
	}

}