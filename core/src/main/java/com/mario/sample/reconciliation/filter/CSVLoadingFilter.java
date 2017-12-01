package com.mario.sample.reconciliation.filter;

import com.mario.sample.reconciliation.exception.FilterException;
import com.mario.sample.reconciliation.processing.RecordStore;
import com.mario.sample.reconciliation.processing.RecordStores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;


public class CSVLoadingFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(CSVLoadingFilter.class);

    @Override
    public boolean doFilter(FilterInput input, FilterOutput output) throws FilterException {
        try {
            logger.info("CSVLoadingFilter is about to run.");
            String file1Path = input.getString(FilterInput.FILE1_PATH);
            File csvFile1 = new File(file1Path);

            //Let's fully load file1 to the record store
            RecordStore file1RecordStore = RecordStores.fromFile(csvFile1);
            output.setFile1RecordStore(file1RecordStore);
            output.put(FilterOutput.TOTAL_RECORDS_FILE1, file1RecordStore.getSize());
            logger.info("CSVLoadingFilter has just finished running.");
            logger.debug("CSVLoadingFilter found {} records in file #1.", file1RecordStore.getSize());
            return true;
        } catch (FileNotFoundException e ) {
            logger.error(e.getMessage());
            throw new FilterException(e.getMessage(), e);
        }
    }

}
