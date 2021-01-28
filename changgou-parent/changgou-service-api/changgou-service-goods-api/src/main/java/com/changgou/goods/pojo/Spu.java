package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:Spu����
 *****/
@ApiModel(description = "Spu",value = "Spu")
@Table(name="tb_spu")
public class Spu implements Serializable{

	@ApiModelProperty(value = "����",required = false)
	@Id
    @Column(name = "id")
	private String id;//����

	@ApiModelProperty(value = "����",required = false)
    @Column(name = "sn")
	private String sn;//����

	@ApiModelProperty(value = "SPU��",required = false)
    @Column(name = "name")
	private String name;//SPU��

	@ApiModelProperty(value = "������",required = false)
    @Column(name = "caption")
	private String caption;//������

	@ApiModelProperty(value = "Ʒ��ID",required = false)
    @Column(name = "brand_id")
	private Integer brandId;//Ʒ��ID

	@ApiModelProperty(value = "һ������",required = false)
    @Column(name = "category1_id")
	private Integer category1Id;//һ������

	@ApiModelProperty(value = "��������",required = false)
    @Column(name = "category2_id")
	private Integer category2Id;//��������

	@ApiModelProperty(value = "��������",required = false)
    @Column(name = "category3_id")
	private Integer category3Id;//��������

	@ApiModelProperty(value = "ģ��ID",required = false)
    @Column(name = "template_id")
	private Integer templateId;//ģ��ID

	@ApiModelProperty(value = "�˷�ģ��id",required = false)
    @Column(name = "freight_id")
	private Integer freightId;//�˷�ģ��id

	@ApiModelProperty(value = "ͼƬ",required = false)
    @Column(name = "image")
	private String image;//ͼƬ

	@ApiModelProperty(value = "ͼƬ�б�",required = false)
    @Column(name = "images")
	private String images;//ͼƬ�б�

	@ApiModelProperty(value = "�ۺ����",required = false)
    @Column(name = "sale_service")
	private String saleService;//�ۺ����

	@ApiModelProperty(value = "����",required = false)
    @Column(name = "introduction")
	private String introduction;//����

	@ApiModelProperty(value = "����б�",required = false)
    @Column(name = "spec_items")
	private String specItems;//����б�

	@ApiModelProperty(value = "�����б�",required = false)
    @Column(name = "para_items")
	private String paraItems;//�����б�

	@ApiModelProperty(value = "����",required = false)
    @Column(name = "sale_num")
	private Integer saleNum;//����

	@ApiModelProperty(value = "������",required = false)
    @Column(name = "comment_num")
	private Integer commentNum;//������

	@ApiModelProperty(value = "�Ƿ��ϼ�,0���¼ܣ�1���ϼ�",required = false)
    @Column(name = "is_marketable")
	private String isMarketable;//�Ƿ��ϼ�,0���¼ܣ�1���ϼ�

	@ApiModelProperty(value = "�Ƿ����ù��",required = false)
    @Column(name = "is_enable_spec")
	private String isEnableSpec;//�Ƿ����ù��

	@ApiModelProperty(value = "�Ƿ�ɾ��,0:δɾ����1����ɾ��",required = false)
    @Column(name = "is_delete")
	private String isDelete;//�Ƿ�ɾ��,0:δɾ����1����ɾ��

	@ApiModelProperty(value = "���״̬��0��δ��ˣ�1������ˣ�2����˲�ͨ��",required = false)
    @Column(name = "status")
	private String status;//���״̬��0��δ��ˣ�1������ˣ�2����˲�ͨ��



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
	public String getCaption() {
		return caption;
	}

	//set����
	public void setCaption(String caption) {
		this.caption = caption;
	}
	//get����
	public Integer getBrandId() {
		return brandId;
	}

	//set����
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	//get����
	public Integer getCategory1Id() {
		return category1Id;
	}

	//set����
	public void setCategory1Id(Integer category1Id) {
		this.category1Id = category1Id;
	}
	//get����
	public Integer getCategory2Id() {
		return category2Id;
	}

	//set����
	public void setCategory2Id(Integer category2Id) {
		this.category2Id = category2Id;
	}
	//get����
	public Integer getCategory3Id() {
		return category3Id;
	}

	//set����
	public void setCategory3Id(Integer category3Id) {
		this.category3Id = category3Id;
	}
	//get����
	public Integer getTemplateId() {
		return templateId;
	}

	//set����
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	//get����
	public Integer getFreightId() {
		return freightId;
	}

	//set����
	public void setFreightId(Integer freightId) {
		this.freightId = freightId;
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
	public String getSaleService() {
		return saleService;
	}

	//set����
	public void setSaleService(String saleService) {
		this.saleService = saleService;
	}
	//get����
	public String getIntroduction() {
		return introduction;
	}

	//set����
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	//get����
	public String getSpecItems() {
		return specItems;
	}

	//set����
	public void setSpecItems(String specItems) {
		this.specItems = specItems;
	}
	//get����
	public String getParaItems() {
		return paraItems;
	}

	//set����
	public void setParaItems(String paraItems) {
		this.paraItems = paraItems;
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
	public String getIsMarketable() {
		return isMarketable;
	}

	//set����
	public void setIsMarketable(String isMarketable) {
		this.isMarketable = isMarketable;
	}
	//get����
	public String getIsEnableSpec() {
		return isEnableSpec;
	}

	//set����
	public void setIsEnableSpec(String isEnableSpec) {
		this.isEnableSpec = isEnableSpec;
	}
	//get����
	public String getIsDelete() {
		return isDelete;
	}

	//set����
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
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
