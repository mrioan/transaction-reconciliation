package com.tutuka.assessment.reconciliation.matching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleRecordRevisitor<T extends RecordModifier, S extends FieldMatcher> extends RecordRevisitor {

    private final static Logger logger = LoggerFactory.getLogger(SimpleRecordRevisitor.class);

    public SimpleRecordRevisitor(T recordModifier, S fieldMatcher) {
        super(recordModifier, fieldMatcher);
    }

    public static <T extends RecordModifier, S extends FieldMatcher> SimpleRecordRevisitor simple(T recordModifier, S fieldMatcher) {
        return new SimpleRecordRevisitor<>(recordModifier, fieldMatcher);
    }

}
