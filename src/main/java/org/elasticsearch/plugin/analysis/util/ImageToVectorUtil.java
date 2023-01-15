package org.elasticsearch.plugin.analysis.util;

import org.elasticsearch.SpecialPermission;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AccessController;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageToVectorUtil {

    private static final String URL_TO_VEC_API_URL = "http://192.168.249.1:29888/urlImagevector";
    private static final String POST = "POST";
    private static final String CONTENT_TYPE = "application/json";


    public static String[] imageUrlToVector(String imageUrl) throws IOException {
        URL url = new URL(URL_TO_VEC_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String json="{\"image_url\" : \"" +imageUrl+"\"}";

        connection.setRequestMethod(POST);
        connection.setRequestProperty("Content-Type", CONTENT_TYPE);
        connection.setDoOutput(true);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection
                .getOutputStream()));

        bufferedWriter.write(json);
        bufferedWriter.flush();
        bufferedWriter.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null)  {
            stringBuffer.append(inputLine);
        }
        bufferedReader.close();

        String response = stringBuffer.toString();

        String[] vectors = parseStringToVectorArray(response);
        return vectors;
    }

    private static String[] parseStringToVectorArray(String response) {
        Pattern pattern = Pattern.compile("(\\[)(.*?)(\\])");
        Matcher matcher = pattern.matcher(response);
        String tempArray="";
        if(matcher.find()){
            tempArray =  matcher.group(2).trim();
        }
        return tempArray.split(",");
    }


}
