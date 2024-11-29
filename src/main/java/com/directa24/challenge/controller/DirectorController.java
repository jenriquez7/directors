package com.directa24.challenge.controller;

import com.directa24.challenge.service.DirectorService;
import com.directa24.challenge.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;


@RestController
@Tag(name = "Director Controller", description = "Director API")
@Slf4j
@RequestMapping(path = "/api/directors", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class DirectorController {

    private final DirectorService service;

    @Autowired
    public DirectorController(DirectorService service) {
        this.service = service;
    }

    @Operation(summary = "get directors whose number of movies directed is strictly greater than the given threshold",
            operationId = "getDirectorsMoviesGreaterThanThreshold")
    @PermitAll
    @GetMapping(path = "/movies_threshold", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<List<String>>> getDirectorsGreaterThanThreshold(@RequestParam("threshold") @Positive Integer threshold) {
        try {
            return ResponseEntity.ok(new ResponseDto<> (service.getDirectorsGreaterThanThreshold(threshold), null));
        } catch (RestClientException | NullPointerException e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<> (Collections.emptyList(), e.getMessage()));
        }
    }
}
