package com.directa24.challenge.dto.request;

import com.directa24.challenge.model.Movie;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MovieRequest (@JsonProperty("page") Integer page,
                            @JsonProperty("per_page") Integer perPage,
                            @JsonProperty("total") Integer total,
                            @JsonProperty("total_pages") Integer totalPages,
                            @JsonProperty("data") List<Movie> data) { }
