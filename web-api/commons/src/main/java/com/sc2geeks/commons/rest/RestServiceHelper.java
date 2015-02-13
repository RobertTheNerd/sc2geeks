package com.sc2geeks.commons.rest;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 8/1/12
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class RestServiceHelper
{

	public static <T> T restGet(String url, Class<T> classOfT)
	{
		return restGet(url, classOfT, null);
	}
	public static <T> T restGet(String url, Class<T> classOfT, Type type)
	{
		try
		{
			HttpClient httpclient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(url);

			HttpResponse response = httpclient.execute(httpGet);

			if (classOfT == null)
			{
				return null;
			}

			return getHttpResponseEntity(response, classOfT, type);
		} catch (Exception e)
		{
			return null;
		}
	}


	public static <T> T restPost(String url, Object postObj, Class<T> classOfT)
	{
		try
		{
			Gson gson = new Gson();
			String jsonString = gson.toJson(postObj);

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(jsonString));
			HttpResponse response = httpclient.execute(httpPost);

			if (classOfT == null)
			{
				return null;
			}
			return getHttpResponseEntity(response, classOfT);

		} catch (Exception e)
		{
			return null;
		}
	}
	private static <T> T getHttpResponseEntity(HttpResponse response, Class<T> classOfT, Type type)
	{
		try
		{
			Gson gson = new Gson();
			HttpEntity entity = response.getEntity();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			if (entity != null)
			{
				InputStream inStream = entity.getContent();
				int l;
				byte[] tmp = new byte[2048];
				while ((l = inStream.read(tmp)) != -1)
				{
					outputStream.write(tmp, 0, l);
				}
			}

			String output = new String(outputStream.toByteArray(), "UTF-8");
			T val = (type == null) ? gson.fromJson(output, classOfT) : (T) gson.fromJson(output, type);
			return val;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	private static <T> T getHttpResponseEntity(HttpResponse response, Class<T> classOfT)
	{
		return getHttpResponseEntity(response, classOfT, null);
	}
}
