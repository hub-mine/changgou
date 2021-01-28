package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/****
 * @Author:itheima
 * @Description:StockBack����
 *****/
@ApiModel(description = "StockBack",value = "StockBack")
@Table(name="tb_stock_back")
public class StockBack implements Serializable{

	@ApiModelProperty(value = "����id",required = false)
    @Column(name = "order_id")
	private String orderId;//����id

	@ApiModelProperty(value = "SKU��id",required = false)
	@Id
    @Column(name = "sku_id")
	private String skuId;//SKU��id

	@ApiModelProperty(value = "�ع�����",required = false)
    @Column(name = "num")
	private Integer num;//�ع�����

	@ApiModelProperty(value = "�ع�״̬",required = false)
    @Column(name = "status")
	private String status;//�ع�״̬

	@ApiModelProperty(value = "����ʱ��",required = false)
    @Column(name = "create_time")
	private Date createTime;//����ʱ��

	@ApiModelProperty(value = "�ع�ʱ��",required = false)
    @Column(name = "back_time")
	private Date backTime;//�ع�ʱ��



	//get����
	public String getOrderId() {
		return orderId;
	}

	//set����
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	//get����
	public String getSkuId() {
		return skuId;
	}

	//set����
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	//get����
	public Integer getNum() {
		return num;
	}

	//set����
	public void setNum(Integer num) {
		this.num = num;
	}
	//get����
	public String getStatus() {
		return status;
	}

	//set����
	public void setStatus(String status) {
		this.status = status;
	}
	//get����
	public Date getCreateTime() {
		return createTime;
	}

	//set����
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	//get����
	public Date getBackTime() {
		return backTime;
	}

	//set����
	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}


}
