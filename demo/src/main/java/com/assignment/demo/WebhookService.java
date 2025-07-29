package com.assignment.demo;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

  public void callWebhook() {
    RestTemplate restTemplate = new RestTemplate();

    String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    Map<String, String> request = new HashMap<>();
    request.put("name", "Shivansh Sood");
    request.put("regNo", "2211981374");
    request.put("email", "shivansh1374.be22@chitkarauniversity.edu.in");


    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);

    ResponseEntity<WebhookResponse> response = restTemplate.exchange(
      url, HttpMethod.POST, requestEntity, WebhookResponse.class
    );

    WebhookResponse webhookResponse = response.getBody();

    if (webhookResponse == null) {
      System.out.println("Filed to get response");
      return;
    }
    
    String webhookUrl = webhookResponse.getWebhook();
    String token = webhookResponse.getAccessToken();

    System.out.println("URL: " + webhookUrl);
    System.out.println("Token: " + token);

    String finalSQL = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, (SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e1.DEPARTMENT AND e2.DOB > e1.DOB) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID ORDER BY e1.EMP_ID DESC";

    HttpHeaders submitHeaders = new HttpHeaders();
    submitHeaders.setContentType(MediaType.APPLICATION_JSON);
    submitHeaders.setBearerAuth(token);

    Map<String, String> submit = new HashMap<>();
    submit.put("finalQuery", finalSQL);

    HttpEntity<Map<String, String>> finalRequest = new HttpEntity<>(submit, submitHeaders);

    try {
      ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, finalRequest, String.class);
      System.out.println("Response :" + submitResponse.getBody());
    }
    catch(HttpClientErrorException e) {
      System.err.println("Error submiiting sol: " + e.getMessage());
    }
  }

}
