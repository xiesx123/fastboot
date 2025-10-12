package com.xiesx.fastboot;

/**
 * @title Version.java
 * @description
 * @author sxxie
 * @date 2023-03-22 18:02:15
 */
public class Version {

	/**
	 * 应用名称
	 */
	public static final String APPLICATION = "${project.name}";
	
	/**
	 * 当前版本
	 */
	public static final String VERSION = "${project.version}";

	/**
	 * 构建分支
	 */
	public static final String BRANCH = "${buildBranch}";
	
	/**
	 * 构建版本
	 */
	public static final String BUILD = "${buildNumber}";

	/**
	 * 构建时间
	 */
	public static final String TIMESTAMP = "${buildTimestamp}";
}
