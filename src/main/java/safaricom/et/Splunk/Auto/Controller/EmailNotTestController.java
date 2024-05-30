package safaricom.et.Splunk.Auto.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Exceptions.EmailSendingException;
import safaricom.et.Splunk.Auto.Exceptions.FetchDataException;
import safaricom.et.Splunk.Auto.Exceptions.SplunkJobInterruptedException;
import safaricom.et.Splunk.Auto.Repo.ReportTypeRepo;
import safaricom.et.Splunk.Auto.Service.DataService;
import safaricom.et.Splunk.Auto.Service.SystemPerformanceService;

import java.io.IOException;
//
//@RestController
//@RequestMapping("system")
//
//public class EmailNotTestController {
//
//    private final DataService queryingService;
//
//    private final ReportTypeRepo reportTypeRepo;
//    private final SystemPerformanceService systemPerformanceService;
//
//    public EmailNotTestController(DataService queryingService, ReportTypeRepo reportTypeRepo, SystemPerformanceService systemPerformanceService) {
//        this.queryingService = queryingService;
//        this.reportTypeRepo = reportTypeRepo;
//        this.systemPerformanceService = systemPerformanceService;
//    }
//
//    @GetMapping
//    public ResponseEntity<String> fetchDataByFrequency(@RequestParam Frequency frequency) throws FetchDataException, EmailSendingException, IOException, SplunkJobInterruptedException {
//      //  DataService.fetchDataByFrequency(frequency);
//        systemPerformanceService.fetchDailySystemPerformanceData();
//        return null;
//    }
//
//
//}
