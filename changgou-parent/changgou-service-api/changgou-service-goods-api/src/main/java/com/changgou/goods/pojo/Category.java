package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:Category����
 *****/
@ApiModel(description = "Category",value = "Category")
@Table(name="tb_category")
public class Category implements Serializable{

	@ApiModelProperty(value = "����ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//����ID

	@ApiModelProperty(value = "��������",required = false)
    @Column(name = "name")
	private String name;//��������

	@ApiModelProperty(value = "��Ʒ����",required = false)
    @Column(name = "goods_num")
	private Integer goodsNum;//��Ʒ����

	@ApiModelProperty(value = "�Ƿ���ʾ",required = false)
    @Column(name = "is_show")
	private String isShow;//�Ƿ���ʾ

	@ApiModelProperty(value = "�Ƿ񵼺�",required = false)
    @Column(name = "is_menu")
	private String isMenu;//�Ƿ񵼺�

	@ApiModelProperty(value = "����",required = false)
    @Column(name = "seq")
	private Integer seq;//����

	@ApiModelProperty(value = "�ϼ�ID",required = false)
    @Column(name = "parent_id")
	private Integer parentId;//�ϼ�ID

	@ApiModelProperty(value = "ģ��ID",required = false)
    @Column(name = "template_id")
	private Integer templateId;//ģ��ID



	//get����
	public Integer getId() {
		return id;
	}

	//set����
	public void setId(Integer id) {
		this.id = id;
	}
	//get����
	public String getName() {
		return name;
	}

	//set����
	public void setName(String name) {
		this.name = name;
	}
	//get����
	public Integer getGoodsNum() {
		return goodsNum;
	}

	//set����
	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}
	//get����
	public String getIsShow() {
		return isShow;
	}

	//set����
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	//get����
	public String getIsMenu() {
		return isMenu;
	}

	//set����
	public void setIsMenu(String isMenu) {
		this.isMenu = isMenu;
	}
	//get����
	public Integer getSeq() {
		return seq;
	}

	//set����
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	//get����
	public Integer getParentId() {
		return parentId;
	}

	//set����
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	//get����
	public Integer getTemplateId() {
		return templateId;
	}

	//set����
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}


}
