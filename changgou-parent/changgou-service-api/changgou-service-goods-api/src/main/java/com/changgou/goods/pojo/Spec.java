package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:Spec����
 *****/
@ApiModel(description = "Spec",value = "Spec")
@Table(name="tb_spec")
public class Spec implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//ID

	@ApiModelProperty(value = "����",required = false)
    @Column(name = "name")
	private String name;//����

	@ApiModelProperty(value = "���ѡ��",required = false)
    @Column(name = "options")
	private String options;//���ѡ��

	@ApiModelProperty(value = "����",required = false)
    @Column(name = "seq")
	private Integer seq;//����

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
	public String getOptions() {
		return options;
	}

	//set����
	public void setOptions(String options) {
		this.options = options;
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
	public Integer getTemplateId() {
		return templateId;
	}

	//set����
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}


}
