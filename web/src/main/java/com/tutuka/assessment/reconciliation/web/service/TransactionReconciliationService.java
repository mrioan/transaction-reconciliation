package com.tutuka.assessment.reconciliation.web.service;

import com.tutuka.assessment.reconciliation.filter.CSVProcessingWorkflow;
import com.tutuka.assessment.reconciliation.filter.FilterOutput;
import com.tutuka.assessment.reconciliation.web.mapper.FilterOutputMapper;
import com.tutuka.assessment.reconciliation.web.model.ReconciliationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class TransactionReconciliationService {

    private final Logger logger = LoggerFactory.getLogger(TransactionReconciliationService.class);

    @Autowired
    private StorageService storageService;

    @Autowired
    private CSVProcessingWorkflow csvProcessingWorkflow;

    public ReconciliationResult reconcile(String file1Name, String file2Name) {
        logger.debug("Reconciliation process is requested to run with files '{}' and '{}'", file1Name, file2Name);
        String errorMessage = "%s is not a supported file. Only files having '.csv' extension are supported.";
        Assert.isTrue(file1Name != null && file1Name.toLowerCase().endsWith(".csv"), String.format(errorMessage, file1Name));
        Assert.isTrue(file2Name != null && file2Name.toLowerCase().endsWith(".csv"), String.format(errorMessage, file2Name));
        FilterOutput output = csvProcessingWorkflow.process(storageService.load(file1Name).toFile(), storageService.load(file2Name).toFile());
        return FilterOutputMapper.toReconciliationResult(output);
    }
}
