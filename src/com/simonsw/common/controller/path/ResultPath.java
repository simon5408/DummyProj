package com.simonsw.common.controller.path;

/**
 * @author Simon Lv
 * @since 2012-8-9
 */
public interface ResultPath {
	
	String demo = "/demo";
	String user = "/user";
	String login = "/login";
	String logout = "/logout";
	String _403 = "/403";
	
	String GOTO_HOME = "home";
	String GOTO_LOGIN = "login";
	String GOTO_403 = "403";
	String GOTO_INDEX = "/";
}
