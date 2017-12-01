package com.tutuka.assessment.reconciliation.matching;

/**
 * We here call a `field` to each one of the values of a record (i.e each column value). Similar to {@link
 * com.tutuka.assessment.reconciliation.model.Record Records} each field can undergo a wide
 * variety of matching operations. Such operations must implement this interface in order to be supported by the processing workflow.
 *
 * @see FieldMatchers
 */
@FunctionalInterface
public interface FieldMatcher {

    /**
     * Executes the actual matching operation against the given fields.
     * @param columnName the header to which these fields belong.
     * @param recordOneField the field from the first file to be compared.
     * @param recordTwoField the field from the second file to be compared.
     * @return <code>true</code> if <var>recordOneField</var> matches <var>recordTwoField</var>, otherwise <code>false</code>.
     */
    boolean matches(String columnName, String recordOneField, String recordTwoField);

}
