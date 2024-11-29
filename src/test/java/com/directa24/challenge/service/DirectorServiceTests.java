package com.directa24.challenge.service;

import com.directa24.challenge.model.Movie;
import com.directa24.challenge.dto.request.MovieRequest;
import com.directa24.challenge.service.impl.DirectorServiceImpl;
import com.directa24.challenge.util.ExternalEndpointsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class DirectorServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    ExternalEndpointsConstants constants;

    @InjectMocks
    DirectorServiceImpl service;

    private String url;
    private MovieRequest movieRequest1;
    private MovieRequest movieRequest2;
    private MovieRequest movieRequest3;


    @BeforeEach
    void setUp() {
        url = "http://www.aurl.com";
        Movie movie1 = new Movie("The Skin I Live In", "2011", "R", "02 Sep 2011", "120 min",
                "Drama, Horror, Thriller", "Pedro Almodóvar", "Pedro Almodóvar, Agustín Almodóvar, Thierry Jonquet",
                "Antonio Banderas, Elena Anaya, Jan Cornet");
        Movie movie2 = new Movie("Hugo", "2011", "PG", "23 Nov 2011", "126 min",
                "Adventure, Drama, Family", "Martin Scorsese", "John Logan, Brian Selznick",
                "Asa Butterfield, Chloë Grace Moretz, Christopher Lee");
        Movie movie3 = new Movie("Silence", "2016", "R", "13 Jan 2017", "161 min",
                "Drama, History", "Martin Scorsese", "Jay Cocks, Martin Scorsese, Shûsaku Endô",
                "Andrew Garfield, Adam Driver, Liam Neeson");
        movieRequest1 = new MovieRequest(1, 1, 3, 3, List.of(movie1));
        movieRequest2 = new MovieRequest(1, 1, 3, 3, List.of(movie2));
        movieRequest3 = new MovieRequest(1, 1, 3, 3, List.of(movie3));
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - over 0 movies")
    void getDirectorsGreaterThanThresholdOver0Movies() {
        List<String> expectedResult = List.of("Martin Scorsese", "Pedro Almodóvar");
        whenClausesForMovieRequest(movieRequest1, movieRequest2, movieRequest3);

        List<String> result = service.getDirectorsGreaterThanThreshold(0);

        assertNotNull(result);
        assertEquals(expectedResult.getFirst(), expectedResult.getFirst());
        assertEquals(expectedResult.getLast(), expectedResult.getLast());
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - over 1 movies")
    void getDirectorsGreaterThanThresholdOver1Movies() {
        List<String> expectedResult = List.of("Martin Scorsese");
        whenClausesForMovieRequest(movieRequest1, movieRequest2, movieRequest3);

        List<String> result = service.getDirectorsGreaterThanThreshold(1);

        assertNotNull(result);
        assertEquals(expectedResult.getFirst(), expectedResult.getFirst());
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - over 2 movies")
    void getDirectorsGreaterThanThresholdOver2Movies() {
        whenClausesForMovieRequest(movieRequest1, movieRequest2, movieRequest3);

        List<String> result = service.getDirectorsGreaterThanThreshold(2);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - RestClientException")
    void getDirectorsGreaterThanThresholdRestClientException() {
        String expectedResult = "RestClient problem";
        when(constants.getEronMoviesUrl()).thenReturn(url);
        when(restTemplate.getForObject(url + 1, MovieRequest.class)).thenThrow(new RestClientException(expectedResult));

        RestClientException exception = assertThrows(
                RestClientException.class,
                () -> service.getDirectorsGreaterThanThreshold(1)
        );
        assertEquals(expectedResult, exception.getMessage());
    }

    @Test
    @DisplayName("Get Directors Greater Than Threshold - NullPointerException")
    void getDirectorsGreaterThanThresholdNullPointerException() {
        MovieRequest withoutData = new MovieRequest(1, 1, 3, 3, null);
        whenClausesForMovieRequest(withoutData, movieRequest2, movieRequest3);

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> service.getDirectorsGreaterThanThreshold(1)
        );
        assertEquals(NullPointerException.class, exception.getClass());
    }

    private void whenClausesForMovieRequest(MovieRequest request1, MovieRequest request2, MovieRequest request3) {
        when(constants.getEronMoviesUrl()).thenReturn(url);
        when(restTemplate.getForObject(url + 1, MovieRequest.class)).thenReturn(request1);
        when(restTemplate.getForObject(url + 2, MovieRequest.class)).thenReturn(request2);
        when(restTemplate.getForObject(url + 3, MovieRequest.class)).thenReturn(request3);
    }
}
