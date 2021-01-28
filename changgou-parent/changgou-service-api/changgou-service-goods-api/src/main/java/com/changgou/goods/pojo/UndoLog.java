package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/****
 * @Author:itheima
 * @Description:UndoLog����
 *****/
@ApiModel(description = "UndoLog",value = "UndoLog")
@Table(name="undo_log")
public class UndoLog implements Serializable{

	@ApiModelProperty(value = "",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "branch_id")
	private Long branchId;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "xid")
	private String xid;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "context")
	private String context;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "rollback_info")
	private String rollbackInfo;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "log_status")
	private Integer logStatus;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "log_created")
	private Date logCreated;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "log_modified")
	private Date logModified;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "ext")
	private String ext;//



	//get����
	public Long getId() {
		return id;
	}

	//set����
	public void setId(Long id) {
		this.id = id;
	}
	//get����
	public Long getBranchId() {
		return branchId;
	}

	//set����
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	//get����
	public String getXid() {
		return xid;
	}

	//set����
	public void setXid(String xid) {
		this.xid = xid;
	}
	//get����
	public String getContext() {
		return context;
	}

	//set����
	public void setContext(String context) {
		this.context = context;
	}
	//get����
	public String getRollbackInfo() {
		return rollbackInfo;
	}

	//set����
	public void setRollbackInfo(String rollbackInfo) {
		this.rollbackInfo = rollbackInfo;
	}
	//get����
	public Integer getLogStatus() {
		return logStatus;
	}

	//set����
	public void setLogStatus(Integer logStatus) {
		this.logStatus = logStatus;
	}
	//get����
	public Date getLogCreated() {
		return logCreated;
	}

	//set����
	public void setLogCreated(Date logCreated) {
		this.logCreated = logCreated;
	}
	//get����
	public Date getLogModified() {
		return logModified;
	}

	//set����
	public void setLogModified(Date logModified) {
		this.logModified = logModified;
	}
	//get����
	public String getExt() {
		return ext;
	}

	//set����
	public void setExt(String ext) {
		this.ext = ext;
	}


}
