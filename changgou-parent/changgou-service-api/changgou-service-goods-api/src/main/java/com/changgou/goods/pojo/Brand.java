package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:Brand����
 *****/
@ApiModel(description = "Brand",value = "Brand")
@Table(name="tb_brand")
public class Brand implements Serializable{

	@ApiModelProperty(value = "Ʒ��id",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//Ʒ��id

	@ApiModelProperty(value = "Ʒ������",required = false)
    @Column(name = "name")
	private String name;//Ʒ������

	@ApiModelProperty(value = "Ʒ��ͼƬ��ַ",required = false)
    @Column(name = "image")
	private String image;//Ʒ��ͼƬ��ַ

	@ApiModelProperty(value = "Ʒ�Ƶ�����ĸ",required = false)
    @Column(name = "letter")
	private String letter;//Ʒ�Ƶ�����ĸ

	@ApiModelProperty(value = "����",required = false)
    @Column(name = "seq")
	private Integer seq;//����



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
	public String getImage() {
		return image;
	}

	//set����
	public void setImage(String image) {
		this.image = image;
	}
	//get����
	public String getLetter() {
		return letter;
	}

	//set����
	public void setLetter(String letter) {
		this.letter = letter;
	}
	//get����
	public Integer getSeq() {
		return seq;
	}

	//set����
	public void setSeq(Integer seq) {
		this.seq = seq;
	}


}
