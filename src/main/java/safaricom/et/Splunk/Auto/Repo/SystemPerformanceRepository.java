package safaricom.et.Splunk.Auto.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import safaricom.et.Splunk.Auto.Model.SystemPerformance;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SystemPerformanceRepository extends JpaRepository<SystemPerformance , Long> {
    @Query("SELECT DISTINCT sp.host FROM SystemPerformance sp")
    List<String> findDistinctByHost();
    @Query("SELECT sp FROM SystemPerformance sp WHERE sp.host IN :hosts AND sp.date BETWEEN :fromDate AND :toDate")
        List<SystemPerformance> findSystemPerformancesByHostsAndDateBetween(@Param("hosts") List<String> hosts, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
    @Query("SELECT s FROM SystemPerformance s WHERE s.date BETWEEN :fromDate AND :toDate")
    public List<SystemPerformance> findRecordsBetweenDates(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


}





