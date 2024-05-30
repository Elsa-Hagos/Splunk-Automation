package safaricom.et.Splunk.Auto.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import safaricom.et.Splunk.Auto.Config.SecurityConfig;
import safaricom.et.Splunk.Auto.Model.Recipient;
import safaricom.et.Splunk.Auto.Model.RecipientReport;
import safaricom.et.Splunk.Auto.Repo.RecipientRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import safaricom.et.Splunk.Auto.Repo.RecipientReportRepo;

@Service
public  class RecipientService {
    @Autowired
    RecipientRepo recipientRepository;
    @Autowired
    RecipientReportRepo recipientReportRepository;
    private final AuthenticationService authenticationService;

    public RecipientService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public List<Recipient> getAllRecipients() {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return recipientRepository.findAll();
    }

    public Recipient getRecipientByName(String name) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
          Optional<Recipient> recipientOptional = recipientRepository.findByName(name);
            if (recipientOptional.isPresent()) {
            return recipientOptional.get();}
            else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipient found with name: " + name);
               }
            }

    public Recipient createRecipient(Recipient recipient) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
            Optional<Recipient> existingRecipient = recipientRepository.findByName(recipient.getName());
            if (existingRecipient.isPresent()) {
                throw new IllegalArgumentException("Recipient with the same name already exists");
            }
            return recipientRepository.save(recipient);
        }


    public Recipient updateRecipient(String name, Recipient updatedRecipient) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
         Optional<Recipient> recipientOptional = recipientRepository.findByName(name);
        if (recipientOptional.isPresent()) {
            Recipient existingRecipient = recipientOptional.get();
            if (updatedRecipient.getName() != null) {
                existingRecipient.setName(updatedRecipient.getName());
            }
            if (updatedRecipient.getDepartment() != null) {
                existingRecipient.setDepartment(updatedRecipient.getDepartment());
            }
            if (updatedRecipient.getEmail() != null) {
                existingRecipient.setEmail(updatedRecipient.getEmail());
            }
            if (updatedRecipient.getJobTitle() != null) {
                existingRecipient.setJobTitle(updatedRecipient.getJobTitle());
            }
            return recipientRepository.save(existingRecipient);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipient found with name: " + name);
        }
    }

    @Transactional
    public void deleteRecipient(String name) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
       Optional<Recipient> recipientOptional = recipientRepository.findByName(name);
        if (recipientOptional.isPresent()) {
            Recipient recipient = recipientOptional.get();
            List<RecipientReport> recipientReports = recipientReportRepository.findByRecipientId(recipient.getId());
            for (RecipientReport recipientReport : recipientReports) {
                recipientReportRepository.delete(recipientReport);
            }
            recipientRepository.deleteByName(name);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipient with Name: " + name + " found");
        }
    }

}
