package com.mario.sample.reconciliation.filter;

import com.mario.sample.reconciliation.exception.FilterException;
import com.mario.sample.reconciliation.exception.ReconciliationException;
import com.mario.sample.reconciliation.model.Record;
import com.mario.sample.reconciliation.model.RecordPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A specific {@link Filter} that will organize existing data for its final presentation to the user.
 */
public class ReportDataOrganizationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ReportDataOrganizationFilter.class);

    @Override
    public boolean doFilter(FilterInput input, FilterOutput output) throws FilterException {
        //not public code
        return true;
    }

    /**
     * Records will be ordered by row number.
     */
    private static class RecordPairComparator implements Comparator<RecordPair> {

        @Override
        public int compare(RecordPair o1, RecordPair o2) {
            if (o1.getRecord1() != null) {
                if (o2.getRecord1() != null) {
                    return doCompare(o1.getRecord1(), o2.getRecord1());
                } else {
                    return doCompare(o1.getRecord1(), o2.getRecord2());
                }
            } else {
                if (o2.getRecord1() != null) {
                    return doCompare(o1.getRecord2(), o2.getRecord1());
                } else {
                    return doCompare(o1.getRecord2(), o2.getRecord2());
                }
            }
        }

        private int doCompare(Record record1, Record record2) {
            Integer row1 = record1.getRow();
            Integer row2 = record2.getRow();
            int compare = row1.compareTo(row2);
            if (compare == 0) {
                //Just adding safety check here to validate that the reconciliation workflow does not have a critical bug
                //This should never happen. If this exception is ever thrown then we have a bug.
                if (record1.equals(record2)) {
                    String message = "The reconciliation workflow missed to find two identical records; this is an " +
                            "erroneous state and therefore the processing outcome cannot be trusted. Processing is aborted now.";
                    logger.error(message);
                    throw new ReconciliationException(message);
                }
            }
            return compare;
        }
    }

}
