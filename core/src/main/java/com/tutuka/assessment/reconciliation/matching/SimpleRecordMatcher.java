package com.tutuka.assessment.reconciliation.matching;

import com.tutuka.assessment.reconciliation.exception.HeaderNotFoundException;
import com.tutuka.assessment.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class SimpleRecordMatcher<S extends FieldMatcher> extends RecordRevisitor implements RecordMatcher {

    private final static Logger logger = LoggerFactory.getLogger(SimpleRecordMatcher.class);

    public SimpleRecordMatcher(S fieldMatcher) {
        super(RecordModifiers.doNothing(), fieldMatcher);
    }

    @Override
    public boolean matches(Record recordOne, Record recordTwo) {
        Map<String, Integer> headers = recordOne.getHeader();
        for (String header : headers.keySet()) {
            try {
                String recordOneField = recordOne.get(header);
                String recordTwoField = recordTwo.get(header);
                if (!fieldMatcher.matches(header, recordOneField, recordTwoField)) {
                    return false;
                }
            } catch (HeaderNotFoundException e) {
                logger.debug("Exception occurred comparing {} with {}", recordOne, recordTwo, e);
                return false;
            }
        }
        return true;
    }

    public static <S extends FieldMatcher> RecordMatcher simple(S fieldMatcher) {
        return new SimpleRecordMatcher<>(fieldMatcher);
    }

}
