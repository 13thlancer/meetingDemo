package com.v4ward.meeting.poiEntity;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;

/** 
 * 分会场人员导入模板
 * @ClassName: PartmeetingUserTemplate
 * @author: 彭浩
 * @date: 2018年3月21日 下午1:45:00  
 */
public class PartmeetingUserTemplate implements Serializable {

	private static final long serialVersionUID = -1956575438146472910L;
	
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
	
	
}
