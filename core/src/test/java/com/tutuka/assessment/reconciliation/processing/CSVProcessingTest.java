package com.tutuka.assessment.reconciliation.processing;

import com.tutuka.assessment.reconciliation.exception.FilterException;
import com.tutuka.assessment.reconciliation.exception.HeaderNotFoundException;
import com.tutuka.assessment.reconciliation.exception.ReconciliationException;
import com.tutuka.assessment.reconciliation.filter.*;
import com.tutuka.assessment.reconciliation.model.RecordPair;
import com.tutuka.assessment.reconciliation.model.Record;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class CSVProcessingTest {

    private CSVProcessingWorkflow defaultWorkflow = CSVProcessingWorkflow.instantiate();
    private CSVProcessingWorkflow filterChainWithAbbreviationMatcher = CustomCSVProcessingWorkflow.omitResembling();
    private CSVProcessingWorkflow filterChainWithoutAbbreviationMatcher = CustomCSVProcessingWorkflow.omitResemblingAndAbreviationMatcher();
    private final static ClassLoader classLoader = CSVProcessingTest.class.getClassLoader();

    @Test
    public void happyPathTest() throws FilterException {
        final String filePrefix = "all_good";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(5),output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(0),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(0),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void identicalButWithCommasInFieldsWithQuotesTest() throws FilterException {
        final String filePrefix = "identical_with_commas_in_fields";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(5),output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(0),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(0),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void columnsInDifferentOrderTest() throws FilterException {
        final String filePrefix = "columns_in_different_order";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(5), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(0),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(0),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void recordsWithDifferentSpacesTest() throws FilterException {
        final String filePrefix = "records_with_different_spaces";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(6), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(6), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(6), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void differentSpacesAndCaseTest() throws FilterException {
        final String filePrefix = "different_spaces_and_case";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(6), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(6), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(6), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    //abbreviations will not be detected in this processor since the abbreviation matcher is missing
    @Test
    public void differentSpacesAndCaseAndAbbreviationsTest() throws FilterException {
        String filePrefix = "different_spaces_and_case_and_abbreviations";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(6), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(6), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(1), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(1), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(1, output.getFile1RecordStore().getSize());
        assertEquals(1, output.getFile2RecordStore().getSize());

        filePrefix = "different_spaces_and_case_and_abbreviations";
        output = filterChainWithAbbreviationMatcher.process(getFile1(filePrefix), getFile1(filePrefix));

        assertEquals(new Integer(6), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(6), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(6), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void emptyLinesAndMissingValuesTest() throws FilterException, HeaderNotFoundException {
        final String filePrefix = "empty_lines_and_missing_values";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(9), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(10), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(4), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(6), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(5, output.getFile1RecordStore().getSize());
        assertEquals(6, output.getFile2RecordStore().getSize());

        Record record = output.getFile1RecordStore().iterator().next();
        assertEquals("P_NzQ0MjAwMTZfMTM3OTU5MjE3OS44OTI2", record.get("WalletReference"));
        Assert.assertEquals(2, record.getRow());
    }

    @Test
    public void duplicateKeysAndMissingValuesTest() throws FilterException, HeaderNotFoundException {
        final String filePrefix = "duplicate_keys_and_missing_values";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(7), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(7), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(3), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(4), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(4), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(4, output.getFile1RecordStore().getSize());
        assertEquals(4, output.getFile2RecordStore().getSize());

        //let's assert records are always in order
        Iterator<Record> recordIterator = output.getFile1RecordStore().iterator();
        Record record = recordIterator.next();
        assertEquals("P_NzI2NzA1NjhfMTM4MjQzMzE2OC41Mjk3", record.get("WalletReference"));
        assertEquals(2, record.getRow());
        record = recordIterator.next();
        assertEquals("", record.get("WalletReference"));
        assertEquals(3, record.getRow());
        record = recordIterator.next();
        assertEquals("", record.get("WalletReference"));
        assertEquals(4, record.getRow());
        record = recordIterator.next();
        assertEquals("P_NzIxNTE5NzFfMTM4MDM2NDkwOS43NTM5", record.get("WalletReference"));
        assertEquals(8, record.getRow());
    }

    @Test
    public void resemblingRecordsTest() throws FilterException {
        String filePrefix = "resembling_records";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(2, output.getFile1RecordStore().getSize());
        assertEquals(2, output.getFile2RecordStore().getSize());
        assertEquals(new Integer(7), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(7), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(2), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(2), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getRecordPairs().size());

        filePrefix = "resembling_records";
        output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
        assertEquals(new Integer(7), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(7), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(2), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(2), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        List<RecordPair> recordPairList = output.getRecordPairs();

        assertEquals(3, recordPairList.size());

        //Let's check records are in order
        assertTrue(recordPairList.get(0).getRecord1().toString().contains("0464011844938429"));
        assertEquals(3, recordPairList.get(0).getRecord1().getRow());
        assertEquals(0, recordPairList.get(0).getRecord2().getValues().size());

        assertEquals(0, recordPairList.get(1).getRecord1().getValues().size());
        assertEquals(5, recordPairList.get(1).getRecord2().getRow());
        assertTrue(recordPairList.get(1).getRecord2().toString().contains("0464011844938420"));

        assertEquals(8, recordPairList.get(2).getRecord1().getRow());
        assertTrue(recordPairList.get(2).getRecord1().toString().contains("0161021844238022"));
        assertEquals(3, recordPairList.get(2).getRecord2().getRow());
        assertTrue(recordPairList.get(2).getRecord2().toString().contains("0161021844238022"));
    }

    @Test //this test uses the files that I was given as an example when the project was explained to me
    public void sampleFilesTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        File file1 = new File(classLoader.getResource("TutukaMarkoffFile20140113.csv").getFile());
        File file2 = new File(classLoader.getResource("ClientMarkoffFile20140113.csv").getFile());
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(file1, file2);

        assertEquals(new Integer(305),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(306),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(288),output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(17),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(18),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(17, output.getFile1RecordStore().getSize());
        assertEquals(18, output.getFile2RecordStore().getSize());

        //let's verify the internal state of the maps
        RecordMap recordMap1 = (RecordMap) getValue(AbstractCSVRecordStore.class, output.getFile1RecordStore(), "recordMap");
        assertEquals(16, recordMap1.keySet().size());
        RecordMap recordMap2 = (RecordMap) getValue(AbstractCSVRecordStore.class, output.getFile2RecordStore(), "recordMap");
        assertEquals(18, recordMap2.keySet().size());

    }

    @Test
    public void missingTransactionIDValueTest() throws FilterException, NoSuchFieldException, IllegalAccessException, HeaderNotFoundException {
        final String filePrefix = "missing_transactionID_value";
        FilterOutput output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(7),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(1),output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(6),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(4),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());

        //let's verify the internal state of the maps
        RecordMap recordMap1 = (RecordMap) getValue(AbstractCSVRecordStore.class, output.getFile1RecordStore(), "recordMap");
        assertEquals(0, recordMap1.keySet().size());
        RecordMap recordMap2 = (RecordMap) getValue(AbstractCSVRecordStore.class, output.getFile2RecordStore(), "recordMap");
        assertEquals(0, recordMap2.keySet().size());

        List<RecordPair> recordPairList = output.getRecordPairs();
        assertEquals(10, recordPairList.size());
        Iterator<RecordPair> iterator = recordPairList.iterator();

        RecordPair recordPair = iterator.next();
        assertEquals(2, recordPair.getRecord1().getRow());
        assertEquals("2014-01-12 11:25:58", recordPair.getRecord1().get("TransactionDate"));
        assertEquals(0, recordPair.getRecord2().getValues().size());

        recordPair = iterator.next();
        assertEquals(2, recordPair.getRecord2().getRow());
        assertEquals("2014-01-11 22:27:44", recordPair.getRecord2().get("TransactionDate"));
        assertEquals(0, recordPair.getRecord1().getValues().size());

        recordPair = iterator.next();
        assertEquals(3, recordPair.getRecord1().getRow());
        assertEquals("FRIENDLY GROCER           JOHANNESBURG  BW", recordPair.getRecord1().get("TransactionNarrative"));
    }

    @Test
    public void missingTransactionIDColumnTest() throws FilterException, NoSuchFieldException, IllegalAccessException, HeaderNotFoundException {
        final String filePrefix = "missing_transactionID_column";
        try {
            defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));
            fail("should have thrown ReconciliationException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("The required 'TransactionID' is not present in record"));
        }
    }

    @Test //validates that the system can handle rows having less columns or extra columns
    public void moreAndLessValuesTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        final String filePrefix = "more_and_less_values";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(3),output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(2),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(2),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(2, output.getFile1RecordStore().getSize());
        assertEquals(2, output.getFile2RecordStore().getSize());
    }

    @Test(expected = ReconciliationException.class)
    public void multiLineRecordMustAbortTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        final String filePrefix = "multi-line_record";
        filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));
    }

    @Test
    public void emptyFileTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        final String filePrefix = "empty_file";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(0), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(5, output.getFile2RecordStore().getSize());
    }

    @Test
    public void onlyTheHeaderTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        final String filePrefix = "only_header";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(5), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(5), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(0), output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(5, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void headerNotInFirstRowTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        final String filePrefix = "header_in_line_10";
        FilterOutput output = filterChainWithoutAbbreviationMatcher.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE1));
        assertEquals(new Integer(5),output.getInteger(FilterOutput.TOTAL_RECORDS_FILE2));
        assertEquals(new Integer(3),output.getInteger(FilterOutput.MATCHING_RECORDS));
        assertEquals(new Integer(2),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1));
        assertEquals(new Integer(2),output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2));
        assertEquals(output.getFile1RecordStore().getSize(), 2);
        assertEquals(output.getFile2RecordStore().getSize(), 2);

        //let's assert records' row
        Iterator<Record> recordIterator = output.getFile1RecordStore().iterator();
        Record record = recordIterator.next();
        assertEquals(2, record.getRow());
        record = recordIterator.next();
        assertEquals(4, record.getRow());
        assertFalse(recordIterator.hasNext());

        recordIterator = output.getFile2RecordStore().iterator();
        record = recordIterator.next();
        assertEquals(11, record.getRow());
        record = recordIterator.next();
        assertEquals(13, record.getRow());
        assertFalse(recordIterator.hasNext());
    }

    @Test(expected = ReconciliationException.class)
    public void differentHeadersTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        final String filePrefix = "different_headers";

        defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));
    }

    private File getFile1(String fileName) {
        return new File(classLoader.getResource(fileName + "-file1.csv").getFile());
    }

    private File getFile2(String fileName) {
        return new File(classLoader.getResource(fileName + "-file2.csv").getFile());
    }

    //Let's use reflection to obtain some internal data
    private Object getValue(Class clazz, Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

}
