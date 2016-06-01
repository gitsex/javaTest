package com.ecmoho.sycm.schq.exploration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.Util.UrlUtil;

/**
 * @author zhangjr 市场行情--商品店铺榜--商品详情
 */
@Component("schqSpdpbSpxqExploration")
public class SchqSpdpbSpxqExploration extends SchqExploration {
	private List<HashMap<String, String>> urlList = new ArrayList<HashMap<String, String>>();
	private String childAccountArr2 = "spdpb-hyld-rxsp,spdpb-hyld-llsp,spdpb-ppld-rxsp,spdpb-ppld-llsp,spdpb-cpld-rxsp,spdpb-cpld-llsp";
	private List<HashMap<String, String>> itemBeanList = new ArrayList<>();
	private HashMap<String, String> urlMap = null;
	private String nowDateStr = null;
	private String accountid = null;
	private String yesterdayday = null;

	@Override
	public List<HashMap<String, String>> getUrlList(String account,
			String childAccountArr, int days) {
		/**
		 * childAccountArr:spdpb-spxq-spqs,spdpb-spxq-llly,spdpb-spxq-ylgjc
		 */
		// 获取行业目录
		List<HashMap<String, String>> hymlList = getHyml(schqDbcom,
				schqHeaderBean);
		// 返回URL集合信息
		urlList = new ArrayList<>();
		itemBeanList = new ArrayList<>();
		// 存储日期
		nowDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		Map<String, Object> taskMap = schqDbcom.getSpider(account);
		accountid = StringUtil.objectVerString(taskMap.get("id"));
		for (int d = 1; d <= Integer.valueOf(days); d++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -d);
			// d天之前
			yesterdayday = new SimpleDateFormat("yyyy-MM-dd").format(cal
					.getTime());
			// getItemId(yesterdayday);
			getItemIdTest(yesterdayday);
			// 获取需要抓取URL列表
			List<Map<String, Object>> spdpbList = schqDbcom
					.getSpiderChildList(childAccountArr);
			for (Map<String, Object> spdpbHyldMap : spdpbList) {
				String geturl = StringUtil.objectVerString(spdpbHyldMap
						.get("geturl"));
				String childAccount = StringUtil.objectVerString(spdpbHyldMap
						.get("child_account"));
				for (Map<String, String> mlMap : hymlList) {
					String targetUrl = geturl.replaceAll("##D##", yesterdayday)
							.replaceAll("##CID##", mlMap.get("id"))
							.replaceAll("\\|", "%7C");
					if (childAccount.equals("spdpb-spxq-ylgjc")) {
						for (int i = 0; i < itemBeanList.size(); i++) {
							String finalUrl = targetUrl.replaceAll("##IID##",
									itemBeanList.get(i).get("itemId"));
							makeUrlList(mlMap, finalUrl, childAccount,
									itemBeanList.get(i), -1);
						}
					} else if (childAccount.equals("spdpb-spxq-spqs")) {
						for (int i = 0; i < itemBeanList.size(); i++) {
							String targetUrl1 = targetUrl.replaceAll("##IID##",
									itemBeanList.get(i).get("itemId"));
							for (int j = 0; j < 3; j++) {
								String finalUrl = targetUrl1.replaceAll(
										"##DE1##", j + "");
								makeUrlList(mlMap, finalUrl, childAccount,
										itemBeanList.get(i), j);
							}
						}
					} else if (childAccount.equals("spdpb-spxq-llly")) {
						for (int i = 0; i < itemBeanList.size(); i++) {
							String targetUrl1 = targetUrl.replaceAll("##IID##",
									itemBeanList.get(i).get("itemId"));
							for (int j = 1; j < 3; j++) {
								String finalUrl = targetUrl1.replaceAll(
										"##DE##", j + "");
								makeUrlList(mlMap, finalUrl, childAccount,
										itemBeanList.get(i), j);
							}
						}
					}
				}
			}
		}
		return urlList;
	}

	private void makeUrlList(Map<String, String> mlMap, String finalUrl,
			String childAccount, HashMap<String, String> item, int i) {
		urlMap = new HashMap<>();
		urlMap.put("childAccount", childAccount);
		urlMap.put("targetUrl", finalUrl);
		urlMap.put("accountid", accountid);
		urlMap.put("create_at", yesterdayday);
		urlMap.put("level", mlMap.get("level"));
		urlMap.put("item1", mlMap.get("item1"));
		urlMap.put("item2", mlMap.get("item2"));
		urlMap.put("item3", mlMap.get("item3"));
		if (i != -1) {
			urlMap.put("device", i + "");
		} else {
			urlMap.put("device", "-1");
		}
		urlMap.put("itemTitle", item.get("itemTitle"));
		urlMap.put("log_at", nowDateStr);
		urlList.add(urlMap);
	}

	/**
	 * 获取itemId
	 * 
	 * @param childAccountArr
	 * @param days
	 */
	private void getItemId(String days) {
		itemBeanList = new ArrayList<>();
		// 获取行业目录
		/**
		 * 其中有id 就是cateID和categoryID
		 * */
		List<HashMap<String, String>> hymlList = getHyml(schqDbcom,
				schqHeaderBean);
		// 获取需要抓取URL列表
		List<Map<String, Object>> spdpbList = schqDbcom
				.getSpiderChildList(childAccountArr2);
		for (Map<String, Object> spdpbHyldMap : spdpbList) {
			String geturl = StringUtil.objectVerString(spdpbHyldMap
					.get("geturl"));
			String childAccount = StringUtil.objectVerString(spdpbHyldMap
					.get("child_account"));
			for (Map<String, String> mlMap : hymlList) {
				// 循环终端来源（0,所有终端，1，PC端，2，无线端）
				for (int j = 0; j <= 2; j++) {
					// 循环支付金额分段类型（0,分时段趋势图，1，时段累计图）
					for (int k = -1; k <= 1; k++) {
						if (childAccount.equals("spdpb-hyld-rxsp")
								|| childAccount.equals("spdpb-hyld-llsp")) {
							String finalTargetUrl = geturl
									.replaceAll("##D##", days)
									.replaceAll("##CID##", mlMap.get("id"))
									.replaceAll("##DE##", j + "")
									.replaceAll("##S##", k + "")
									.replaceAll("\\|", "%7C");
							getItemIdByUrl(finalTargetUrl);
						} else {
							Map<String, String> pplbMap = getPplb(schqDbcom,
									schqHeaderBean, mlMap.get("id"), days);
							for (Entry<String, String> entry : pplbMap
									.entrySet()) {
								String brandId = entry.getKey();// 品牌ID
								if (childAccount.equals("spdpb-ppld-rxsp")
										|| childAccount
												.equals("spdpb-ppld-llsp")) {
									urlMap = new HashMap<String, String>();
									String finalTargetUrl = geturl
											.replaceAll("##D##", days)
											.replaceAll("##CID##",
													mlMap.get("id"))
											.replaceAll("##BID##", brandId)
											.replaceAll("##DE##", j + "")
											.replaceAll("##S##", k + "");
									getItemIdByUrl(finalTargetUrl);
								} else if (childAccount
										.equals("spdpb-cpld-rxsp")
										|| childAccount
												.equals("spdpb-cpld-llsp")) {
									// 获取产品详情--品牌列表--产品列表
									List<HashMap<String, String>> cplbList = getCpfxCpxqModels(
											schqDbcom, schqHeaderBean,
											mlMap.get("id"), brandId, days);
									// 遍历产品列表
									for (int i = 0; cplbList != null
											&& i < cplbList.size(); i++) {
										String modelId = cplbList.get(i).get(
												"modelId");
										String spuId = cplbList.get(i).get(
												"spuId");

										// 品牌分析_品牌详情_品牌概况
										String finalTargetUrl = geturl
												.replaceAll("##BID##", brandId)
												.replaceAll("##CID##",
														mlMap.get("id"))
												.replaceAll("##MID##", modelId)
												.replaceAll("##D##", days)
												.replaceAll("##DE##", j + "")
												.replaceAll("##SID##", spuId)
												.replaceAll("##S##", k + "");
										getItemIdByUrl(finalTargetUrl);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void getItemIdByUrl(String finalTargetUrl) {
		String response = UrlUtil.getUrlString(schqHeaderBean, finalTargetUrl);
		JSONObject jsonobject = JSONObject.parseObject(response).getJSONObject(
				"content");
		System.out.println("finalTargetUrl: " + finalTargetUrl);
		System.out.println("response: " + response);
		if (JSONObject.parseObject(response).getString("content") != null) {
			JSONObject obj = jsonobject.getJSONObject("data");
			JSONArray array = obj.getJSONArray("data");
			if (null != array && array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = array.getJSONObject(i);
					HashMap<String, String> map = new HashMap<>();
					if (itemBeanList != null && itemBeanList.size() > 0) {
						boolean flag = false;
						for (int s = 0; s < itemBeanList.size(); s++) {
							if (itemBeanList.get(s).equals(
									object.getString("itemId"))) {
								flag = true;
							}
						}
						if (!flag) {
							map.put("itemId", object.getString("itemId"));
							map.put("itemTitle", object.getString("itemTitle"));
							itemBeanList.add(map);
						}
					} else if (itemBeanList == null || itemBeanList.size() == 0) {
						map.put("itemId", object.getString("itemId"));
						map.put("itemTitle", object.getString("itemTitle"));
						itemBeanList.add(map);
					}
				}
			}
		} else {
			System.out.println("获取数据失败，请稍后重试");
		}
	}

	private void getItemIdTest(String days) {
		itemBeanList = new ArrayList<>();
		// 获取行业目录
		/**
		 * 其中有id 就是cateID和categoryID
		 * */
		List<HashMap<String, String>> hymlList = getHyml(schqDbcom,
				schqHeaderBean);
		// 获取需要抓取URL列表
		List<Map<String, Object>> spdpbList = schqDbcom
				.getSpiderChildList(childAccountArr2);
		Map<String, Object> spdpbHyldMap = spdpbList.get(0);
		String childAccount = StringUtil.objectVerString(spdpbHyldMap
				.get("child_account"));
		String geturl = StringUtil.objectVerString(spdpbHyldMap.get("geturl"));
		Map<String, String> mlMap = hymlList.get(0);
		if (childAccount.equals("spdpb-hyld-rxsp")
				|| childAccount.equals("spdpb-hyld-llsp")) {
			String finalTargetUrl = geturl.replaceAll("##D##", days)
					.replaceAll("##CID##", mlMap.get("id"))
					.replaceAll("##DE##", 0 + "").replaceAll("##S##", -1 + "")
					.replaceAll("\\|", "%7C");
			getItemIdByUrl(finalTargetUrl);
		} else {
			Map<String, String> pplbMap = getPplb(schqDbcom, schqHeaderBean,
					mlMap.get("id"), days);
			for (Entry<String, String> entry : pplbMap.entrySet()) {
				String brandId = entry.getKey();// 品牌ID
				if (childAccount.equals("spdpb-ppld-rxsp")
						|| childAccount.equals("spdpb-ppld-llsp")) {
					urlMap = new HashMap<String, String>();
					String finalTargetUrl = geturl.replaceAll("##D##", days)
							.replaceAll("##CID##", mlMap.get("id"))
							.replaceAll("##BID##", brandId)
							.replaceAll("##DE##", 0 + "")
							.replaceAll("##S##", -1 + "");
					getItemIdByUrl(finalTargetUrl);
				} else if (childAccount.equals("spdpb-cpld-rxsp")
						|| childAccount.equals("spdpb-cpld-llsp")) {
					// 获取产品详情--品牌列表--产品列表
					List<HashMap<String, String>> cplbList = getCpfxCpxqModels(
							schqDbcom, schqHeaderBean, mlMap.get("id"),
							brandId, days);
					// 遍历产品列表
					for (int i = 0; cplbList != null && i < cplbList.size(); i++) {
						String modelId = cplbList.get(i).get("modelId");
						String spuId = cplbList.get(i).get("spuId");

						// 品牌分析_品牌详情_品牌概况
						String finalTargetUrl = geturl
								.replaceAll("##BID##", brandId)
								.replaceAll("##CID##", mlMap.get("id"))
								.replaceAll("##MID##", modelId)
								.replaceAll("##D##", days)
								.replaceAll("##DE##", 0 + "")
								.replaceAll("##SID##", spuId)
								.replaceAll("##S##", -1 + "");
						getItemIdByUrl(finalTargetUrl);
					}
				}
			}
		}
	}
}
