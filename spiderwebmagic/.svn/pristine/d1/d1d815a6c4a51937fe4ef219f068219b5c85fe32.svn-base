package com.ecmoho.base.exploration;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class TestHttpClient {

	public static void main(String[] args) {
		 CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();  
		 HttpGet httpGet = new HttpGet("https://mq.sycm.taobao.com/overview/reportIndex.json?cateId=50024153&dateRange=2016-03-16%7C2016-03-16&dateType=recent1&device=0&indexCode=uv%7CsearchUvCnt&seller=-1&sycmToken=57d0e3528&_=1458208249298"); 
				
//				site.addHeader("user-agent", schqHeaderBean.getUserAgent());
//				site.addHeader("accept", schqHeaderBean.getAccept());
//				site.addHeader("accept-language", schqHeaderBean.getAcceptLanguage());
//				site.addHeader("origin", schqHeaderBean.getOrgin());
//				site.addHeader("referer", "https://mq.sycm.taobao.com/industry/overview/overview.htm?_res_id_=199");
//				site.addHeader("cookie", schqHeaderBean.getCookie());
		 httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
		 httpGet.setHeader("accept", "*/*");
		 httpGet.setHeader("accept-language", "zh-CN,zh;q=0.8");
		 httpGet.setHeader("origin", "https://mq.sycm.taobao.com");
		 httpGet.setHeader("referer", "https://mq.sycm.taobao.com/industry/overview/overview.htm?_res_id_=199");
		 httpGet.setHeader("cookie", "thw=cn; miid=7793860282557482013; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; v=0; _tb_token_=loY4zvhRylOd9wA; uc1=tmb=1&cookie14=UoWyhuwZj%2FVc0g%3D%3D&existShop=true&cookie16=V32FPkk%2FxXMk5UvIbNtImtMfJQ%3D%3D&cookie21=UIHiLt3xSard&tag=7&cookie15=WqG3DMC9VAQiUQ%3D%3D&pas=0; uc3=sg2=BqtbvkqAyWh%2F02zuzLYZPBunxipgenHPP5raWIrUWU4%3D&nk2=r7ktwK2rBZy8RHTDJrU2tA%3D%3D&id2=UUwRkmwAHt8Wmw%3D%3D&vt3=F8dASmw1gybZfSo%2FAr0%3D&lg2=WqG3DMC9VAQiUQ%3D%3D; existShop=MTQ1ODIwOTcyNA%3D%3D; uss=VAWIxbjjtK7yh%2BfDCEL9zN%2FJWOaze0PxcH6U2J2STyLAdiNWggW%2BdxU%3D; lgc=%5Cu5929%5Cu987A%5Cu5927%5Cu836F%5Cu623F%5Cu65D7%5Cu8230%5Cu5E97; tracknick=%5Cu5929%5Cu987A%5Cu5927%5Cu836F%5Cu623F%5Cu65D7%5Cu8230%5Cu5E97; cookie2=3c2a8ea12fd594b5f5c8d01f26d3d816; sg=%E5%BA%9781; mt=np=&ci=0_1; cookie1=BqFgZ%2FNSE8o%2FQlacVuIgLAQqObpmkHU5p9X7k0ZjKFg%3D; unb=2418229518; skt=2c8a315a16e5caec; t=1db04b1e93264b71f762f05d3a26f0c3; _cc_=URm48syIZQ%3D%3D; tg=0; _l_g_=Ug%3D%3D; _nk_=%5Cu5929%5Cu987A%5Cu5927%5Cu836F%5Cu623F%5Cu65D7%5Cu8230%5Cu5E97; cookie17=UUwRkmwAHt8Wmw%3D%3D; l=ArGxbvV5PCFfQqAxT5fLQoTZQTNLhCUQ; _uacm_ac_s_tp_=1; _uacm_ac_s_ti_=%CC%EC%CB%B3%B4%F3%D2%A9%B7%BF%C6%EC%BD%A2%B5%EA; _uacm_ac_s_id_=116796949; _uacm_ac_u_nk_=%CC%EC%CB%B3%B4%F3%D2%A9%B7%BF%C6%EC%BD%A2%B5%EA");
		 try {
			HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();  
	            //响应状态  
	            System.out.println("status:" + httpResponse.getStatusLine());  
	            //判断响应实体是否为空  
	            if (entity != null) {  
	                System.out.println("contentEncoding:" + entity.getContentEncoding());  
	                System.out.println("response content:" + EntityUtils.toString(entity));  
	            } 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

}
