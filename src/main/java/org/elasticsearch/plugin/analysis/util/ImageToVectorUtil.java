package org.elasticsearch.plugin.analysis.util;

import org.elasticsearch.plugin.analysis.util.dto.ResponseVector;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageToVectorUtil {

    private static final String URL_TO_VEC_API_URL = "http://192.168.249.1:29888/urlImagevector";

    public static String[] imageUrlToVector(String imageUrl) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("image_url", imageUrl);
        ResponseEntity<ResponseVector> response = vectorApiResponse(jsonMap);
        return Objects.requireNonNull(response.getBody()).getVector();
    }

    private static ResponseEntity<ResponseVector> vectorApiResponse(Map<String, Object> jsonMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jsonMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(URL_TO_VEC_API_URL, HttpMethod.GET, entity, ResponseVector.class);
    }

}
