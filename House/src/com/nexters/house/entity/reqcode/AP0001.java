package com.nexters.house.entity.reqcode;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AP0001 implements Serializable {
	public static final int NORMAL = 1;
	public static final int SCRAP = 2;
	public static final int LIKE = 3;
	
	private int type;
	private String orderType;
	private int reqPo;
	private int reqPoCnt;
	private long reqPoNo;
	private int reqPoType;
	private String usrId;
	
	private int resCnt;
	private List<AP0001Res> res;
	private String resDate;
	private long resLastNo;
	
	@JsonProperty("_type")
	public int getType() {
		return type;
	}
	@JsonProperty("_order_type")
	public String getOrderType() {
		return orderType;
	}
	@JsonProperty("_req_po")
	public int getReqPo() {
		return reqPo;
	}
	@JsonProperty("_req_po_cnt")
	public int getReqPoCnt() {
		return reqPoCnt;
	}
	@JsonProperty("_req_po_no")
	public long getReqPoNo() {
		return reqPoNo;
	}
	@JsonProperty("_usr_id")
	public String getUsrId() {
		return usrId;
	}
	@JsonIgnore
	public int getResCnt() {
		return resCnt;
	}
	@JsonIgnore
	public List<AP0001Res> getRes() {
		return res;
	}
	@JsonIgnore
	public String getResDate() {
		return resDate;
	}
	@JsonIgnore
	public long getResLastNo() {
		return resLastNo;
	}
	@JsonProperty("_req_po_type")
	public int getReqPoType() {
		return reqPoType;
	}
	
	@JsonIgnore
	public void setReqPoType(int reqPoType) {
		this.reqPoType = reqPoType;
	}
	@JsonIgnore
	public void setType(int type) {
		this.type = type;
	}
	@JsonIgnore
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	@JsonIgnore
	public void setReqPo(int reqPo) {
		this.reqPo = reqPo;
	}
	@JsonIgnore
	public void setReqPoCnt(int reqPoCnt) {
		this.reqPoCnt = reqPoCnt;
	}
	@JsonIgnore
	public void setReqPoNo(long reqPoNo) {
		this.reqPoNo = reqPoNo;
	}
	@JsonProperty
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	@JsonProperty("_res_cnt")
	public void setResCnt(int resCnt) {
		this.resCnt = resCnt;
	}
	@JsonProperty("_res")
	public void setRes(List<AP0001Res> res) {
		this.res = res;
	}
	@JsonProperty("_res_date")
	public void setResDate(String resDate) {
		this.resDate = resDate;
	}
	@JsonProperty("_res_last_no")
	public void setResLastNo(long resLastNo) {
		this.resLastNo = resLastNo;
	}

	public static class AP0001Res {
		@JsonProperty("_brd_no")
		public long brdNo;
		@JsonProperty("_brd_nm")
		public String brdNm;
		@JsonProperty("_brd_id")
		public String brdId;
		@JsonProperty("_brd_profile_img")
		public String brdProfileImg;
		@JsonProperty("_brd_subject")
		public String brdSubject;
		@JsonProperty("_brd_contents")
		public byte[] brdContents;
		@JsonProperty("_brd_tag")
		public String brdTag;
		@JsonProperty("_brd_cate_nm")
		public String brdCateNm;
		@JsonProperty("_brd_cate")
		public int brdCate;
		@JsonProperty("_brd_modified")
		public String brdModified;
		@JsonProperty("_brd_created")
		public String brdCreated;
		@JsonProperty("_brd_like_cnt")
		public int brdLikeCnt;
		@JsonProperty("_brd_comment_cnt")
		public int brdCommentCnt;
		@JsonProperty("_brd_img")
		public List<AP0001Img> brdImg;
	}
	
	public static class AP0001Img {
		@JsonProperty("_brd_thumb_img")
		public String brdThumbImg;
		@JsonProperty("_brd_origin_img")
		public String brdOriginImg;
	}
}
