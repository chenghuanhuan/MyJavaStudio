package com.xjj.http;

import com.xjj.util.DateUtils;
import com.xjj.util.RandomUtils;
import com.xjj.util.RegexUtils;

public class GetAccessTest2 {
	private static String getTimeString() {
		return DateUtils.getCurrentDateString(DateUtils.DFHHmmssSSS);
	}
	
	private static void logMsg(String format, Object... args){
		System.out.println(getTimeString() + " [myLog] " + String.format(format, args));
	}
	
	public static void main(String[] args) {
		int times = 5;	//总次数
		int maxIntervalMinutes = 1; //Minutes
		String url = "http://www.maimemo.com/share/page/?uid=727782&pid=624&from=singlemessage&isappinstalled=1";

		logMsg("Program started, will hit for %s times, with maximum interval %s minutes", times, maxIntervalMinutes);

		long maxInterval = maxIntervalMinutes*60*1000;
		int succCount = 0;
		int failCount = 0;

		while(succCount + failCount < times){
			logMsg("第 %s次开始……", succCount + failCount +1);
			HttpResult r = HttpHelper.doGetWithRandoms(url);
			if(r.getCode()==200){
				String websiteHitCount = RegexUtils.getFirstMatch(r.getMsg(), "\\d+位新增朋友的阅读");
				websiteHitCount = RegexUtils.findFirstNumber(websiteHitCount);
				logMsg("sucess result: 有%s位新增朋友的阅读。", websiteHitCount);
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
