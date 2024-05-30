package safaricom.et.Splunk.Auto.Service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Exceptions.EmailSendingException;
import safaricom.et.Splunk.Auto.Model.RecipientReport;
import safaricom.et.Splunk.Auto.Repo.RecipientRepo;
import safaricom.et.Splunk.Auto.Repo.RecipientReportRepo;
import safaricom.et.Splunk.Auto.Repo.ReportHistoryRepo;
import safaricom.et.Splunk.Auto.Repo.ReportTypeRepo;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private ReportHistoryRepo reportHistoryRepo;
    @Autowired
    private RecipientRepo recipientRepo;
    @Autowired
    private ReportTypeRepo reportTypeRepo;
    @Autowired
    private RecipientReportRepo recipientReportRepo;

    @Value("${splunk.mail.host}")
    private String host;

    @Value("${splunk.mail.port}")
    private String port;

    @Value("${splunk.mail.username}")
    private String username;

    @Value("${splunk.mail.password}")
    private String password;


    public void sendEmailWithAttachment(File excelFile, Frequency frequency) throws EmailSendingException {
        try {
            List<RecipientReport> recipientReports = recipientReportRepo.findByReportTypeFrequency(frequency);
            List<String> recipientEmails = recipientReports.stream()
                    .map(recipientReport -> recipientReport.getRecipient().getEmail())
                    .collect(Collectors.toList());
            if (recipientEmails.isEmpty()) {
                throw new IllegalArgumentException("No recipient email addresses found.");
            }
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            session.setDebug(true);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            InternetAddress[] recipientAddresses = new InternetAddress[recipientEmails.size()];
            for (int i = 0; i < recipientEmails.size(); i++) {
                recipientAddresses[i] = new InternetAddress(recipientEmails.get(i));
            }

            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            message.setSubject("Report Attachment");
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent("Please find the attached Excel file.", "text/html");
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(excelFile);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(excelFile.getName());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
          throw new EmailSendingException("Failed to send email: " + e.getMessage(), e);
        }
        }
    }




