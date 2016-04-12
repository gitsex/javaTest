package com.ecmoho.base.exploration;

import java.util.HashMap;
import java.util.List;

public interface BaseUrlExploration {
	  //根据url链接获取数据
	/*
	 * account 店铺账号
	 * childAccount 链接子账号
	 * days 查询天数 
	 */
    public List<HashMap<String, String>> getUrlList(String account,String childAccount,int days);

}
