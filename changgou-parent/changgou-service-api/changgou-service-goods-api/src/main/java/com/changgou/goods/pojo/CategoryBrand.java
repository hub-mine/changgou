package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:CategoryBrand����
 *****/
@ApiModel(description = "CategoryBrand",value = "CategoryBrand")
@Table(name="tb_category_brand")
public class CategoryBrand implements Serializable{

	@ApiModelProperty(value = "����ID",required = false)
	@Id
    @Column(name = "category_id")
	private Integer categoryId;//����ID

	@ApiModelProperty(value = "Ʒ��ID",required = false)
    @Column(name = "brand_id")
	private Integer brandId;//Ʒ��ID



	//get����
	public Integer getCategoryId() {
		return categoryId;
	}

	//set����
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	//get����
	public Integer getBrandId() {
		return brandId;
	}

	//set����
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}


}
