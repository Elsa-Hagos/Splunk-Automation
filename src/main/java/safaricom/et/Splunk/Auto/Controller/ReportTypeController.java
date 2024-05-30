package safaricom.et.Splunk.Auto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Model.ReportType;
import safaricom.et.Splunk.Auto.Service.ReportTypeService;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@RestController
@RequestMapping("/Report-Types")
public class ReportTypeController {
    private final ReportTypeService reportTypeService;
    @Autowired
    public ReportTypeController(ReportTypeService reportTypeService) {
        this.reportTypeService = reportTypeService;
    }
    @PostMapping("/create")
    public ResponseEntity<ReportType> createReportType(@RequestParam String query,
                                                       @RequestParam String reportName,
                                                       @RequestParam Set<String> recipientNames,
                                                       @RequestParam Frequency frequency) {
        ReportType reportType = reportTypeService.createReportType(query, reportName, recipientNames, frequency);
        return ResponseEntity.ok(reportType);}
    @PutMapping("/{reportName}")
    public ResponseEntity<ReportType> updateReportType(@PathVariable String reportName,
                                                       @RequestBody ReportType updatedReportType) {
        try {
            ReportType updated = reportTypeService.updateReportType(reportName, updatedReportType);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{reportName}")
    public ResponseEntity<Void> deleteReportType(@PathVariable String reportName) {
            reportTypeService.deleteReportType(reportName);
            return ResponseEntity.ok().build();
    }
    @GetMapping("/{reportName}")
    public ResponseEntity<ReportType> viewReportType(@PathVariable String reportName) {
    Optional<ReportType> reportTypeOptional = reportTypeService.viewReportType(reportName);
    return reportTypeOptional.map(reportType -> ResponseEntity.ok(reportType)).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/all")
    public ResponseEntity<List<ReportType>> viewAllReportTypes() {
        List<ReportType> reportTypes = reportTypeService.viewAllReportTypes();
        return new ResponseEntity<>(reportTypes, HttpStatus.OK);
    }
}
