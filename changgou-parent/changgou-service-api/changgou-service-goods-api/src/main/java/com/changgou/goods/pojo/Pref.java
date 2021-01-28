package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/****
 * @Author:itheima
 * @Description:Pref����
 *****/
@ApiModel(description = "Pref",value = "Pref")
@Table(name="tb_pref")
public class Pref implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//ID

	@ApiModelProperty(value = "����ID",required = false)
    @Column(name = "cate_id")
	private Integer cateId;//����ID

	@ApiModelProperty(value = "���ѽ��",required = false)
    @Column(name = "buy_money")
	private Integer buyMoney;//���ѽ��

	@ApiModelProperty(value = "�Żݽ��",required = false)
    @Column(name = "pre_money")
	private Integer preMoney;//�Żݽ��

	@ApiModelProperty(value = "���ʼ����",required = false)
    @Column(name = "start_time")
	private Date startTime;//���ʼ����

	@ApiModelProperty(value = "���������",required = false)
    @Column(name = "end_time")
	private Date endTime;//���������

	@ApiModelProperty(value = "����,1:��ͨ������2����ʱ�",required = false)
    @Column(name = "type")
	private String type;//����,1:��ͨ������2����ʱ�

	@ApiModelProperty(value = "״̬,1:��Ч��0����Ч",required = false)
    @Column(name = "state")
	private String state;//״̬,1:��Ч��0����Ч



	//get����
	public Integer getId() {
		return id;
	}

	//set����
	public void setId(Integer id) {
		this.id = id;
	}
	//get����
	public Integer getCateId() {
		return cateId;
	}

	//set����
	public void setCateId(Integer cateId) {
		this.cateId = cateId;
	}
	//get����
	public Integer getBuyMoney() {
		return buyMoney;
	}

	//set����
	public void setBuyMoney(Integer buyMoney) {
		this.buyMoney = buyMoney;
	}
	//get����
	public Integer getPreMoney() {
		return preMoney;
	}

	//set����
	public void setPreMoney(Integer preMoney) {
		this.preMoney = preMoney;
	}
	//get����
	public Date getStartTime() {
		return startTime;
	}

	//set����
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	//get����
	public Date getEndTime() {
		return endTime;
	}

	//set����
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	//get����
	public String getType() {
		return type;
	}

	//set����
	public void setType(String type) {
		this.type = type;
	}
	//get����
	public String getState() {
		return state;
	}

	//set����
	public void setState(String state) {
		this.state = state;
	}


}
