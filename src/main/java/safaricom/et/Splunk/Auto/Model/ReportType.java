package safaricom.et.Splunk.Auto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
public class ReportType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10485700)
    private String query;
    @Column(unique = true)
    private String reportName;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    @CreationTimestamp
    private LocalDate createdDate;
    @UpdateTimestamp
    private LocalDate updatedDate;
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "reportType", fetch = FetchType.LAZY)
    private Set<RecipientReport> reportRecipients = new HashSet<>();
}

