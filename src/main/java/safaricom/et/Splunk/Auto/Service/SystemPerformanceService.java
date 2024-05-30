package safaricom.et.Splunk.Auto.Service;

import com.splunk.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import safaricom.et.Splunk.Auto.Config.SecurityConfig;
import safaricom.et.Splunk.Auto.Config.SplunkConfig;
import safaricom.et.Splunk.Auto.Exceptions.SplunkJobInterruptedException;
import safaricom.et.Splunk.Auto.Model.SystemPerformance;
import safaricom.et.Splunk.Auto.Repo.SystemPerformanceRepository;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@org.springframework.stereotype.Service
public class SystemPerformanceService {

    private static SystemPerformanceRepository systemPerformanceRepository;

    private  SplunkConfig splunkConfig;
    private static AuthenticationService authenticationService;

    public SystemPerformanceService(SystemPerformanceRepository systemPerformanceRepository, SplunkConfig splunkConfig, AuthenticationService authenticationService) {
        this.systemPerformanceRepository = systemPerformanceRepository;
        this.splunkConfig = splunkConfig;
        this.authenticationService = authenticationService;
    }

    public static void fetchDailySystemPerformanceData() throws IOException, SplunkJobInterruptedException {

       Service service =  SplunkConfig.configureConnection();
        JobArgs jobArgs = new JobArgs();

        jobArgs.setExecutionMode(JobArgs.ExecutionMode.NORMAL);
        String query = "Search index=os host=*-tbc-* earliest=-60m@m latest=now | multikv | search (source=cpu AND CPU=all) OR source=vmstat | eval PctCPU=100-pctIdle | bucket _time span=8d | stats avg(PctCPU) as Avg_CPU, max(PctCPU) as Peak_CPU, avg(memUsedPct) as Avg_Mem, max(memUsedPct) as Peak_Memory by host, _time | eval Peak=max(tonumber(Peak_CPU),tonumber(Peak_Memory)) | eval Avg_CPU=round(Avg_CPU,2), Avg_Mem=round(Avg_Mem,2) | eval Avg=round(max(tonumber(Avg_CPU),tonumber(Avg_Mem)),1)";
          Job job = service.getJobs().create(query, jobArgs);
        while (!job.isDone()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SplunkJobInterruptedException("Interrupted while waiting for Splunk job to finish", e);
            }

        }

        InputStream results = job.getResults();
        ResultsReaderXml resultsReader = new ResultsReaderXml(results);
        Map<String, String> event;
        while ((event = resultsReader.getNextEvent()) != null) {
            SystemPerformance performance = new SystemPerformance();
            performance.setHost(event.get("host"));

            String avgMemoryValue = event.get("Avg_Mem");
            if (avgMemoryValue != null && !avgMemoryValue.isEmpty()) {
                performance.setAvgMemory(Double.valueOf(avgMemoryValue.trim()));
            }

            String peakMemoryValue = event.get("Peak_Memory");
            if (peakMemoryValue != null && !peakMemoryValue.isEmpty()) {
                performance.setPeakMemory(Double.valueOf(peakMemoryValue.trim()));
            }

            String peakCPUValue = event.get("Peak_CPU");
            if (peakCPUValue != null && !peakCPUValue.isEmpty()) {
                performance.setPeakCPU(Double.valueOf(peakCPUValue.trim()));
            }

            String avgCPUValue = event.get("Avg_CPU");
            if (avgCPUValue != null && !avgCPUValue.isEmpty()) {
                performance.setAvgCPU(Double.valueOf(avgCPUValue.trim()));
            }


            String timeValue = event.get("_time");
            if (timeValue != null && !timeValue.isEmpty()) {
                performance.setTime(timeValue.trim());
            }
            try {
                systemPerformanceRepository.save(performance);
            } catch (DataIntegrityViolationException e) {
                throw new DataIntegrityViolationException("Data integrity violation occurred while saving system performance data", e);
            } catch (DataAccessException e) {
                throw new DataAccessException("Data access error occurred while saving system performance data", e) {};
            }
        }
        resultsReader.close();
    }
    public List<SystemPerformance> getAllPerformance() {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return systemPerformanceRepository.findAll();
    }
    public List<String> getHosts() {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return systemPerformanceRepository.findDistinctByHost();
    }

    public List<SystemPerformance> getSystemPerformances(List<String> hosts, LocalDate fromDate, LocalDate toDate) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return systemPerformanceRepository.findSystemPerformancesByHostsAndDateBetween(hosts, fromDate, toDate);
    }

    public List<SystemPerformance> getRecordsBetweenDates(LocalDate fromDate, LocalDate toDate) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return systemPerformanceRepository.findRecordsBetweenDates(fromDate, toDate);
    }


}