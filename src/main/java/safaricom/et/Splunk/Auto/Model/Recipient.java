package safaricom.et.Splunk.Auto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "recipient")
        public class Recipient {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique = true)
        private String name;
        private String department;
        @Email(message = "Please provide a valid email address")
        private String email;
        private String jobTitle;
        @CreationTimestamp
        private LocalDate createdDate;
        @UpdateTimestamp
        private LocalDate updatedDate;
        @JsonIgnore
        @EqualsAndHashCode.Exclude
        @JsonManagedReference
        @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER)
        private Set<RecipientReport> reportRecipients = new HashSet<>();
        }



