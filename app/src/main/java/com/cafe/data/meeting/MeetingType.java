package com.cafe.data.meeting;

/**
 * 会议类型
 * Created by Justin Z on 2016/11/9.
 * 502953057@qq.com
 */

public enum MeetingType {
	/**
	 * 头脑风暴
	 */
	BRAIN_STORM(1, "brainStorm"),
	/**
	 *
	 */
	THEME(2, "theme");

	private int id;
	private String name;


	MeetingType(int id, String name){
		this.id = id;
		this.name = name;
	}

	public int getId(){
		return id;
	}

	public static MeetingType getMeetingTypeById(int id) {
		for (MeetingType type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "MeetingType{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
