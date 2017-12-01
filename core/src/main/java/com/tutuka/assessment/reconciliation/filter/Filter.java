package com.tutuka.assessment.reconciliation.filter;

import com.tutuka.assessment.reconciliation.exception.FilterException;

/**
 * A piece of logic to be executed during CSV processing. Filters will be added to a chain and will be executed
 * sequentially in the defined order.
 */
public interface Filter {

    /**
     * The specific logic to be executed.
     * @param input data sent to the workflow from the calling party.
     * @param output any relevant outcome of the filter can be stored here and passed to the following filter.
     * @return return <code>true</code> to allow the chain to continue, <code>false</code> to stop chain.
     * @throws FilterException if some error during filtering takes place.
     */
    boolean doFilter(FilterInput input, FilterOutput output) throws FilterException;

}
