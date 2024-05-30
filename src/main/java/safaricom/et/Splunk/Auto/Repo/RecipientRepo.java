package safaricom.et.Splunk.Auto.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safaricom.et.Splunk.Auto.Model.Recipient;
import java.util.Optional;


@Repository
    public interface RecipientRepo extends JpaRepository<Recipient, Long> {
      Optional<Recipient> findByName(String name);
      void deleteByName(String name);


}
