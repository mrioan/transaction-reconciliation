package com.mario.sample.reconciliation.processing;

import com.mario.sample.reconciliation.exception.ReconciliationException;
import com.mario.sample.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Parser that provides an iterable interface to obtain one by one every record from a file.
 */
public class CSVParser implements Iterable<Record>, AutoCloseable {

    private final Logger logger = LoggerFactory.getLogger(CSVParser.class);
    private final static String CSV_FILE_EXTENSION = ".csv";
    private final Scanner scanner;

    private CSVParser(File csvFile) throws FileNotFoundException {
        if (csvFile == null || !csvFile.getName().toLowerCase().endsWith(CSV_FILE_EXTENSION)) {
            throw new IllegalArgumentException(String.format("%s is not a supported file. Only files having '%s' extension are supported", csvFile, CSV_FILE_EXTENSION));
        }
        this.scanner = new Scanner(csvFile);
    }

    public static CSVParser parse(File csvFile) throws FileNotFoundException {
        return new CSVParser(csvFile);
    }

    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

    @Override
    public Iterator<Record> iterator() {
        try {
            return new DefaultCSVParserIterator(scanner);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new ReconciliationException(e.getMessage(), e);
        }
    }
}
