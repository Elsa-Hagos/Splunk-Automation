package safaricom.et.Splunk.Auto.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Model.ReportHistory;
import safaricom.et.Splunk.Auto.Model.SystemPerformance;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportHistoryRepo extends JpaRepository<ReportHistory, Long> {
    List<ReportHistory> findByFrequency(Frequency frequency);
    List<ReportHistory> findByGeneratedDateBetween(LocalDate fromDate, LocalDate toDate);
}