package safaricom.et.Splunk.Auto.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class SystemPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = true)
    private String host;
    @Column(nullable = true)
    private String time;
    @Column(nullable = true)
    private Double  avgCPU;
    @Column(nullable = true)
    private Double peakCPU;
    @Column(nullable = true)
    private Double  avgMemory;
    @Column(nullable = true)
    private Double peakMemory;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate date;

}
