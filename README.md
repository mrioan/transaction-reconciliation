# Transaction Reconciliation 

## Introduction
This system is a webapp tool that can carry out automatic financial reconciliation between two different sets of data.

This project was built using plain Java for the core processing + [Spring Boot](https://projects.spring.io/spring-boot/) for the web layer (refer to the Implementation Details section for more info)

## Demo
The application has been uploaded to [Heroku](https://heroku.com) so it can be tried out without having to be built and run by yourself. You can see it [here](http://tr-mario.herokuapp.com).

Please note that Heroku will put the app to sleep if no activity is detected in 30 minutes. This means that some delay could be experienced the first time the app is accessed. Subsequent usages of the app will be significantly faster.  

## Build Instructions
This project requires Maven 3.3.9 and JDK 8 to be built. Run the following:

```
$ mvn clean install -Dmaven.test.skip=true
```

## Run Instructions
Once the project has been successfully built, you can run it like this: 

```
$ cd web
$ mvn spring-boot:run
```

or 

```
$ java -jar web/target/transaction-reconciliation-web-0.0.1-SNAPSHOT.jar
```

That's it, the Transaction Reconciliation app is now accessible via [http://localhost:8080](http://localhost:8080)

Note: both Chrome and Safari have been verified to work properly with this app. However Chrome is recommended since it was the browser that was tested out the most during development of this app. 

## Usage Instructions

### HTML
Open your browser and go to `http://localhost:8080`. Once the home page loads you can select 2 CSV files and press the `Compare` button. If you prefer to use your keyboard rather than your mouse (I do!) you can use keys `1`, `2` and `Enter` for the same goal. Once the processing is done, two tables showing a reconciliation summary are displayed. They include:

* Number of total records in file #1
* Number of total records in file #2
* Number of matching records
* Number of unmatched records in file #1
* Number of unmatched records in file #2

If there are unmatched records then a button labeled `Unmatched Report` will be displayed as well. Once it is pressed two new tables will show up below (they disappear if button is pressed again). 

These tables will contain the records for each file that failed reconciliation. During processing, these remaining records are analyzed in order to try to find records that look somehow similar. Each pair that is detected as potentially similar will be displayed side by side in those tables (i.e. in the same line).

Note that the records will be accompanied by the row number in which they were found in the file. For example,  row 10 does not mean the 10th record in the file, it means the record that was found in row number 10 in the file.

### JSON
The system provides a JSON api as well. This is a sample request:
  
```
$ curl -v \
    -F "file1=@samplefile01.csv" \
    -F "file2=@samplefile02.csv" \
    -H "Accept: application/json" \
    http://tr-mario.herokuapp.com
```

The response will be a JSON representation of the same data displayed in the UI. 

### Configuration

The core processing module is configured by means of an internal properties file called `config.properties` located under `core/main/resources`. It is really not necessary but in case you would like to define your own config file you can do so like this:

```no-highlight
$ java -jar -Dconfig.file=PATH_TO_ANOTHER_CONFIG_FILE_HERE web/target/transaction-reconciliation-web-0.0.1-SNAPSHOT.jar
```

To configure web-related aspects of the application refer to [Spring Boot's documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)


## Features
* Configurable via properties file
    * Example: key column, secondary columns, case sensitive columns, max file size, storage dir, etc.
* Responsive UI with animations, async calls and error handling
    * Components are automatically enabled/disabled/faded(in/out) to enhance user experience.
    * Error messages are displayed via animations
    * UI can be operated with the keyboard. Keys: `1`, `2` and `Enter`.
* Web App can be used via HTML and JSON
* The following anomalous conditions are detected and handled accordingly
    * Duplicated key value (`TransactionID` by default) is accepted
    * Missing key header (`TransactionID` by default) aborts processing
    * Missing key value is accepted
    * Requests larger than 512KB are not supported (i.e the sum of the size of both files cannot exceed 512KB)
    * Added the ability to match records even if columns are in different order
    * Files having different columns aborts execution
    * Records having less values than expected are filled with empty values and processed as regular
    * Records having more columns than expected are processed as regular (extra values are omitted)
    * Added support for CSV files not having the header in first line
    * Records not ending with ',' are not supported
    * Empty file is OK
    * Empty lines are OK
    * File having just the header (i.e. no records) is OK
    * Added support for fields having a comma inside (they need to be double quoted)
    * Multi-line records are not supported
    * Files not ending with '.csv' are rejected
* Web app supports i18n (only english is available now)
    
## Implementation Details

### Project Structure
The project has been divided in two different modules: `core` and `web`.
 
* **Core**: Provides all the CSV processing functionality using plain java (no external dependencies are used)
* **Web**: It is a [Spring Boot](https://projects.spring.io/spring-boot/) WebMVC layer relying on the core module for CSV processing. The UI is implemented with [Thymeleaf](http://www.thymeleaf.org/documentation.html) + Javascript + Ajax + Bootstrap + Jquery. 

### Architecture  
The CSV processing logic is architectured as a sequence of filters that can manipulate the data. By default these filters are executed in the following order (new filters can be added very easily):

* **CSVLoadingFilter**: loads the first file entirely to memory in a `MultiValueMap`.
* **IdenticalRecordRemoverFilter**: iterates over the second file. For each record: if an identical record from file 1 is found, then it will just remove the file 1's record from memory and this record will be discarded. Otherwise this record will be 
loaded into memory in a separate map.
* **SimilarRecordFilter**: A configurable filter that compares unmatched records in both maps trying to match them based on the provided strategy. Matching records are removed from memory.
* **ResemblingRecordFilter**: A configurable filter that compares remaining records in both maps trying to find resemblance based on the provided strategy. The concept here is that the system will not longer decide which records are similar (it already did so during the previous step). Now, it will just try to link records that somehow resemble so the user can manually compare them. 
* **ReportDataOrganizationFilter**: Organizes and sorts the remaining records for manual review.
  
#### Matchers
As explained above, both `SimilarRecordFilter` and `ResemblingRecordFilter` are configurable. This allows the existing behaviour to be very simply modified/enhanced/extended if different strategies to detect "similarity" and "resemblance" in the records are desired.

By default, the system uses the following matching functionality to detect *similar* records:
 
- Every field will be compared ignoring spaces (with the exception of `TransactionID`)
- Ignoring case (with the exception of `TransactionID` and case-sensitive columns)
- Abbreviations: an experimental matcher which tries to match abbreviated words ("Corporation" will match "Corp." for example)

Now, for *resemblance*, the system is configured with a single matcher that:
  
- Compares the `TransactionID` value. If they are equal then the records are linked as potentially similar but the user will lately decide so manually.   

## Testing
This project contains a comprehensive set of tests, including the web layer where both HTML and JSON apis are verified.
 
In order to run them, just execute: `> mvn clean verify`

### Coverage
Code coverage is also included. In order to run with coverage do: `> mvn clean verify -Pjacoco` (results can be browsed opening 
`jacoco/target/site/jacoco-aggregate/index.html`)

## Authors

* **Mario Antollini**

