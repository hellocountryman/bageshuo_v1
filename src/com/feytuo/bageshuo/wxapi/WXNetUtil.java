package com.feytuo.bageshuo.wxapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * @author feytuo
 * 
 *       以同步方式发送Http请求
 */
public class WXNetUtil {

	private final String BASE_URL  = "https://api.weixin.qq.com/sns";
	private int timeoutConnection = 5*1000;//由网络问题引起的连接超时
	private int timeoutSocket = 10*1000;//服务器响应超时
	private HttpParams httpParams;
	private HttpClient httpClient;
	private String apiUrl;
	private String charset = "UTF-8";
	public WXNetUtil (String url) {
		initClient(url);
	}
	
	public WXNetUtil (String url, String charset) {
		initClient(url);
		this.charset = charset; // set charset
	}
	
	private void initClient (String url) {
		// initialize API URL
		this.apiUrl = BASE_URL + url;
		// set client timeout
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);// Set the timeout in
																				// milliseconds
																				// until a
																				// connection is
																				// established.
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);// Set the default socket timeout
																	// (SO_TIMEOUT) // in milliseconds which is
																	// the timeout for waiting for data.
																	// init client
		httpClient = new DefaultHttpClient(httpParams);
	}
	/**
	 * 通过GET方式发送请求
	 * 
	 * @param url
	 *            URL地址
	 * @param params
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public String get(String params) throws Exception {
		String response = null; // 返回信息
		// 拼接请求URL
		if (null != params && !params.equals("")) {
			apiUrl += "?" + params;
		}
		Log.i("client", "get/url:"+apiUrl);
		// 创建GET方法的实现
		HttpGet httpGet = new HttpGet(apiUrl);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) // SC_OK = 200
			{
				// 获得返回结果
				response = EntityUtils.toString(httpResponse.getEntity(),charset);
			} else {
				response = "返回码：" + statusCode;
			}
			Log.i("client", "get/response:"+response);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	/**
	 * 通过POST方式发送请求
	 * 
	 * @param url
	 *            URL地址
	 * @param params
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public String post(HashMap<String , Object> urlParams) throws Exception {
		String response = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		Log.i("client", "post/url:"+apiUrl);
		if (urlParams.size() >= 0) {
			// 设置httpPost请求参数
			httpPost.setEntity(new UrlEncodedFormEntity(
					buildNameValuePair(urlParams), charset));
		}
		// 使用execute方法发送HTTP Post请求，并返回HttpResponse对象
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			// 获得返回结果
			response = EntityUtils.toString(httpResponse.getEntity(),charset);
		} else {
			response = "返回码：" + statusCode;
		}
		Log.i("client", "post/response:"+response);
		return response;
	}

	/**
	 * 把Parameter类型集合转换成NameValuePair类型集合
	 * 
	 * @param params
	 *            参数集合
	 * @return
	 */
	private List<BasicNameValuePair> buildNameValuePair(HashMap<String , Object> urlParams) {
		List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
		// get post parameters
		Iterator<Entry<String, Object>> it = urlParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			result.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
		}
		return result;
	}
	
}
