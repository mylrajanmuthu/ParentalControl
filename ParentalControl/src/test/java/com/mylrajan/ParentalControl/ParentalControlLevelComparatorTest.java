package com.mylrajan.ParentalControl;

import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParentalControlLevelComparatorTest {

    @Test
    public void compare_shouldOrderValuesAccordingToProvidedList() throws Exception {
        // Set up fixture
        final ParentalControlLevelComparator comparator =
                new ParentalControlLevelComparator(asList("level 1", "level 2", "level 3"));

        // Verify behaviour
        assertThat(comparator.compare("level 1", "level 2"), is(lessThan(0)));
        assertThat(comparator.compare("level 1", "level 3"), is(lessThan(0)));
        assertThat(comparator.compare("level 3", "level 2"), is(not(lessThan(0))));
        assertThat(comparator.compare("level 2", "level 2"), is(equalTo(0)));
    }

    private LessThan lessThan(int upperBound) {
        return new LessThan("level less than", upperBound);
    }

    private class LessThan extends CustomTypeSafeMatcher<Integer> {
        private int upperBound;

        public LessThan(String description, int upperBound) {
            super(description);
            this.upperBound = upperBound;
        }

        @Override
        protected boolean matchesSafely(Integer item) {
            return item < upperBound;
        }
    }
}
