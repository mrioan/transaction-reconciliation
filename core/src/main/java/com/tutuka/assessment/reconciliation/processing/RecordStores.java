package com.tutuka.assessment.reconciliation.processing;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Utility factory class providing instances of {@link RecordStore RecordStores}. It could be subclassed to create
 * new record store implementations.
 */
public class RecordStores {

    /**
     * Cretes an {@link RecordStore} that will load records from the given file.
     * @param file the file containing the records to be loaded to this record store.
     * @return a {@link FileCSVRecordStore} instance.
     * @throws FileNotFoundException if the given file is not found
     */
    public final static RecordStore fromFile(File file) throws FileNotFoundException {
        return new FileCSVRecordStore(file);
    }

    /**
     * Cretes an {@link RecordStore} where you can lately start adding records.
     * @return a {@link DefaultCSVRecordStore} instance.
     */
    public final static RecordStore newInstance() {
        return new DefaultCSVRecordStore();
    }

}
