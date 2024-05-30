package safaricom.et.Splunk.Auto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Data
@Entity
public class ReportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate generatedDate;
    private byte[] excelFile;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reportTypeId")
    private ReportType reportType;



}

