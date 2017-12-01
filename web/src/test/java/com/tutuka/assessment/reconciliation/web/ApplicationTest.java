package com.tutuka.assessment.reconciliation.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    private MockMvc restFileMockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.restFileMockMvc =  MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getHome() throws Exception {
        restFileMockMvc.perform(get("/")
                .accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("<title>Transaction Reconciliation</title>")));
    }

    @Test
    public void compareFilesTest() throws IOException, Exception {

        final String file1Name = "ClientMarkoffFile20140113.csv";
        final String file2Name = "TutukaMarkoffFile20140113.csv";

        MockMultipartFile file1 = new MockMultipartFile("file1", file1Name, "text/plain", new ClassPathResource(file1Name).getInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", file2Name, "text/plain", new ClassPathResource(file2Name).getInputStream());

        restFileMockMvc.perform(fileUpload("/")
                        .file(file1)
                        .file(file2)
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Total Records')]/following-sibling::td").number(new Double(306)))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Matching Records')]/following-sibling::td").number(new Double(299)))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Unmatched Records')]/following-sibling::td").number(new Double(7)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Total Records')]/following-sibling::td").number(new Double(305)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Matching Records')]/following-sibling::td").number(new Double(299)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Unmatched Records')]/following-sibling::td").number(new Double(6)))
                .andExpect(xpath("count(//table[@id='report-table-left']//tr)").number(new Double(12)))
                .andExpect(xpath("count(//table[@id='report-table-right']//tr)").number(new Double(12)));
    }

    @Test
    public void compareOneEmptyFileTest() throws IOException, Exception {

        final String file1Name = "some_file.csv";
        final String file2Name = "empty_file.csv";

        MockMultipartFile file1 = new MockMultipartFile("file1", file1Name, "text/plain", new ClassPathResource(file1Name).getInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", file2Name, "text/plain", new ClassPathResource(file2Name).getInputStream());

        restFileMockMvc.perform(fileUpload("/")
                .file(file1)
                .file(file2)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Total Records')]/following-sibling::td").number(new Double(123)))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Matching Records')]/following-sibling::td").number(new Double(0)))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Unmatched Records')]/following-sibling::td").number(new Double(123)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Total Records')]/following-sibling::td").number(new Double(0)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Matching Records')]/following-sibling::td").number(new Double(0)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Unmatched Records')]/following-sibling::td").number(new Double(0)))
                .andExpect(xpath("count(//table[@id='report-table-left']//tr)").number(new Double(124)))
                .andExpect(xpath("count(//table[@id='report-table-right']//tr)").number(new Double(124)));
    }

    @Test
    public void compareFilesJsonTest() throws IOException, Exception {

        final String file1Name = "ClientMarkoffFile20140113.csv";
        final String file2Name = "TutukaMarkoffFile20140113.csv";

        MockMultipartFile file1 = new MockMultipartFile("file1", file1Name, "text/plain", new ClassPathResource(file1Name).getInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", file2Name, "text/plain", new ClassPathResource(file2Name).getInputStream());

        restFileMockMvc.perform(fileUpload("/")
                .file(file1)
                .file(file2)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['totalRecordsFile1'])").value(306))
                .andExpect(jsonPath("$.['matchingRecords'])").value(299))
                .andExpect(jsonPath("$.['unmatchedRecordsFile1'])").value(7))
                .andExpect(jsonPath("$.['totalRecordsFile2'])").value(305))
                .andExpect(jsonPath("$.['unmatchedRecordsFile2'])").value(6))
                .andExpect(jsonPath("$..record1.values[?(@=='0004012314602553')])").value("0004012314602553"))
                .andExpect(jsonPath("$..record2.values[?(@=='0004012314602553')])").doesNotExist())
                .andExpect(jsonPath("$.recordPairs.length()").value(11))
                .andExpect(jsonPath("$..record2").value(hasSize(11)))
                .andExpect(jsonPath("$..record2").value(hasSize(11)));
    }

    @Test
    public void unsupportedFileJsonTest() throws Exception {

        final String file1Name = "config.properties";
        final String file2Name = "TutukaMarkoffFile20140113.csv";

        MockMultipartFile file1 = new MockMultipartFile("file1", file1Name, "text/plain", new ClassPathResource(file1Name).getInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", file2Name, "text/plain", new ClassPathResource(file2Name).getInputStream());

        restFileMockMvc.perform(fileUpload("/")
                .file(file1)
                .file(file2)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value(containsString("config.properties is not a supported file")));
    }

    @Test
    public void unsupportedFileHTMLTest() throws Exception {

        final String file1Name = "config.properties";
        final String file2Name = "TutukaMarkoffFile20140113.csv";

        MockMultipartFile file1 = new MockMultipartFile("file1", file1Name, "text/plain", new ClassPathResource(file1Name).getInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", file2Name, "text/plain", new ClassPathResource(file2Name).getInputStream());

        MvcResult result = restFileMockMvc.perform(fileUpload("/")
                .file(file1)
                .file(file2)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isBadRequest()) //no matter whether the request is HTML or JSON the app always responds with JSON in case of error
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value(containsString("config.properties is not a supported file")))
                .andReturn();

        assertThat(result.getResolvedException().getMessage().contains("config.properties is not a supported file"));
    }

    @Test
    public void missingTransactionIDColumnTest() throws Exception {

        final String file1Name = "missing_transactionID_column-file1.csv";
        final String file2Name = "missing_transactionID_column-file2.csv";

        MockMultipartFile file1 = new MockMultipartFile("file1", file1Name, "text/plain", new ClassPathResource(file1Name).getInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", file2Name, "text/plain", new ClassPathResource(file2Name).getInputStream());

        MvcResult result = restFileMockMvc.perform(fileUpload("/")
                .file(file1)
                .file(file2)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isBadRequest()) //no matter whether the request is HTML or JSON the app always responds with JSON in case of error
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value(containsString("The required 'TransactionID' is not present in record")))
                .andReturn();

        assertThat(result.getResolvedException().getMessage().contains("The required 'TransactionID' is not present in record"));
    }

    @Test
    public void allRecordsMatchTest() throws Exception {

        final String file1Name = "all_good-file1.csv";
        final String file2Name = "all_good-file2.csv";

        MockMultipartFile file1 = new MockMultipartFile("file1", file1Name, "text/plain", new ClassPathResource(file1Name).getInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", file2Name, "text/plain", new ClassPathResource(file2Name).getInputStream());

        restFileMockMvc.perform(fileUpload("/")
                .file(file1)
                .file(file2)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Total Records')]/following-sibling::td").number(new Double(5)))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Matching Records')]/following-sibling::td").number(new Double(5)))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Unmatched Records')]/following-sibling::td").number(new Double(0)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Total Records')]/following-sibling::td").number(new Double(5)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Matching Records')]/following-sibling::td").number(new Double(5)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Unmatched Records')]/following-sibling::td").number(new Double(0)))
                .andExpect(xpath("//table[@id='report-table-left']").doesNotExist())
                .andExpect(xpath("//table[@id='report-table-right']").doesNotExist());
    }

    @Test
    public void emptyLinesTest() throws Exception {

        final String file1Name = "empty_lines_and_missing_values-file1.csv";
        final String file2Name = "all_good-file1.csv";

        MockMultipartFile file1 = new MockMultipartFile("file1", file1Name, "text/plain", new ClassPathResource(file1Name).getInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", file2Name, "text/plain", new ClassPathResource(file2Name).getInputStream());

        restFileMockMvc.perform(fileUpload("/")
                .file(file1)
                .file(file2)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Total Records')]/following-sibling::td").number(new Double(9)))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Matching Records')]/following-sibling::td").number(new Double(0)))
                .andExpect(xpath("//table[@id='summary-table-left']//td[contains(text(),'Unmatched Records')]/following-sibling::td").number(new Double(9)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Total Records')]/following-sibling::td").number(new Double(5)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Matching Records')]/following-sibling::td").number(new Double(0)))
                .andExpect(xpath("//table[@id='summary-table-right']//td[contains(text(),'Unmatched Records')]/following-sibling::td").number(new Double(5)))
                .andExpect(xpath("translate(normalize-space(//table[@id='report-table-left']/div[13]),' ', '')").string(allOf(startsWith("13"), endsWith("P_NzI3OTA5MzFfMTM4NjU5MTE4MC4yNzgz"))));
    }

}
