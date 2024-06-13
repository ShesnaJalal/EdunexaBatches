package com.example.v1project.service;
import com.example.v1project.utility.CustomException;
import com.example.v1project.dto.Batches;
import com.example.v1project.dto.BatchParticipants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
@Component
public class ServiceSupport {
    private final RestTemplate restTemplate;

    public ServiceSupport(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> fetchUserData(int userId) {
        try {
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(
                    "http://localhost:8888/users?userId=" + userId, Map.class);
            Map<String, Object> userResponse = responseEntity.getBody();

            // Extract the user details from the response
            List<Map<String, Object>> responseData = (List<Map<String, Object>>) userResponse.get("responseData");
            if (responseData == null || responseData.isEmpty()) {
                throw new CustomException("Participant with ID " + userId + " not found in response data");
            }
            return responseData.get(0);
        } catch (Exception e) {
            throw new CustomException("Participant with ID " + userId + " not found", e);
        }
    }
}
