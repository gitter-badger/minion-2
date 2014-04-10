package com.neueda.minion.ext;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PatternHelperTest {

    @Test
    public void testSentence() throws Exception {
        Pattern sentence = Patterns.sentence("foo", "bar", "!");

        assertMatcher(sentence, "foo bar !");
        assertMatcher(sentence, "  foo   bar ! ");
    }

    @Test
    public void testPreambleOneWord() throws Exception {
        Pattern preamble = Patterns.preamble("foo");

        assertMatcher(preamble, "foo:bar baz", "bar baz");
        assertMatcher(preamble, "  foo  : bar  baz ", "bar  baz");
    }

    @Test
    public void testPreambleTwoWords() throws Exception {
        Pattern preamble = Patterns.preamble("foo", "bar");

        assertMatcher(preamble, "foo bar:baz", "baz");
        assertMatcher(preamble, " foo  bar :baz", "baz");
    }

    private void assertMatcher(Pattern preamble, String input, String... groups) {
        Matcher matcher = preamble.matcher(input);
        assertThat(matcher.matches(), is(true));
        for (int i = 0; i < groups.length; i++) {
            assertThat(matcher.group(i + 1), equalTo(groups[i]));
        }
    }

}
