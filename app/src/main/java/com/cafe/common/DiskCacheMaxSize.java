package com.cafe.common;

/**
 * 磁盘缓存最大容量常量类
 * Created by Justin on 2016/1/7.
 * Email:502953057@qq.com,zhenghx3@asiainfo.com
 */
public interface DiskCacheMaxSize {

	/**
	 * 最多缓存100MB音量数据
	 */
	long AUDIO_DB = (long) (100 * 1024 * 1024);

	/**
	 * 最多缓存100MB活动等级数据
	 */
	long ACTIVITY_LEVEl = (long) (100 * 1024 * 1024);
}

