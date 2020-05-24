package com.bean.classicmini.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class ClassicMiniNetworking {
    private static StringBuilder getParameters(HashMap<String, String> args){
        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : args.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(args.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }
        
        return sbParams;
    }
    
    private static HttpURLConnection getConnection(String url, StringBuilder params){
        HttpURLConnection connection = null;
        try{
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);

            connection.connect();

            String paramsString = params.toString();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return connection;
    }
    
    private static String getPostOutput(HttpURLConnection connection){
        try {
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return "Error";
    }
    
    public static String runPostRequest(String url, HashMap<String, String> args){
        StringBuilder parameters = getParameters(args);
        HttpURLConnection connection = getConnection(url, parameters);
        return getPostOutput(connection);
    }
}
