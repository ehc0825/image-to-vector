package org.elasticsearch.plugin.analysis.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageToVectorUtil {

    private static final String URL_TO_VEC_API_URL = "http://127.0.0.1:29888/urlImagevector";
    private static final String POST = "POST";
    private static final String CONTENT_TYPE = "application/json";


    public static String[] imageUrlToVector(String imageUrl, String img_to_vec_api) throws IOException {
        URL url;
        url = getUrl(img_to_vec_api);

        HttpURLConnection connection = getHttpURLConnection(url);

        String json="{\"image_url\" : \"" +imageUrl+"\"}";
        sendRequest(connection, json);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer stringBuffer = getStringBuffer(bufferedReader);

        String response = stringBuffer.toString();

        String[] vectors = parseStringToVectorArray(response);
        return vectors;
    }

    private static StringBuffer getStringBuffer(BufferedReader bufferedReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null)  {
            stringBuffer.append(inputLine);
        }
        bufferedReader.close();
        return stringBuffer;
    }

    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(POST);
        connection.setRequestProperty("Content-Type", CONTENT_TYPE);
        connection.setDoOutput(true);
        return connection;
    }

    private static void sendRequest(HttpURLConnection connection, String json) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection
                .getOutputStream()));
        bufferedWriter.write(json);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static URL getUrl(String img_to_vec_api) throws MalformedURLException {
        URL url;
        if(img_to_vec_api.equals("")|| img_to_vec_api ==null)
        {
            url = new URL(URL_TO_VEC_API_URL);
        }
        else
        {
            url = new URL(img_to_vec_api);
        }
        return url;
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
