package com.Bajaj.Bajaj;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SolutionService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void startProcess() {

        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        String jsonBody = """
                {
                  "name": "Saathik Basha",
                  "regNo": "22BCE0086",
                  "email": "saathik.basha2022@vitstudent.ac.in"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> generateReq = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<WebhookResponse> response =
                restTemplate.exchange(url,
                        HttpMethod.POST,
                        generateReq,
                        WebhookResponse.class);

        WebhookResponse data = response.getBody();
        assert data != null;

        System.out.println("Webhook: " + data.getWebhook());
        System.out.println("AccessToken: " + data.getAccessToken());

        
        String finalQuery = "SELECT \r\n" + //
                        "    d.DEPARTMENT_NAME,\r\n" + //
                        "    AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURRENT_DATE)) AS AVERAGE_AGE,\r\n" + //
                        "    (\r\n" + //
                        "        SELECT GROUP_CONCAT(full_name ORDER BY emp_id SEPARATOR ', ')\r\n" + //
                        "        FROM (\r\n" + //
                        "            SELECT \r\n" + //
                        "                e2.EMP_ID AS emp_id,\r\n" + //
                        "                CONCAT(e2.FIRST_NAME, ' ', e2.LAST_NAME) AS full_name\r\n" + //
                        "            FROM EMPLOYEE e2\r\n" + //
                        "            JOIN PAYMENTS p2 ON e2.EMP_ID = p2.EMP_ID\r\n" + //
                        "            WHERE e2.DEPARTMENT = d.DEPARTMENT_ID \r\n" + //
                        "              AND p2.AMOUNT > 70000\r\n" + //
                        "            ORDER BY e2.EMP_ID\r\n" + //
                        "            LIMIT 10\r\n" + //
                        "        ) AS limited_names\r\n" + //
                        "    ) AS EMPLOYEE_LIST\r\n" + //
                        "FROM DEPARTMENT d\r\n" + //
                        "JOIN EMPLOYEE e ON d.DEPARTMENT_ID = e.DEPARTMENT\r\n" + //
                        "JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID\r\n" + //
                        "WHERE p.AMOUNT > 70000\r\n" + //
                        "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME\r\n" + //
                        "ORDER BY d.DEPARTMENT_ID DESC;\r\n" + //
                        "";

        sendFinalQuery(data.getWebhook(), data.getAccessToken(), finalQuery);
    }

    private void sendFinalQuery(String webhookUrl, String token, String finalQuery) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", token);

    FinalQueryRequest body = new FinalQueryRequest(finalQuery);

    HttpEntity<FinalQueryRequest> entity = new HttpEntity<>(body, headers);

    ResponseEntity<String> response =
            restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);

    System.out.println("Response from webhook: " + response.getBody());
}

}
