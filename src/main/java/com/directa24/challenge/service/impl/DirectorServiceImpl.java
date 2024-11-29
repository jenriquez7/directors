package com.directa24.challenge.service.impl;

import com.directa24.challenge.model.Movie;
import com.directa24.challenge.dto.request.MovieRequest;
import com.directa24.challenge.service.DirectorService;
import com.directa24.challenge.util.ExternalEndpointsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DirectorServiceImpl implements DirectorService {

    private final RestTemplate restTemplate;
    private final ExternalEndpointsConstants constants;

    public DirectorServiceImpl(RestTemplate restTemplate, ExternalEndpointsConstants constants) {
        this.restTemplate = restTemplate;
        this.constants = constants;
    }

    @Override
    public List<String> getDirectorsGreaterThanThreshold(int threshold) throws RestClientException, NullPointerException {
        List<Movie> movies = this.getAllMovies();
        Map<String, Long> directorCounts = movies.stream()
                .collect(Collectors.groupingBy(
                        Movie::getDirector,
                        Collectors.counting()
                ));

        return directorCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted(String::compareTo)
                .toList();

    }

    private List<Movie> getAllMovies() {
        try {
            List<Movie> movies = new ArrayList<>();
            int currentPage = 1;
            boolean hasMorePages = true;

            while (hasMorePages) {
                MovieRequest movieRequest = restTemplate.getForObject(constants.getEronMoviesUrl() + currentPage, MovieRequest.class);

                if (Objects.isNull(movieRequest)) {
                    break;
                }

                movies.addAll(movieRequest.data());
                hasMorePages = currentPage < movieRequest.totalPages();
                currentPage++;
            }
            return movies;
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }
}
