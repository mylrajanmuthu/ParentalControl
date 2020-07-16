package com.mylrajan.stubs;

import com.mylrajan.MovieService.MovieService;
import com.mylrajan.MovieService.TechnicalFailureException;
import com.mylrajan.MovieService.TitleNotFoundException;
import com.mylrajan.row.MovieRow;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class StubMovieService implements MovieService {

    private final Map<String, String> movieRatings;

    public StubMovieService(List<MovieRow> movies) {
        movieRatings = movies.stream()
                .collect(toMap(MovieRow::getId, MovieRow::getRating));
    }

    @Override
    public String getParentalControlLevel(String movieId) throws TitleNotFoundException, TechnicalFailureException {
        return movieRatings.get(movieId);
    }
}
