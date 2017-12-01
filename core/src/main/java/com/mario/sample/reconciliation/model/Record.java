package com.mario.sample.reconciliation.model;

import com.mario.sample.reconciliation.exception.HeaderNotFoundException;
import com.mario.sample.reconciliation.exception.ReconciliationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an actual CSV record. Each row of data read from the CSV files is represented here as a Java object
 * by means of this class.
 */
public class Record {

    private final Logger logger = LoggerFactory.getLogger(Record.class);

    final Map<String, Integer> mapping; //the header and its position
    final int rowNumber; //the row number in the CSV file where this record was found
    final List<String> values; //actual record values, in the same order that they were found in the file

    public Record(int rowNumber, List values, final Map<String, Integer> mapping) {
        if (rowNumber < 0) {
            throw new IllegalArgumentException("rowNumber cannot be less than 0.");
        }
        this.rowNumber = rowNumber;
        this.values = values != null ? Collections.unmodifiableList(values) : Collections.emptyList();
        this.mapping = Collections.unmodifiableMap(mapping);
    }

    /**
     * Indicates whether some other record is "equal to" this one.
     * @param aRecord the record to be compared against this record.
     * @return <code>true</code> if the records are identical; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object aRecord) {
        if (!Record.class.isInstance(aRecord)) {
            return false;
        }
        Record record = (Record) aRecord;
        try {
            for (String key : mapping.keySet()) {
                String value = record.get(key);
                if (value != null) {
                    if (!value.equals(this.get(key))) {
                        return false;
                    }
                } else {
                    if (this.get(key) != null) {
                        return false;
                    }
                }
            }
            return true;
        } catch (HeaderNotFoundException e) {
            logger.debug("Exception occurred while trying to compare {} with {}.", aRecord, record, e);
            throw new ReconciliationException(e.getMessage(), e);
        }
    }

    public String toString() {
        return String.format("Row: %d, Values: [%s]", getRow(), values.stream()
                .map( Object::toString )
                .collect( Collectors.joining( ", " )));
    }

    public List<String> getValues() {
        return values;
    }

    public String get(final int i) {
        return values.get(i);
    }

    public String get(String headerName) throws HeaderNotFoundException {
        if (mapping == null) {
            throw new IllegalStateException("This record has no header mapping. Records cannot be accessed by header name.");
        }
        final Integer index = mapping.get(headerName);
        if (index == null) {
            throw new HeaderNotFoundException(String.format("Mapping for '%s' not found, expected one of '%s'", headerName, mapping.keySet()));
        }
        try {
            return values.get(index);
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException(String.format( "Index for header '%s' is %d but this record has only %d values", headerName, index, values.size()), e);
        }
    }

    public Map<String, Integer> getHeader() {
        return this.mapping; //mapping is unmodifiable
    }

    public int getRow() {
        return this.rowNumber;
    }

}
