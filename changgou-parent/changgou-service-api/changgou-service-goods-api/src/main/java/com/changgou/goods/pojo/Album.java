package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:Album����
 *****/
@ApiModel(description = "Album",value = "Album")
@Table(name="tb_album")
public class Album implements Serializable{

	@ApiModelProperty(value = "���",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;//���

	@ApiModelProperty(value = "�������",required = false)
    @Column(name = "title")
	private String title;//�������

	@ApiModelProperty(value = "������",required = false)
    @Column(name = "image")
	private String image;//������

	@ApiModelProperty(value = "ͼƬ�б�",required = false)
    @Column(name = "image_items")
	private String imageItems;//ͼƬ�б�



	//get����
	public Long getId() {
		return id;
	}

	//set����
	public void setId(Long id) {
		this.id = id;
	}
	//get����
	public String getTitle() {
		return title;
	}

	//set����
	public void setTitle(String title) {
		this.title = title;
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
	public String getImageItems() {
		return imageItems;
	}

	//set����
	public void setImageItems(String imageItems) {
		this.imageItems = imageItems;
	}


}
