package com.tutuka.assessment.reconciliation.processing;

import com.tutuka.assessment.reconciliation.config.ConfigProperties;
import com.tutuka.assessment.reconciliation.model.Record;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;

/**
 * This is a file-related {@link RecordStore} implementation. Records are automatically loaded from the given file.
 * Records cannot be added to this store once it has been initialized. It does however allow records to be removed, retrieved and
 * iterated.
 */
public class FileCSVRecordStore extends AbstractCSVRecordStore {

    private static final Logger logger = LoggerFactory.getLogger(FileCSVRecordStore.class);

    private final static String CSV_FILE_EXTENSION = ".csv";
    protected final static String keyColumn = ConfigProperties.getStringProperty("key.column");

    public FileCSVRecordStore(File csvFile) throws FileNotFoundException {
        super(keyColumn);
        if (csvFile == null || !csvFile.getName().toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException(String.format("%s is not a supported file. Only files having '%s' extension are supported", csvFile, CSV_FILE_EXTENSION));
        }
        loadRecords(csvFile);
    }

    private void loadRecords(File csvFile) throws FileNotFoundException {
        try(CSVParser csvParser = CSVParser.parse(csvFile)) { //autocloseable
            for (Record record : csvParser) {
                super.add(record);
            }
        }
    }

    @Override
    public Record add(Record record) {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not allow records to be added.");
    }
}
