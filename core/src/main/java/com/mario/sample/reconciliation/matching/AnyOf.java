package com.mario.sample.reconciliation.matching;

import com.mario.sample.reconciliation.exception.HeaderNotFoundException;
import com.mario.sample.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Executes a logical disjunction: evaluates multiple matchers until one of them is positive. As soon as one of all
 * the internal matchers evaluates to <code>true</code> then this matcher finishes and returns <code>true</code>. If
 * none of all the internal matchers is positive then this matcher will return <code>false</code>.
 */
public class AnyOf implements RecordMatcher {

    private final static Logger logger = LoggerFactory.getLogger(AnyOf.class);

    private final Iterable<RecordRevisitor> recordRevisitors;

    public AnyOf(List<RecordRevisitor> recordRevisitors) {
        if (recordRevisitors == null) {
            throw new IllegalArgumentException("recordRevisitors cannot be null.");
        }
        this.recordRevisitors = Collections.unmodifiableList(recordRevisitors);
    }

    @Override
    public boolean matches(Record recordOne, Record recordTwo) {
        Record oneRecordModified = recordOne;
        Record anotherRecordModified = recordTwo;
        for (RecordRevisitor recordRevisitor : recordRevisitors) {
            oneRecordModified = recordRevisitor.modify(oneRecordModified);
            anotherRecordModified = recordRevisitor.modify(anotherRecordModified);
        }

        Map<String, Integer> headers = recordOne.getHeader();
        for (RecordRevisitor recordRevisitor : recordRevisitors) {
            boolean oneFieldUnmatched = false;
            for (String header : headers.keySet()) {
                try {
                    String recordOneField = oneRecordModified.get(header);
                    String recordTwoField = anotherRecordModified.get(header);
                    if (!recordRevisitor.matches(header, recordOneField, recordTwoField)) {
                        oneFieldUnmatched = true;
                        break;
                    }
                } catch (HeaderNotFoundException e) {
                    logger.debug("Exception occurred comparing {} with {}", recordOne, recordTwo, e);
                    return false;
                }
            }
            if (!oneFieldUnmatched) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers (i.e. executes a
     * logical disjunction)
     * @param recordRevisitors the list of {@link RecordRevisitor RecordRevisitor} that will be evaluated.
     * @return a new instance of {@link AnyOf}.
     */
    public static RecordMatcher anyOf(List<RecordRevisitor> recordRevisitors) {
        return new AnyOf(recordRevisitors);
    }

}