package com.xjj.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by XuJijun on 2017-06-06.
 */
@Service
public class HttpService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	CloseableHttpClient httpClient;

	public HttpService() {
		//logger.warn("缺省构造方法被调用。");
	}

	/**
	 * 某些特殊要求的客户端，可以使用这个方法new，同时设置超时时间
	 * @param timeout 毫秒
	 */
	public HttpService(int timeout) {
		//logger.warn("timeout构造方法被调用。");

		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		//客户端和服务器建立连接的timeout
		requestConfigBuilder.setConnectTimeout(timeout);
		//从连接池获取连接的timeout
		requestConfigBuilder.setConnectionRequestTimeout(timeout);
		//连接建立后，request没有回应的timeout
		requestConfigBuilder.setSocketTimeout(timeout);
		RequestConfig requestConfig = requestConfigBuilder.build();

		SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
		socketConfigBuilder.setSoTimeout(timeout);
		SocketConfig socketConfig = socketConfigBuilder.build();

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		clientBuilder.setDefaultSocketConfig(socketConfig);
		clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
		this.httpClient = clientBuilder.build();
	}

	/**
	 * 使用缺省的配置发送GET请求
	 * @param url
	 * @return
	 */
	public HttpResult get(String url){
		return this.get(url, null);
	}

	/**
	 * 使用缺省的配置发送GET请求
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpResult get(String url, Map<String, Object> params){
		return this.get(url, params, null);
	}

	/**
	 * 使用缺省的配置发送GET请求
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public HttpResult get(String url, Map<String, Object> params, Map<String, String> headers) {
		//设置请求参数到url中
		if(!CollectionUtils.isEmpty(params)){// params!=null && !params.isEmpty()){
			try {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String pKey: params.keySet()) {
					paramList.add(new BasicNameValuePair(pKey, params.get(pKey).toString()));
				}

				url = url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(paramList, "UTF-8"));
				//logger.debug("url: {}", url);
			} catch (IOException e) {
				logger.error("创建参数时发生错误", e);
			}
		}

		HttpGet request = new HttpGet(url);
		//logger.debug("url: {}", url);

		//设置headers
		if(!CollectionUtils.isEmpty(headers)){// headers!=null && !headers.isEmpty()){
			for(String hKey: headers.keySet()){
				request.setHeader(hKey, headers.get(hKey)); //替换或添加headers
			}
		}

		return this.executeRequest(request);
	}

	/**
	 * GET，顺便把结果转成JsonNode
	 * @param url
	 * @return
	 */
	public JsonNode getJsonNode(String url){
		HttpGet httpGet =  new HttpGet(url);

		//开始发送请求
		try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();

			if(statusCode != HttpStatus.SC_OK){
				logger.warn("Response not OK: url={}, statusCode={}", url, statusCode);
			}

			if (entity != null) {
				InputStream inputStream = response.getEntity().getContent();
				//logger.debug(EntityUtils.toString(entity));
				return objectMapper.readTree(inputStream);
			}

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 使用缺省的配置发送POST请求
	 * @param url
	 * @return
	 */
	public HttpResult post(String url){
		HttpPost post = new HttpPost(url);
		return this.executeRequest(post);
	}

	/**
	 * 使用缺省的配置发送POST请求，以json发生提交数据
	 * @param url
	 * @param params
	 * @return
	 */
	public <T> HttpResult postJson(String url, T params){
		HttpPost post = new HttpPost(url);

		if(params!=null){
			try {
				post.setHeader("Content-Type", "application/json;charset=UTF-8");
				StringEntity stringEntity;
				if(params instanceof String){
					stringEntity = new StringEntity((String)params, "UTF-8");
				}else {
					stringEntity = new StringEntity(objectMapper.writeValueAsString(params), "UTF-8");
				}
				stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
				post.setEntity(stringEntity);
			} catch (Exception e) {
				logger.error("创建参数时发生错误", e);
			}
		}

		return this.executeRequest(post);
	}

	public HttpResult postForm(String url, Map<String, String> form, Map<String, String> headers){
		HttpPost post = new HttpPost(url);

		if(!CollectionUtils.isEmpty(form)){
			try {
				// 创建参数队列
				List<NameValuePair> params = new ArrayList<>();
				for(String fKey: form.keySet()){
					params.add(new BasicNameValuePair(fKey, form.get(fKey)));
				}

				//参数转码
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");

				post.setEntity(entity);
			} catch (Exception e) {
				logger.error("创建参数时发生错误", e);
			}
		}

		//设置headers
		if(!CollectionUtils.isEmpty(headers)){
			for(String hKey: headers.keySet()){
				post.setHeader(hKey, headers.get(hKey)); //替换或添加headers
			}
		}
		System.out.println("Executing request " + post.getRequestLine());
		return this.executeRequest(post);
	}

	/**
	 * 使用缺省的配置发送POST请求，以form方式提交数据
	 * @param url
	 * @param form
	 * @return
	 */
	public HttpResult postForm(String url, Map<String, String> form){
		HttpPost post = new HttpPost(url);

		if(!CollectionUtils.isEmpty(form)){// form!=null && !form.isEmpty()){
			try {
				// 创建参数队列
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for(String fKey: form.keySet()){
					params.add(new BasicNameValuePair(fKey, form.get(fKey)));
				}

				//参数转码
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				post.setEntity(entity);
			} catch (Exception e) {
				logger.error("创建参数时发生错误", e);
			}
		}

		return this.executeRequest(post);
	}

	/**
	 * 从response中提取相关信息，自动支持gzip的结果
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private HttpResult extractResultInfo(CloseableHttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		HttpResult result = new HttpResult();
		int statusCode = response.getStatusLine().getStatusCode();
		result.setStatus(statusCode);

		if(statusCode!= HttpStatus.SC_OK){
			logger.warn("Response not OK: statusCode={}", statusCode);
		}

		if (entity != null) {
			result.setPayload(EntityUtils.toString(entity, "UTF-8")); //解决中文乱码问题
		}
		return result;
	}

	/**
	 * 执行请求
	 * @param request
	 * @return
	 */
	private HttpResult executeRequest(HttpUriRequest request) {
		HttpResult result = null;
		logger.debug("request: {}", request.toString());
		System.out.println("request: " + request.toString());

		//开始发送请求
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			result = this.extractResultInfo(response);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}
}
