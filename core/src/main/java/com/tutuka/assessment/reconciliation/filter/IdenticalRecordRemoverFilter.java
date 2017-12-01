package com.tutuka.assessment.reconciliation.filter;

import com.tutuka.assessment.reconciliation.exception.FilterException;
import com.tutuka.assessment.reconciliation.exception.UnsupportedDataException;
import com.tutuka.assessment.reconciliation.processing.RecordStore;
import com.tutuka.assessment.reconciliation.processing.RecordStores;
import com.tutuka.assessment.reconciliation.model.Record;
import com.tutuka.assessment.reconciliation.processing.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * A specific {@link Filter} used to try to find records that have identical values. This implementation iterates over
 * the second file. For each record in file #2:
 * <ul>
 *     <li>
 *         If an identical record is found in file 1: it will just remove the file 1's record from memory and discard the record from file 2.
 *     </li>
 *     <li>
 *         If no identical record is found in file 1: the record from file 2 will be loaded into memory in a separate map.
 *     </li>
 * </ul>
 * <p>Important Note: records are compared based on their headers. Two records having the exact same data but in different order
 * will match positively.</p>
 */
public class IdenticalRecordRemoverFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(IdenticalRecordRemoverFilter.class);

    @Override
    public boolean doFilter(FilterInput input, FilterOutput output) throws FilterException {
        logger.info("IdenticalRecordRemoverFilter is about to run.");
        String file2Path = input.getString(FilterInput.FILE2_PATH);
        try(CSVParser file2CSVParser = CSVParser.parse(new File(file2Path))) { //autocloseable
            //Let's initialize the record store for file2. Records will be loaded next...
            RecordStore file2RecordStore = RecordStores.newInstance();
            output.setFile2RecordStore(file2RecordStore);

            RecordStore file1RecordStore = output.getFile1RecordStore();
            int matchingRecordsCount = 0;
            int file2RecordCount = 0;
            for (Record file2Record : file2CSVParser) {
                //not public code
            }

            output.put(FilterOutput.TOTAL_RECORDS_FILE2, file2RecordCount);
            output.put(FilterOutput.MATCHING_RECORDS, matchingRecordsCount);
            output.put(FilterOutput.UNMATCHED_RECORDS_FILE1, file1RecordStore.getSize());
            output.put(FilterOutput.UNMATCHED_RECORDS_FILE2, file2RecordStore.getSize());
            logger.info("IdenticalRecordRemoverFilter has just finished running.");
            logger.debug("IdenticalRecordRemoverFilter found {} records in file #2, {} were identical to those of file #1.", file2RecordCount, matchingRecordsCount);
            return true;
        } catch (FileNotFoundException | UnsupportedDataException e) {
            logger.error("Exception during IdenticalRecordRemoverFilter execution: " + e.getMessage());
            throw new FilterException(e.getMessage(), e);
        }
    }

}
