package com.ecmoho.sycm.schq.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.sycm.schq.exploration.SchqExploration;

/**
 * 
 * @author zhangjr 市场行情--商品店铺榜--商品详情
 */
@Component("schqSpdpbSpxqProcessor")
public class SchqSpdpbSpxqProcessor extends SchqProcessor {
	@Resource(name = "schqSpdpbSpxqProcessor")
	private SchqProcessor schqProcessor;
	@Resource(name = "schqSpdpbSpxqExploration")
	private SchqExploration schqExploration;
	@Value("13")
	private String accountIdArr;
	@Value("spdpb-spxq-spqs,spdpb-spxq-llly,spdpb-spxq-ylgjc")
	private String childAccountArr;
	@Value("1")
	private int days;

	public void run() {
		super.start(schqProcessor, schqExploration, accountIdArr,
				childAccountArr, days);
	}

	@Override
	public Site getSite() {
		Random random = new Random();
		return super.getSite().setSleepTime(random.nextInt(5) * 1000 + 10000);
	}

	@Override
	public void process(Page page) {
		System.out.println("hahahhaha");
		String jsonStr = page.getJson().toString();
		System.out.println(jsonStr);
		JSONObject finalTargetJsonObject = JSON.parseObject(jsonStr);
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dataListpc = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dataListwl = new ArrayList<Map<String, String>>();
		Map<String, String> dataMap = null;
		JSONArray dataJsonArray = null;
		JSONObject dataJsonObject = null;
		String device = "0";
		Map<String, String> urlMap = schqHeaderBean.getUrlMap();
		String childAccount = urlMap.get("childAccount");
		if (null == finalTargetJsonObject.getString("content")) {
			System.out.println("又被限制了，请稍后再试");
		} else {
			if ("0".equals(finalTargetJsonObject.getJSONObject("content")
					.getString("code"))) {
				if (childAccount.equals("spdpb-spxq-spqs")) {
					dataJsonObject = finalTargetJsonObject.getJSONObject(
							"content").getJSONObject("data");
					dataMap = getDataMap(dataMap, urlMap);
					dataMap.put("payOrdCntList",
							dataJsonObject.getString("payOrdCntList"));
					dataMap.put("payByrRateIndexList",
							dataJsonObject.getString("payByrRateIndexList"));
					dataMap.put("payItemQtyList",
							dataJsonObject.getString("payItemQtyList"));
					dataList.add(dataMap);
				} else if (childAccount.equals("spdpb-spxq-ylgjc")) {
					dataJsonArray = finalTargetJsonObject
							.getJSONObject("content").getJSONObject("data")
							.getJSONArray("wlSeList");
					for (int i = 0; i < dataJsonArray.size(); i++) {
						dataJsonObject = dataJsonArray.getJSONObject(i);
						dataMap = getDataMap(dataMap, urlMap);
						for (String key : dataJsonObject.keySet()) {
							dataMap.put(key, dataJsonObject.getString(key));
						}
						dataMap.put("SeList", "wlSeList");
						dataListwl.add(dataMap);
					}
					dataJsonArray = finalTargetJsonObject
							.getJSONObject("content").getJSONObject("data")
							.getJSONArray("pcSeList");
					for (int i = 0; i < dataJsonArray.size(); i++) {
						dataJsonObject = dataJsonArray.getJSONObject(i);
						dataMap = getDataMap(dataMap, urlMap);
						for (String key : dataJsonObject.keySet()) {
							dataMap.put(key, dataJsonObject.getString(key));
						}
						dataMap.put("SeList", "pcSeList");
						dataListpc.add(dataMap);
					}
				} else {
					dataJsonArray = finalTargetJsonObject.getJSONObject(
							"content").getJSONArray("data");
					for (int i = 0; i < dataJsonArray.size(); i++) {
						dataJsonObject = dataJsonArray.getJSONObject(i);
						dataMap = getDataMap(dataMap, urlMap);
						for (String key : dataJsonObject.keySet()) {
							dataMap.put(key, dataJsonObject.getString(key));
						}
						device = dataMap.get("device");
						dataList.add(dataMap);
					}
				}
			} else {
				System.out.println("出问题了，手动查查看吧");
			}
		}

		switch (childAccount) {
		// 商品店铺榜_商品详情_商品趋势
		case "spdpb-spxq-spqs":
			schqDbcom.addList(dataList, "commodityshop_detail_commoditytrend");
			break;
		// 商品店铺榜_商品详情_流量来源
		case "spdpb-spxq-llly":
			if (device.equals("1")) {
				schqDbcom.addList(dataList,
						"commodityshop_detail_flowsources_pc");
			} else if (device.equals("2")) {
				schqDbcom.addList(dataList,
						"commodityshop_detail_flowsources_wl");
			}
			break;
		// 商品店铺榜_商品详情_引流关键词
		case "spdpb-spxq-ylgjc":
			schqDbcom.addList(dataListwl, "commodityshop_detail_keyword_wl");
			schqDbcom.addList(dataListpc, "commodityshop_detail_keyword_pc");

			break;
		default:
			break;
		}
	}

	// 获取数据附加信息
	public static Map<String, String> getDataMap(Map<String, String> dataMap,
			Map<String, String> urlMap) {
		dataMap = new HashMap<String, String>();
		dataMap.put("accountid", urlMap.get("accountid"));
		dataMap.put("create_at", urlMap.get("create_at"));
		dataMap.put("level", urlMap.get("level"));
		dataMap.put("item1", urlMap.get("item1"));
		dataMap.put("item2", urlMap.get("item2"));
		dataMap.put("item3", urlMap.get("item3"));
		if (!"-1".equals(urlMap.get("device"))) {
			dataMap.put("device", urlMap.get("device"));
		}
		dataMap.put("itemTitle", urlMap.get("itemTitle"));
		dataMap.put("log_at", urlMap.get("log_at"));
		return dataMap;
	}
}
