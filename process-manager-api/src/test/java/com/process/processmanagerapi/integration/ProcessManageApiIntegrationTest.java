package com.process.processmanagerapi.integration;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@Sql({"/sql/purge.sql", "/sql/seed.sql"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProcessManageApiIntegrationTest {

    private static final String requestEndpointBase = "http://localhost:8090/process-management";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static String readJSON(String filename) {
        try {
            return FileUtils.readFileToString(ResourceUtils.getFile("classpath:" + filename), "UTF-8");
        } catch (IOException exception) {
            return null;
        }
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    //Tests for Process Controller

    @Test
    public void shouldReturn200WhenCreateNewProcessWithSuccess() {
        String payload = readJSON("request/createProcessWithSuccessPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/create"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn400WhenCreateNewProcessWhenUserNotExist() {
        String payload = readJSON("request/createProcessWithUserNotExistOnDatabasePayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/create"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
    }

    @Test
    public void shouldReturn400WhenCreateNewProcessWhenUserIsNotAuthorizedToCreateProcess() {
        String payload = readJSON("request/createProcessWithUserNotExistOnDatabasePayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/create"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
    }

    @Test
    public void shouldReturn400WhenProcessIdAlreadyExist() {
        String payload = readJSON("request/createProcessWhenUserIsNotAuthorizedPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/create"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User is not authorized to perform this operation"));
    }

    @Test
    public void shouldReturn200WhenIncludeNewProcessOpinionWithSuccess() {
        String payload = readJSON("request/includeProcessOpinionWithSuccessPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/includeProcessOpinion"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn400WhenIncludeNewProcessOpinionWithProcessNotFound() {
        String payload = readJSON("request/includeProcessOpinionWhenProcessNotFoundPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/includeProcessOpinion"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Process not found"));
    }

    @Test
    public void shouldReturn400WhenIncludeNewProcessOpinionWithProcessFinished() {
        String payload = readJSON("request/includeProcessOpinionWhenProcessIsFinishedPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/includeProcessOpinion"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Process already finished"));
    }

    @Test
    public void shouldReturn400WhenIncludeNewProcessOpinionWithUserNotAuthorized() {
        String payload = readJSON("request/includeProcessOpinionWithUserNotAuthorizedPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/includeProcessOpinion"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User is not authorized to perform this operation"));
    }

    @Test
    public void shouldReturn400WhenIncludeNewProcessOpinionWithUserIsNotAuthorizedToIncludeOpinionForThisProcess() {
        String payload = readJSON("request/includeProcessOpinionWhenUserIsNotAuthorizedToIncludeOpinionForThisProcessPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/includeProcessOpinion"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User is not authorized to add process opinion for this process"));
    }

    @Test
    public void shouldReturn200WhenFinishProcessWithSuccess() {
        String payload = readJSON("request/finishProcessWithSuccessPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/finish"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn400WhenFinishProcessWithUserNotFound() {
        String payload = readJSON("request/finishProcessWithUserNotFoundPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/finish"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
    }

    @Test
    public void shouldReturn400WhenFinishProcessWithUserNotAuthorizedToPerformThisOperation() {
        String payload = readJSON("request/finishProcessWithUserIsNotAuthorizedToPerformThisOperationPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/finish"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User is not authorized to perform this operation"));
    }

    @Test
    public void shouldReturn400WhenFinishProcessWithProcessNotFound() {
        String payload = readJSON("request/finishProcessWithProcessNotFoundPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/finish"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Process not found"));
    }

    @Test
    public void shouldReturn400WhenFinishProcessWithProcessAlreadyFinished() {
        String payload = readJSON("request/finishProcessWithProcessAlreadyFinishedPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/finish"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Process already finished"));
    }

    @Test
    public void shouldReturn400WhenFinishProcessWithUseIsNotAuthorizedToPerformThisOperationInThisProcess() {
        String payload = readJSON("request/finishProcessWithUserIsNotAuthorizedToFinishThisProcessPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/finish"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User is not authorized to finish this process"));
    }

    @Test
    public void shouldReturn200WhenFindByProcessNumberWithSuccess() {
        String payload = readJSON("request/findProcessByProcessNumberWithSuccessPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/findByProcessNumber"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn404WhenFindByProcessNumberWithProcessNotFound() {
        String payload = readJSON("request/findProcessByProcessNumberWithProcessNotFoundPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/findByProcessNumber"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Process not found"));
    }

    @Test
    public void shouldReturn400WhenFindByProcessNumberWithUserNotFound() {
        String payload = readJSON("request/findProcessByProcessNumberWithUserNotFoundPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/findByProcessNumber"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
    }

    @Test
    public void shouldReturn400WhenFindByProcessNumberWithUserNotAuthorizedToPerformThisOperation() {
        String payload = readJSON("request/findProcessByProcessNumberWithUserIsNotAuthorizedToPerformThisOperationPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/findByProcessNumber"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User is not authorized to perform this operation"));
    }

    @Test
    public void shouldReturn200WhenFindAllByProcessWithSuccess() {
        String payload = readJSON("request/findAllProcessWithSuccessPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/findAll"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn400WhenFindAllProcessWithUserNotFound() {
        String payload = readJSON("request/findAllProcessWithUserNotFoundPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/findAll"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
    }

    @Test
    public void shouldReturn400WhenFindAllProcessWithUserNotAuthorizedToPerformThisOperation() {
        String payload = readJSON("request/findAllProcessWithUserIsNotAuthorizedToPerformThisOperationPayload.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/process/findAll"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User is not authorized to perform this operation"));
    }

    //Tests for User Controller

}
