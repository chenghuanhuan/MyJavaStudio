package com.xjj.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LuckyDraw {
	
	public static List<Map<String, String>> GenerateLuckyNumber(List<Map<String, String>> awards){
		List<Map<String, String>> result = null;//new ArrayList<>();
		
		//按照概率从大到小排序：
		result = awards.stream().sorted(
				(a1, a2) -> (Double.valueOf(a2.get("ratio"))).compareTo(Double.valueOf(a1.get("ratio"))))
				.collect(Collectors.toList());
		
		System.out.println(result);
		
		for(Map<String, String> a : result){
			String rStr = a.get("ratio");
			Double dRatio = Double.valueOf(rStr);
			int ratio = 0;
			int length = 0;
			if(dRatio>0){
				
				//BigDecimal r = new BigDecimal(ratio);
				length = rStr.length()-rStr.indexOf('.')-1;
				
				for(int i=0; i<length; i++){
					dRatio = dRatio*10;
				}
				ratio = dRatio.intValue();
			}
			
			System.out.println("rStr=" + rStr + ", length=" + length + ", iRatio=" + ratio);
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		List<Map<String, String>> awards = new ArrayList<>();
		
		Map<String, String> a0 = new HashMap<>();
		a0.put("name", "特等奖");
		a0.put("ratio", "0");
		awards.add(a0);
		
		Map<String, String> a1 = new HashMap<>();
		a1.put("name", "一等奖");
		a1.put("ratio", "0.0001");
		awards.add(a1);
		
		Map<String, String> a2 = new HashMap<>();
		a2.put("name", "二等奖");
		a2.put("ratio", "0.003");
		awards.add(a2);

		Map<String, String> a3 = new HashMap<>();
		a3.put("name", "三等奖");
		a3.put("ratio", "0.005");
		awards.add(a3);
		
		Map<String, String> a4 = new HashMap<>();
		a4.put("name", "四等奖");
		a4.put("ratio", "0.3");
		awards.add(a4);
		
		GenerateLuckyNumber(awards);
	}
}
