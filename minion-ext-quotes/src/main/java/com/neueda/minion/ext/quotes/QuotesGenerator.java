package com.neueda.minion.ext.quotes;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuotesGenerator {
    private final Logger logger = LoggerFactory.getLogger(QuotesGenerator.class);

    private static final String SOURCE = "http://www.lorquotes.ru/fortraw.php";
    private static final String QUOTE_WITH_FAIL = "Этот код перестал работать.";

    List<String> quotes = new ArrayList<>();


    public QuotesGenerator() {
        try {
            populateQuotes();
        } catch (IOException e) {
            logger.error("Quotes Generator", e.getStackTrace());
        }
    }

    public String getRandom() {
        if (quotes.size() == 0) {
            try {
                populateQuotes();
                return getRandomQuote();
            } catch (IOException e) {
                e.printStackTrace();
                return QUOTE_WITH_FAIL;
            }
        } else {
            return getRandomQuote();
        }
    }

    private String getRandomQuote() {
        return quotes.remove(new Random().nextInt(quotes.size()));
    }

    private void populateQuotes() throws IOException {
        BufferedReader r = getReaderWithContent();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            if (!line.equals("%")) {
                sb.append(line);
                sb.append("\n");
            } else {
                quotes.add(sb.toString());
                sb = new StringBuilder();
            }
        }
    }


    private BufferedReader getReaderWithContent() throws IOException {
        InputStream stream = Request.Get(SOURCE).execute().returnContent().asStream();
        return new BufferedReader(new InputStreamReader(stream, "KOI8-R"));
    }
}
