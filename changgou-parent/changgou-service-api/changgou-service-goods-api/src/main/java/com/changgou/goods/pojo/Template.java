package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:Template����
 *****/
@ApiModel(description = "Template",value = "Template")
@Table(name="tb_template")
public class Template implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//ID

	@ApiModelProperty(value = "ģ������",required = false)
    @Column(name = "name")
	private String name;//ģ������

	@ApiModelProperty(value = "�������",required = false)
    @Column(name = "spec_num")
	private Integer specNum;//�������

	@ApiModelProperty(value = "��������",required = false)
    @Column(name = "para_num")
	private Integer paraNum;//��������



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
	public Integer getSpecNum() {
		return specNum;
	}

	//set����
	public void setSpecNum(Integer specNum) {
		this.specNum = specNum;
	}
	//get����
	public Integer getParaNum() {
		return paraNum;
	}

	//set����
	public void setParaNum(Integer paraNum) {
		this.paraNum = paraNum;
	}


}
