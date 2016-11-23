package com.cafe.common.net;

/**
 * 接口URL枚举类
 */
public enum UrlName {
	// 用户注册
	USER_REGISTER("userRegister"),
	// 用户登录
	USER_LOGIN("userLogin"),
	// 用户登出
	USER_LOGOUT("userLogout"),
	// 查询用户详情
	USER_INFO("getUserInfo"),

	// 查询可用的会议室列表
	MEETING_ROOM_LIST("queryMeetingRoomList"),

	// 创建主题会议
	MEETING_CREATE("createMeeting"),
	// 发起创建临时会议（头脑风暴）请求
	MEETING_BRAIN_STORM("requestForBrainStorm"),
	// 查询会议基本信息
	MEETING_INFO("getMeetingInfo"),
	// 加入会议
	MEETING_JOIN("joinMeeting"),
	// 退出会议
	MEETING_QUIT("quitMeeting"),
	// 取消会议
	MEETING_CANCEL("cancelMeeting"),
	// 解散会议
	MEETING_DISMISS("dismissMeeting"),
	// 查询用户会议列表
	MEETING_LIST("queryUserMeetingList"),

	// 开始主题
	PROCEDURE_START_TOPIC("startTopic"),
	// 结束主题
	PROCEDURE_STOP_TOPIC("stopTopic"),
	// 开始插话
	PROCEDURE_START_EPISODE("startEpisode"),
	// 停止插话
	PROCEDURE_STOP_EPISODE("stopEpisode"),
	// 查询当前说话人
	PROCEDURE_TALKER("getTalkingUser"),
	// 查询会议过程详情列表(说话实时记录列表)
	PROCEDURE_LIST("queryMeetingProcedure"),

	// 上传采集数据
	UPLOAD_DATA("uploadFunf");


	private String name;

	UrlName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return "http://" + HOST + ":" + PORT + "/" + PROJECT + "/" + name;
	}

	private static final String HOST = "171.221.254.231";
	private static final String PROJECT = "cafe";
	private static final int PORT = 2999;

}
