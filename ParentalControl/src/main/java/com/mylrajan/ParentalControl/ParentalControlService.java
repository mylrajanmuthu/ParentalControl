package com.mylrajan.ParentalControl;

import com.mylrajan.MovieService.MovieService;
import com.mylrajan.MovieService.TechnicalFailureException;
import com.mylrajan.MovieService.TitleNotFoundException;

import java.util.Comparator;

public class ParentalControlService {

    private final MovieService movieService;
    private final Comparator<String> levelComparator;

    public ParentalControlService(MovieService movieService, Comparator<String> levelComparator) {
        this.movieService = movieService;
        this.levelComparator = levelComparator;
    }

    public boolean isAllowedToWatchMovie(String preferredLevel, String movieId, AdditionalMessageCallback callback) {
        try {
            final String movieParentalControlLevel = movieService.getParentalControlLevel(movieId);
            return matchesPreferredLevel(preferredLevel, movieParentalControlLevel);
        } catch (TitleNotFoundException | TechnicalFailureException e) {
            callback.showMessage(e.getMessage());
            return false;
        }
    }

    private boolean matchesPreferredLevel(String preferredLevel, String movieParentalControlLevel) {
        return levelComparator.compare(movieParentalControlLevel, preferredLevel) <= 0;
    }
}