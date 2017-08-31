package com.xjj.http;

import com.xjj.util.DateUtils;
import com.xjj.util.RandomUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XuJijun on 2017-08-30.
 */
public class PostTest {
	private static String getTimeString() {
		return DateUtils.getCurrentDateString(DateUtils.DFHHmmssSSS);
	}

	private static void logMsg(String format, Object... args){
		System.out.println(getTimeString() + " [myLog] " + String.format(format, args));
	}

	public static void main(String[] args){
		int times = 30;	//总次数
		int maxIntervalMinutes = 1; //Minutes

		String url = "http://www.abc.com/aaa";
		Map<String, String> form = new HashMap<>(1);
		Map<String, String> headers = new HashMap<>();

		form.put("id", "aabbcc");

		headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
		String ipPrefix = "113.109.";

		logMsg("Program started, will hit for %s times, with maximum interval %s minutes", times, maxIntervalMinutes);

		long maxInterval = maxIntervalMinutes*60*1000;
		int succCount = 0;
		int failCount = 0;

		while(succCount + failCount < times){
			logMsg("第 %s次开始……", succCount + failCount +1);
			HttpService httpService = new HttpService(30000);
			int ip3 = RandomUtils.getRandomInt(2, 254);
			int ip4 = RandomUtils.getRandomInt(2, 254);
			String ip = ipPrefix + ip3 + "." + ip4;
			headers.put("X-Forwarded-For", ip);
			headers.put("X-Real-IP", ip);
			headers.put("HTTP_CLIENT_IP", ip);

			HttpResult r = httpService.postForm(url, form, headers);

			logMsg("result: %s", r.getPayload());

			if(r.getStatus()==200){
				/*String websiteHitCount = RegexUtils.getFirstMatch(r.getMsg(), "\\d+位新增朋友的阅读");
				websiteHitCount = RegexUtils.findFirstNumber(websiteHitCount);
				logMsg("sucess result: 有%s位新增朋友的阅读。", websiteHitCount);*/
				succCount ++;
			}else {
				logMsg("fail result: %s", r.toString());
				failCount ++;
			}

			if(succCount + failCount == times){
				logMsg("第%s次结束，全部结束。Succeeded: %s, Failed: %s", succCount + failCount, succCount, failCount);
				break;
			}

			long interval = Math.abs(RandomUtils.getRandomLong(maxInterval));
			logMsg("第%s次结束，等待 %s秒后开始下一次", succCount + failCount, interval/1000);
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
