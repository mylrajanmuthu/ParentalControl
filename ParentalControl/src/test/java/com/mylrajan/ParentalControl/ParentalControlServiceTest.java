package com.mylrajan.ParentalControl;

import com.mylrajan.MovieService.MovieService;
import com.mylrajan.MovieService.TechnicalFailureException;
import com.mylrajan.MovieService.TitleNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Comparator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParentalControlServiceTest {

    @Mock
    private AdditionalMessageCallback additionalMessageCallback;
    @Mock
    private MovieService movieService;
    @Mock
    private Comparator<String> levelComparator;

    private ParentalControlService service;

    @Test
    public void isAllowedToWatchMovieWithMovieRatingLowerThanUserPreference_shouldAllowWatching() throws Exception {
        // Set up fixture
        service = new ParentalControlService(movieService, levelComparator);

        // Set expectations
        when(movieService.getParentalControlLevel("some movie id")).thenReturn("PG");
        when(levelComparator.compare("PG", "12")).thenReturn(-1);

        // Exercise SUT
        final boolean result = service.isAllowedToWatchMovie("12", "some movie id", additionalMessageCallback);

        // Verify behaviour
        assertThat(result, is(true));
        verifyZeroInteractions(additionalMessageCallback);
    }

    @Test
    public void isAllowedToWatchMovieWithMovieRatingEqualToUserPreference_shouldAllowWatching() throws Exception {
        // Set up fixture
        service = new ParentalControlService(movieService, levelComparator);

        // Set expectations
        when(movieService.getParentalControlLevel("some movie id")).thenReturn("PG");
        when(levelComparator.compare("PG", "PG")).thenReturn(0);

        // Exercise SUT
        final boolean result = service.isAllowedToWatchMovie("PG", "some movie id", additionalMessageCallback);

        // Verify behaviour
        assertThat(result, is(true));
        verifyZeroInteractions(additionalMessageCallback);
    }

    @Test
    public void isAllowedToWatchMovieWithMovieRatingHigherThanUserPreference_shouldNotAllowWatching() throws Exception {
        // Set up fixture
        service = new ParentalControlService(movieService, levelComparator);

        // Set expectations
        when(movieService.getParentalControlLevel("some movie id")).thenReturn("12");
        when(levelComparator.compare("12", "PG")).thenReturn(1);

        // Exercise SUT
        final boolean result = service.isAllowedToWatchMovie("PG", "some movie id", additionalMessageCallback);

        // Verify behaviour
        assertThat(result, is(false));
        verifyZeroInteractions(additionalMessageCallback);
    }

    @Test
    public void isAllowedToWatchMovieWithTitleNotFoundException_shouldCallCallbackWithMessage() throws Exception {
        // Set up fixture
        final String expectedMessage = "title not found exception message";
        service = new ParentalControlService(movieService, levelComparator);

        // Set expectations
        when(movieService.getParentalControlLevel("some movie id"))
                .thenThrow(new TitleNotFoundException(expectedMessage));

        // Exercise SUT
        final boolean result = service.isAllowedToWatchMovie("PG", "some movie id", additionalMessageCallback);

        // Verify behaviour
        assertThat(result, is(false));
        verify(additionalMessageCallback).showMessage(eq(expectedMessage));
        verifyZeroInteractions(levelComparator);
    }

    @Test
    public void isAllowedToWatchMovieWithTechnicalFailureException_shouldCallCallbackWithMessage() throws Exception {
        // Set up fixture
        final String expectedMessage = "technical failure exception message";
        service = new ParentalControlService(movieService, levelComparator);

        // Set expectations
        when(movieService.getParentalControlLevel("some movie id"))
                .thenThrow(new TechnicalFailureException(expectedMessage));

        // Exercise SUT
        final boolean result = service.isAllowedToWatchMovie("PG", "some movie id", additionalMessageCallback);

        // Verify behaviour
        assertThat(result, is(false));
        verify(additionalMessageCallback).showMessage(eq(expectedMessage));
        verifyZeroInteractions(levelComparator);
    }
}