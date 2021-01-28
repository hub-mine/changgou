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
 * @Description:Sku����
 *****/
@ApiModel(description = "Sku",value = "Sku")
@Table(name="tb_sku")
public class Sku implements Serializable{

	@ApiModelProperty(value = "��Ʒid",required = false)
	@Id
    @Column(name = "id")
	private String id;//��Ʒid

	@ApiModelProperty(value = "��Ʒ����",required = false)
    @Column(name = "sn")
	private String sn;//��Ʒ����

	@ApiModelProperty(value = "SKU����",required = false)
    @Column(name = "name")
	private String name;//SKU����

	@ApiModelProperty(value = "�۸񣨷֣�",required = false)
    @Column(name = "price")
	private Integer price;//�۸񣨷֣�

	@ApiModelProperty(value = "�������",required = false)
    @Column(name = "num")
	private Integer num;//�������

	@ApiModelProperty(value = "���Ԥ������",required = false)
    @Column(name = "alert_num")
	private Integer alertNum;//���Ԥ������

	@ApiModelProperty(value = "��ƷͼƬ",required = false)
    @Column(name = "image")
	private String image;//��ƷͼƬ

	@ApiModelProperty(value = "��ƷͼƬ�б�",required = false)
    @Column(name = "images")
	private String images;//��ƷͼƬ�б�

	@ApiModelProperty(value = "�������ˣ�",required = false)
    @Column(name = "weight")
	private Integer weight;//�������ˣ�

	@ApiModelProperty(value = "����ʱ��",required = false)
    @Column(name = "create_time")
	private Date createTime;//����ʱ��

	@ApiModelProperty(value = "����ʱ��",required = false)
    @Column(name = "update_time")
	private Date updateTime;//����ʱ��

	@ApiModelProperty(value = "SPUID",required = false)
    @Column(name = "spu_id")
	private String spuId;//SPUID

	@ApiModelProperty(value = "��ĿID",required = false)
    @Column(name = "category_id")
	private Integer categoryId;//��ĿID

	@ApiModelProperty(value = "��Ŀ����",required = false)
    @Column(name = "category_name")
	private String categoryName;//��Ŀ����

	@ApiModelProperty(value = "Ʒ������",required = false)
    @Column(name = "brand_name")
	private String brandName;//Ʒ������

	@ApiModelProperty(value = "���",required = false)
    @Column(name = "spec")
	private String spec;//���

	@ApiModelProperty(value = "����",required = false)
    @Column(name = "sale_num")
	private Integer saleNum;//����

	@ApiModelProperty(value = "������",required = false)
    @Column(name = "comment_num")
	private Integer commentNum;//������

	@ApiModelProperty(value = "��Ʒ״̬ 1-������2-�¼ܣ�3-ɾ��",required = false)
    @Column(name = "status")
	private String status;//��Ʒ״̬ 1-������2-�¼ܣ�3-ɾ��



	//get����
	public String getId() {
		return id;
	}

	//set����
	public void setId(String id) {
		this.id = id;
	}
	//get����
	public String getSn() {
		return sn;
	}

	//set����
	public void setSn(String sn) {
		this.sn = sn;
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
	public Integer getPrice() {
		return price;
	}

	//set����
	public void setPrice(Integer price) {
		this.price = price;
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
	public Integer getAlertNum() {
		return alertNum;
	}

	//set����
	public void setAlertNum(Integer alertNum) {
		this.alertNum = alertNum;
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
	public String getImages() {
		return images;
	}

	//set����
	public void setImages(String images) {
		this.images = images;
	}
	//get����
	public Integer getWeight() {
		return weight;
	}

	//set����
	public void setWeight(Integer weight) {
		this.weight = weight;
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
	public Date getUpdateTime() {
		return updateTime;
	}

	//set����
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	//get����
	public String getSpuId() {
		return spuId;
	}

	//set����
	public void setSpuId(String spuId) {
		this.spuId = spuId;
	}
	//get����
	public Integer getCategoryId() {
		return categoryId;
	}

	//set����
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	//get����
	public String getCategoryName() {
		return categoryName;
	}

	//set����
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	//get����
	public String getBrandName() {
		return brandName;
	}

	//set����
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	//get����
	public String getSpec() {
		return spec;
	}

	//set����
	public void setSpec(String spec) {
		this.spec = spec;
	}
	//get����
	public Integer getSaleNum() {
		return saleNum;
	}

	//set����
	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}
	//get����
	public Integer getCommentNum() {
		return commentNum;
	}

	//set����
	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}
	//get����
	public String getStatus() {
		return status;
	}

	//set����
	public void setStatus(String status) {
		this.status = status;
	}


}
