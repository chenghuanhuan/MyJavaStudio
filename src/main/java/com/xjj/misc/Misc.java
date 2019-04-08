package com.xjj.misc;

import org.codehaus.jettison.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Misc {
	public static void main(String[] args) throws MalformedURLException {
		Misc m = new Misc();
//		m.test1();
//		m.test2();
//		m.test3();
//		m.maxInteger();
		m.plusPlusTest();
	}

	private void plusPlusTest(){
		int i=0, j=0;
		int x=i++, y=++j;
		System.out.println("i: " + i + "; x:" + x);
		System.out.println("j: " + j + "; y:" + y);
	}

	private void test1() throws MalformedURLException {
		final String[] URL_NAMES = { "http://javapuzzlers.com",
				"http://apache2-snort.skybar.dreamhost.com",
				"http://www.google.com",
				"http://javapuzzlers.com",
				"http://findbugs.sourceforge.net",
				"http://www.cs.umd.edu" };
		
		Set<URL> favorites = new HashSet<URL>();
		for (String urlName : URL_NAMES)
			favorites.add(new URL(urlName));
		System.out.println(favorites.size());
		
		
	}

	private void test2() {
        Random rnd = new Random();
        boolean toBe = rnd.nextBoolean();
        Number result = (toBe || !toBe) ?
                new Integer(3) : new Float(1);
        System.out.println(result);
    }
	
	private void test3(){
		Map<String, Double> m = new HashMap<>();
		m.put("aaaa", 123456.789);
		JSONObject jsonObject = new JSONObject(m);
		System.out.println(jsonObject);
		
		String[] s = new String[]{"a","b"};
		
		List<String> l = Arrays.asList(s);
		System.out.println(l);
	}

	private void maxInteger() {
		System.out.println(Integer.MAX_VALUE);
	}
}
