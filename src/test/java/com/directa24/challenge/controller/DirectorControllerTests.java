package com.directa24.challenge.controller;

import com.directa24.challenge.dto.response.ResponseDto;
import com.directa24.challenge.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class DirectorControllerTests {

    @Mock
    private DirectorService service;

    @InjectMocks
    private DirectorController controller;

    private int threshold;
    private List<String> directors;

    @BeforeEach
    void setUp() {
        threshold = 3;
        directors = List.of("M. Night Shyamalan", "Martin Scorsese", "Pedro Almodóvar", "Woody Allen");
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - Successfully with results")
    void getDirectorsGreaterThanThresholdSuccessfullyWithResults() {
        when(service.getDirectorsGreaterThanThreshold(threshold)).thenReturn(directors);
        ResponseEntity<ResponseDto<List<String>>> response = controller.getDirectorsGreaterThanThreshold(threshold);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().error());
        assertEquals(directors, response.getBody().data());
        verify(service, times(1)).getDirectorsGreaterThanThreshold(threshold);
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - Successfully without results")
    void getDirectorsGreaterThanThresholdSuccessfullyWithoutResults() {
        when(service.getDirectorsGreaterThanThreshold(8)).thenReturn(Collections.emptyList());
        ResponseEntity<ResponseDto<List<String>>> response = controller.getDirectorsGreaterThanThreshold(8);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().error());
        assertEquals(Collections.emptyList(), response.getBody().data());
        verify(service, times(1)).getDirectorsGreaterThanThreshold(8);
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - RestClientException")
    void getDirectorsGreaterThanThresholdRestClientException() {
        String message = "Problem with rest client";
        when(service.getDirectorsGreaterThanThreshold(threshold)).thenThrow(new RestClientException(message));

        ResponseEntity<ResponseDto<List<String>>> response = controller.getDirectorsGreaterThanThreshold(threshold);

        throwingExceptionAssertion(response, message);
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - NullPointerException")
    void getDirectorsGreaterThanThresholdNullPointerException() {
        String message = "NullPointerException in some field";
        when(service.getDirectorsGreaterThanThreshold(threshold)).thenThrow(new NullPointerException(message));

        ResponseEntity<ResponseDto<List<String>>> response = controller.getDirectorsGreaterThanThreshold(threshold);

        throwingExceptionAssertion(response, message);
    }

    private void throwingExceptionAssertion(ResponseEntity<ResponseDto<List<String>>> response, String message) {
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Collections.emptyList(), response.getBody().data());
        assertNotNull(response.getBody().error());
        assertEquals(message, response.getBody().error());
        verify(service, times(1)).getDirectorsGreaterThanThreshold(threshold);
    }
}
