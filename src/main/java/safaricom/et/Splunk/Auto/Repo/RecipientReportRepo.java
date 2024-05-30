package safaricom.et.Splunk.Auto.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Model.RecipientReport;
import java.util.List;

@Repository
public interface RecipientReportRepo extends JpaRepository<RecipientReport, Long> {
    List<RecipientReport> findByReportTypeId(Long id);
    List<RecipientReport> findByRecipientId(Long id);
    List<RecipientReport> findByReportTypeFrequency(Frequency frequency);
}
