package safaricom.et.Splunk.Auto.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Model.ReportType;
import java.util.List;
import java.util.Optional;


@Repository
public interface ReportTypeRepo extends JpaRepository<ReportType, Long> {
    Optional<ReportType> findByReportName(String reportName);
    List<ReportType> findByFrequency(Frequency frequency);
    void deleteByReportName(String reportName);

}
