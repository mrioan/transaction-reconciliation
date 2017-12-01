package com.tutuka.assessment.reconciliation.processing;

import com.tutuka.assessment.reconciliation.exception.UnsupportedDataException;
import com.tutuka.assessment.reconciliation.matching.RecordModifier;
import com.tutuka.assessment.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public abstract class AbstractCSVParserIterator implements Iterator<Record> {

    private final static Logger logger = LoggerFactory.getLogger(AbstractCSVParserIterator.class);

    public static final String EOL = ",";
    public static final String DEFAULT_HEADER_REGEX = ",";
    public static final String DEFAULT_RECORD_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    private final Scanner scanner;
    private final Map<String, Integer> headerMap;
    private final RecordModifier missingValuesModifier; //the chance to apply some strategy when a found record has less values than expected
    private final RecordModifier extraValuesModifier; //the chance to apply some strategy when a found record has more values than expected
    private final Pattern headerPattern;
    private final Pattern recordPattern;

    private AtomicInteger currentRow = new AtomicInteger(1);
    private Record nextRecord = null;

    public AbstractCSVParserIterator(Scanner scanner, RecordModifier missingValuesModifier, RecordModifier extraValuesModifier) throws FileNotFoundException {
        this(scanner, DEFAULT_HEADER_REGEX, DEFAULT_RECORD_REGEX, missingValuesModifier, extraValuesModifier);
    }

    public AbstractCSVParserIterator(Scanner scanner, String headerRegex, String recordRegex, RecordModifier missingValuesModifier, RecordModifier extraValuesModifier) throws FileNotFoundException {
        if (scanner == null) {
            throw new IllegalArgumentException("scanner cannot be null.");
        }
        if (headerRegex == null) {
            throw new IllegalArgumentException("headerRegex cannot be null.");
        }
        if (recordRegex == null) {
            throw new IllegalArgumentException("recordRegex cannot be null.");
        }
        if (missingValuesModifier == null) {
            throw new IllegalArgumentException("missingValuesModifier cannot be null.");
        }
        if (extraValuesModifier == null) {
            throw new IllegalArgumentException("extraValuesModifier cannot be null.");
        }
        this.scanner = scanner;
        this.headerPattern = Pattern.compile(headerRegex);
        this.recordPattern = Pattern.compile(recordRegex);
        this.headerMap = Collections.unmodifiableMap(startIteratingForHeader());
        this.missingValuesModifier = missingValuesModifier;
        this.extraValuesModifier = extraValuesModifier;
    }

    @Override
    public synchronized boolean hasNext() {
        boolean hasNext;
        do {
            hasNext = scanner.hasNextLine();
            if (!hasNext) {
                break;
            }
            try {
                this.nextRecord = this.nextInternal();
            } catch (UnsupportedDataException e) {
                logger.error("The input data is not supported.", e);
                throw e;
            }
        } while (this.nextRecord == null); //nextRecord is null when the line that was just read from the file is a blank line
        return hasNext;
    }

    @Override
    public synchronized Record next() {
        Record returningRecord = this.nextRecord;
        this.nextRecord = null;
        return returningRecord;
    }

    private Record nextInternal() {
        String line = scanner.nextLine();
        if (line.length() > 0 && !line.endsWith(EOL) ) {
            throw new UnsupportedDataException(String.format("Every CSV record is expected to be single-line " +
                            "long and to end with '%s'. The record '%s' ends with '%s' and it therefore is either " +
                            "a multi-line record or an erroneous one. CSV parsing is aborted now.",
                    EOL, line, line.substring(line.length()-1, line.length())));
        }

        String[] values = this.recordPattern.split(line, -1); //with -1 we can have empty values returned

        //not public code

        return new Record(this.currentRow.getAndIncrement(), Arrays.asList(values), headerMap);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private Map<String, Integer> startIteratingForHeader() {
        Map<String, Integer> headerMap = new LinkedHashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            currentRow.getAndIncrement();
            if (line.length() > 0) {
                String[] values = this.headerPattern.split(line);
                for (int i = 0; i < values.length; i++) {
                    headerMap.put(values[i], i);
                }
                break;
            }
        }
        return headerMap;
    }
}
