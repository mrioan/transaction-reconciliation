package com.tutuka.assessment.reconciliation.filter;

import com.tutuka.assessment.reconciliation.exception.FilterException;
import com.tutuka.assessment.reconciliation.processing.RecordStore;
import com.tutuka.assessment.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * General abstract filter that iterates over two sets of {@link RecordStore RecordStore} and ultimately delegates to
 * sub-classes the final responsibility to decide if two records match.
 */
public abstract class RecordMatcherConsumerFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RecordMatcherConsumerFilter.class);

    private final String className = this.getClass().getSimpleName();

    @Override
    public boolean doFilter(FilterInput input, FilterOutput output) throws FilterException {
        logger.info(className + " is about to run.");
        RecordStore file1RecordStore = output.getFile1RecordStore();
        RecordStore file2RecordStore = output.getFile2RecordStore();

        List<Record> toBeRemoved = new ArrayList<>();
        for (Record record1 : file1RecordStore) {
            for (Record record2 : file2RecordStore) {
                //not public code
            }
        }
        toBeRemoved.forEach(file1RecordStore::remove);
        logger.info(this.getClass().getSimpleName() + " has just finished running.");
        return true;
    }

    /**
     * Sub-classes are responsible to decide whether two records matches or not.
     * @param recordOne
     * @param recordTwo
     * @return <code>true</code> if <var>recordOne</var> matches <var>recordTwo</var>; <code>false</code> otherwise.
     */
    protected abstract boolean matches(Record recordOne, Record recordTwo);

    /**
     * Sub-classes are responsible to decide what to do after two records were positively matched.
     * @param record1
     * @param record2
     * @param output
     */
    protected abstract void accept(Record record1, Record record2, FilterOutput output);

}
