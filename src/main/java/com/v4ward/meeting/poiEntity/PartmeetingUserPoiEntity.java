package com.v4ward.meeting.poiEntity;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class PartmeetingUserPoiEntity implements Serializable {

	private static final long serialVersionUID = -1956575438146472910L;
	
	private String id;

	@Excel(name = "姓名", orderNum = "0",width=8)
	private String name;

	@Excel(name = "手机号", orderNum = "1",width=12)
	private Long account;

	@Excel(name = "酒店信息", orderNum = "2",width=20)
	private String hotelInfo;

	@Excel(name = "房间信息", orderNum = "3",width=10)
	private String roomInfo;

	@Excel(name = "座位信息", orderNum = "4",width=10)
	private String seatInfo;
	
	@Excel(name = "所属部门", orderNum = "5",width=40)
	private String orgName;
	
	@Excel(name = "签到时间", orderNum = "6",width=20)
	private String signtime;
	
	@Excel(name = "签到状态", replace = { "未签到_0", "已签到_1", "迟到_2", "请假_3" }, orderNum = "7",width=8)
	private String signStatus;
	
	@Excel(name = "签到类型", replace = { "后台导入_0", "扫码绑定_1" }, orderNum = "8",width=8)
	private String bindStatus;

	private String userId;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the account
	 */
	public Long getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Long account) {
		this.account = account;
	}

	/**
	 * @return the hotelInfo
	 */
	public String getHotelInfo() {
		return hotelInfo;
	}

	/**
	 * @param hotelInfo the hotelInfo to set
	 */
	public void setHotelInfo(String hotelInfo) {
		this.hotelInfo = hotelInfo;
	}

	/**
	 * @return the roomInfo
	 */
	public String getRoomInfo() {
		return roomInfo;
	}

	/**
	 * @param roomInfo the roomInfo to set
	 */
	public void setRoomInfo(String roomInfo) {
		this.roomInfo = roomInfo;
	}

	/**
	 * @return the seatInfo
	 */
	public String getSeatInfo() {
		return seatInfo;
	}

	/**
	 * @param seatInfo the seatInfo to set
	 */
	public void setSeatInfo(String seatInfo) {
		this.seatInfo = seatInfo;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	
	/**
	 * @return the signtime
	 */
	public String getSigntime() {
		return signtime;
	}

	/**
	 * @param signtime the signtime to set
	 */
	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}

	/**
	 * @return the signStatus
	 */
	public String getSignStatus() {
		return signStatus;
	}

	/**
	 * @param signStatus the signStatus to set
	 */
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}

	/**
	 * @return the bindStatus
	 */
	public String getBindStatus() {
		return bindStatus;
	}

	/**
	 * @param bindStatuss the bindStatus to set
	 */
	public void setBindStatus(String bindStatus) {
		this.bindStatus = bindStatus;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
