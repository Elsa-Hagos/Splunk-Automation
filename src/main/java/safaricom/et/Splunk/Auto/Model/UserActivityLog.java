package safaricom.et.Splunk.Auto.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class UserActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  userName;
    private String activityType;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;


}


