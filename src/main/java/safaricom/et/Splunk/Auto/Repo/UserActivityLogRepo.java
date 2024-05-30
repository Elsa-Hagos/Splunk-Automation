package safaricom.et.Splunk.Auto.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safaricom.et.Splunk.Auto.Model.UserActivityLog;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityLogRepo extends JpaRepository<UserActivityLog, Long> {
    List<UserActivityLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

}
