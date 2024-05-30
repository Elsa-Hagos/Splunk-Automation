package safaricom.et.Splunk.Auto.Config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Exceptions.EmailSendingException;
import safaricom.et.Splunk.Auto.Exceptions.FetchDataException;
import safaricom.et.Splunk.Auto.Exceptions.SplunkJobInterruptedException;
import safaricom.et.Splunk.Auto.Service.DataService;
import safaricom.et.Splunk.Auto.Service.SystemPerformanceService;

import java.io.IOException;

@Configuration
@EnableScheduling
@ComponentScan("safaricom.et.Splunk.Auto.Config")
public class ScheduleConfig {

    private final DataService dataService;
    public ScheduleConfig(DataService dataService) {
        this.dataService = dataService;
    }

    @Scheduled(cron = "0 30 9 * * *")
    public void fetchDailyData() throws FetchDataException, EmailSendingException{
        DataService.fetchDataByFrequency(Frequency.DAILY);
    }
    @Scheduled(cron = "0 0 1 ? * FRI")
    public void fetchWeeklyData() throws FetchDataException, EmailSendingException {
        DataService.fetchDataByFrequency(Frequency.WEEKLY);
    }
    @Scheduled(cron = "0 0 7 28 * ?")
    public void fetchMonthlyData() throws FetchDataException, EmailSendingException {
        DataService.fetchDataByFrequency(Frequency.MONTHLY);
    }

    @Scheduled(cron = "0 7 * * * *")
    public void fetchHourlyData() throws   IOException, SplunkJobInterruptedException {
        SystemPerformanceService.fetchDailySystemPerformanceData();
    }



}
