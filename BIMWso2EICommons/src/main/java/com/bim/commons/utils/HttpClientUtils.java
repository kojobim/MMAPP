package com.bim.commons.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.RequestDTO;
import com.google.gson.Gson;


public class HttpClientUtils {


	private HttpClientUtils() { }
	
	public static String postPerform(RequestDTO request) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			final URIBuilder uriBuilder = new URIBuilder(request.getUrl());
			
			HttpPost post = new HttpPost(uriBuilder.build());
			StringEntity entity = new StringEntity(request.getMessage().getBody());
			System.out.println("&&&&&Message&&&&" + new Gson().toJson(request.getMessage()));
			post.setEntity(entity);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-Type", "application/json");
			CloseableHttpResponse response = client.execute(post);
			InputStream inputStream = response.getEntity().getContent();
			String json = IOUtils.toString(inputStream);
			client.close();
			return json;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getStringContent(Request request) {
		return getStringFromInputStream(request.getMessageContentStream());
	}
	
	private static String getStringFromInputStream(InputStream inputStream) {
		BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String result;
        try {
            int data;
            while ((data = bis.read()) != -1) {
                bos.write(data);
            }
            result = bos.toString();
        } catch (IOException ioe) {
            return "";
        } finally {
            try {
                bos.close();
            } catch (IOException ignored) {

            }
        }
        return result;
	}
	
}
