package com.mario.sample.reconciliation.web.controller;

import com.mario.sample.reconciliation.exception.ReconciliationException;
import com.mario.sample.reconciliation.web.exception.BadRequestException;
import com.mario.sample.reconciliation.web.model.ReconciliationResult;
import com.mario.sample.reconciliation.web.service.StorageService;
import com.mario.sample.reconciliation.web.service.TransactionReconciliationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * The sole controller of this web application providing all operations that are required to interact with it.
 */
@Controller
@RequestMapping(path = "/")
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final StorageService storageService;

    private final TransactionReconciliationService transactionReconciliationService;

    @Autowired
    public HomeController(StorageService storageService, TransactionReconciliationService transactionReconciliationService) {
        this.storageService = storageService;
        this.transactionReconciliationService = transactionReconciliationService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return "home";
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView reconcile(@RequestParam(value = "file1") MultipartFile file1,
                                  @RequestParam(value= "file2") MultipartFile file2) {

        Map<String, Object> modelMap = new HashMap<>();

        ReconciliationResult reconciliationResult = reconcileJson(file1, file2);

        modelMap.put("file1_name", file1.getOriginalFilename());
        modelMap.put("file2_name", file2.getOriginalFilename());
        modelMap.put("reconciliation_result", reconciliationResult);
        return new ModelAndView("result :: reconciliation-fragment", "model", modelMap);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public @ResponseBody ReconciliationResult reconcileJson(
                                        @RequestParam(value = "file1") MultipartFile file1,
                                        @RequestParam(value= "file2") MultipartFile file2) {
        logger.info("A request to reconcile {} with {} has been received.", file1.getOriginalFilename(), file2.getOriginalFilename());
        // Save the files
        storageService.store(file1);
        storageService.store(file2);
        ReconciliationResult reconciliationResult;
        try {
            reconciliationResult = transactionReconciliationService.reconcile(file1.getOriginalFilename(), file2.getOriginalFilename());
        } catch (ReconciliationException | IllegalArgumentException e) {
            logger.warn("Exception occurred during transaction reconciliation.", e);
            throw new BadRequestException(e.getMessage(), e);
        } finally {
            storageService.delete(file1.getOriginalFilename());
            storageService.delete(file2.getOriginalFilename());
        }
        return reconciliationResult;
    }
}
