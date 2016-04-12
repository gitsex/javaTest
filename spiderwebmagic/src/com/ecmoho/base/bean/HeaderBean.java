package com.ecmoho.base.bean;

import java.util.Map;

public class HeaderBean {
	
	private String userAgent;
	private String referer;
	private String orgin;
	private String acceptLanguage;
	private String accept;
	private String cookie;
	private Map<String,String> urlMap;
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getOrgin() {
		return orgin;
	}
	public void setOrgin(String orgin) {
		this.orgin = orgin;
	}
	public String getAcceptLanguage() {
		return acceptLanguage;
	}
	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}
	public String getAccept() {
		return accept;
	}
	public void setAccept(String accept) {
		this.accept = accept;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public Map<String, String> getUrlMap() {
		return urlMap;
	}
	public void setUrlMap(Map<String, String> urlMap) {
		this.urlMap = urlMap;
	}
	
}
