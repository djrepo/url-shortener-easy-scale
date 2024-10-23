package com.upwork.tinyurl.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
public class UrlControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // Example URL to test
    // localhost:8080/urls/aaaaanI
    // https://google.com

    @Test
    @DisplayName("CallWithWrongTinyURL")
    public void whenGetCalled_thenShouldBadRequest() {
        ResponseEntity<String> result = restTemplate.getForEntity("/urls/aaaaa", String.class);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    // For test there should be a tiny URL generated in the database
    @Test
    @DisplayName("CallWithTinyURLAlreadyExist")
    public void whenGetCalled_thenShouldRedirectToLongURL() {
        ResponseEntity<String> result = restTemplate.getForEntity("/urls/aaaaanI", String.class);

        assertNotNull(result);
        System.out.println("statusCode: " + result.getStatusCodeValue());
        assertEquals(HttpStatus.FOUND.value(), result.getStatusCodeValue());
    }

    @Test
    @DisplayName("CreateTinyUrlWithInvalidStringFormat")
    public void whenGenerateCalledWithInvalidFormat() {
        ResponseEntity<String> result = restTemplate.postForEntity("/urls/generate", "http//google.com", String.class);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid URL format.", result.getBody());
    }

    @Test
    @DisplayName("CreateTinyUrl")
    public void whenGenerateEndPointCall() {
        ResponseEntity<String> result = restTemplate.postForEntity("/urls/generate", "http://google.com", String.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}