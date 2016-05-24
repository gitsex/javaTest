package com.ecmoho.sycm.schq.entities;

import org.springframework.stereotype.Component;

@Component("redisProcessorBean")
public class RedisProcessorBean {
	//店铺ID
    private String accountid;
    //店铺名称
    private String accountname;
    //执行processor（与注解名称一致）
    private String processor;
    //执行processor描述
    private String processordesc;
    //执行周期
    private String executecycle;
    //总记录数
    private String recordcount;
    //开始执行日期
    private String startdate;
    //存储url信息
    private String urlDataStr;
    //该条计划任务执行状态
    private String executeStatus;
    
    //覆盖toString方法
  	public String toString() {
  		return accountid+","+accountname+","+processor+","+processordesc+","+executecycle+","+recordcount+","+startdate+","+executeStatus;
  	}
  	//构造方法1
  	public RedisProcessorBean(){
  		
  	}
    //构造方法2
  	public RedisProcessorBean(String redisProcessorKey){
  		String processokey[]=redisProcessorKey.split(",");
  		this.accountid=processokey[0];
  		this.accountname=processokey[1];
  		this.processor=processokey[2];
  		this.processordesc=processokey[3];
  		this.executecycle=processokey[4];
  		this.recordcount=processokey[5];
  		this.startdate=processokey[6];
  		this.executeStatus=processokey[7];
  	}
	public String getAccountid() {
		return accountid;
	}
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public String getProcessordesc() {
		return processordesc;
	}
	public void setProcessordesc(String processordesc) {
		this.processordesc = processordesc;
	}
	public String getExecutecycle() {
		return executecycle;
	}
	public void setExecutecycle(String executecycle) {
		this.executecycle = executecycle;
	}
	public String getRecordcount() {
		return recordcount;
	}
	public void setRecordcount(String recordcount) {
		this.recordcount = recordcount;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getUrlDataStr() {
		return urlDataStr;
	}
	public void setUrlDataStr(String urlDataStr) {
		this.urlDataStr = urlDataStr;
	}
	public String getExecuteStatus() {
		return executeStatus;
	}
	public void setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
	}
	
	   
}
