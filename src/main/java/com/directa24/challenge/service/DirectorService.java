package com.directa24.challenge.service;

import org.springframework.web.client.RestClientException;

import java.util.List;

public interface DirectorService {
    List<String> getDirectorsGreaterThanThreshold(int threshold) throws RestClientException, NullPointerException;
}
