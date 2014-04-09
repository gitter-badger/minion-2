package com.neueda.minion.ext;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PatternHelperTest {

    @Test
    public void testOneWord() throws Exception {
        Pattern preamble = Patterns.preamble("foo");

        assertMatcher(preamble, "foo:bar baz", "bar baz");
        assertMatcher(preamble, "  foo  : bar  baz ", "bar  baz");
    }

    @Test
    public void testTwoWords() throws Exception {
        Pattern preamble = Patterns.preamble("foo", "bar");

        assertMatcher(preamble, "foo bar:baz", "baz");
        assertMatcher(preamble, " foo  bar :baz", "baz");
    }

    private void assertMatcher(Pattern preamble, String input, String group) {
        Matcher matcher = preamble.matcher(input);
        assertThat(matcher.matches(), is(true));
        assertThat(matcher.group(1), equalTo(group));
    }

}
