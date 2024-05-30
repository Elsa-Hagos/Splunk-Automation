package safaricom.et.Splunk.Auto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import safaricom.et.Splunk.Auto.Model.SystemPerformance;
import safaricom.et.Splunk.Auto.Service.SystemPerformanceService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/SystemPerformance")
public class SystemPerformanceController {
    @Autowired
    private SystemPerformanceService systemPerformanceService;

    @GetMapping("/getAll")
    public List<SystemPerformance> getAllPerformance() {
        return systemPerformanceService.getAllPerformance();
    }


    @GetMapping("/getHosts")
    public ResponseEntity<List> getHosts() {
        List<String> hosts = systemPerformanceService.getHosts();
        return ResponseEntity.ok(hosts);
    }

    @GetMapping("/systemPerformance")
    public List<SystemPerformance> getSystemPerformancesByHostsAndDate(@RequestParam List<String> hosts,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return systemPerformanceService.getSystemPerformances(hosts, fromDate, toDate);
    }

    @GetMapping("/records")
    public List<SystemPerformance> getRecordsBetweenDates(@RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                          @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return systemPerformanceService.getRecordsBetweenDates(fromDate, toDate);
    }

}