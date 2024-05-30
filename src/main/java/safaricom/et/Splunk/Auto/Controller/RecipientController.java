package safaricom.et.Splunk.Auto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safaricom.et.Splunk.Auto.Model.Recipient;
import safaricom.et.Splunk.Auto.Service.RecipientService;
import java.util.List;

@RestController
@RequestMapping("/Recipient")
public class RecipientController {

    @Autowired
    private RecipientService recipientService;

    @GetMapping
    public List<Recipient> getAllRecipients() {
        return recipientService.getAllRecipients();
    }

    @GetMapping("/{name}")
    public Recipient getRecipientByName(@PathVariable String  name) {
        return recipientService.getRecipientByName(name);
    }

    @PostMapping
    public Recipient createRecipient(@RequestBody Recipient recipient) {
        return recipientService.createRecipient(recipient);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Recipient> updateRecipient(@PathVariable String name, @RequestBody Recipient updatedRecipient) {
        Recipient updated = recipientService.updateRecipient(name, updatedRecipient);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    @DeleteMapping("/{name}")
    public void deleteRecipient(@PathVariable String name) {
        recipientService.deleteRecipient(name);
    }
}




