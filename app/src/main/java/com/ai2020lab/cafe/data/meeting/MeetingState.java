package com.ai2020lab.cafe.data.meeting;

/**
 * Created by Justin Z on 2016/11/9.
 * 502953057@qq.com
 */

public enum MeetingState {

	PROGRESS(1, "progress"),
	APPOINTMENT(2, "appointment"),
	HISTORY(3, "history");

	private int id;
	private String name;


	MeetingState(int id, String name){
		this.id = id;
		this.name = name;
	}

	public int getId(){
		return id;
	}

	public static MeetingState getMeetingStateById(int id) {
		for (MeetingState state : values()) {
			if (state.id == id) {
				return state;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "MeetingState{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
