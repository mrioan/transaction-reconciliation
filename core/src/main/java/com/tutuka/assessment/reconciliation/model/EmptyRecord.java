package com.tutuka.assessment.reconciliation.model;

import com.tutuka.assessment.reconciliation.exception.HeaderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Sub-class of a record denoting an empty record.
 */
public class EmptyRecord extends Record {

    private final Logger logger = LoggerFactory.getLogger(EmptyRecord.class);

    public EmptyRecord(int rowNumber, final Map<String, Integer> mapping) {
        super(rowNumber, null, mapping);
    }

    @Override
    public String get(final int i) {
        return null;
    }

    @Override
    public String get(String headerName) throws HeaderNotFoundException {
        return null;
    }

}
