package com.ai2020lab.cafe.common.net;

/**
 * 接口URL枚举类
 */
public enum UrlName {
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
	private static final String PROJECT = "sun";
//	private static final String HOST = "10.5.1.116";
	private static final int PORT = 2003;

}
