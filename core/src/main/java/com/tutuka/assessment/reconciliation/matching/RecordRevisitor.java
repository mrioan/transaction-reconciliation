package com.tutuka.assessment.reconciliation.matching;

import com.tutuka.assessment.reconciliation.model.Record;

/**
 * Generic abstract class to be implemented by concrete record revisitors.
 * <p>A revisitor has the opportunity to virtually apply modifications to a record before carrying out an actual
 * matching operation.</p>
 * @param <T> an implementation of a {@link RecordModifier}
 * @param <S>an implementation of a {@link FieldMatcher}
 */
public abstract class RecordRevisitor<T extends RecordModifier, S extends FieldMatcher> implements RecordModifier, FieldMatcher {

    final T recordModifier;
    final S fieldMatcher;

    public RecordRevisitor(T recordModifier, S fieldMatcher) {
        if (recordModifier == null) {
            throw new IllegalArgumentException("recordModifier cannot be null.");
        }
        if (fieldMatcher == null) {
            throw new IllegalArgumentException("fieldMatcher cannot be null.");
        }
        this.recordModifier = recordModifier;
        this.fieldMatcher = fieldMatcher;
    }

    @Override
    final public Record modify(Record record) {
        return recordModifier.modify(record);
    }

    @Override
    final public boolean matches(String columnName, String recordOneField, String recordTwoField) {
        return fieldMatcher.matches(columnName, recordOneField, recordTwoField);
    }

}
