package com.devsuperior.integrations.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devsuperior.integrations.dto.EmailDTO;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;


public class SendGridEmailService implements EmailService {

  private static Logger LOG = LoggerFactory.getLogger(SendGridEmailService.class);

  @Autowired
  private SendGrid sendGrid;

  public void sendEmail(EmailDTO dto) {

    // Implement the logic to send the email using SendGrid API
    Email from = new Email(dto.getFromEmail(), dto.getFromName());
    Email to = new Email(dto.getTo());
    Content content = new Content(dto.getContentType() , dto.getBody());
    Mail mail = new Mail(from, dto.getSubject(), to, content);

    Request request = new Request();

    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      LOG.info("Sending email to: " + dto.getTo());
      Response response = sendGrid.api(request);

      LOG.info("Status Code: " + response.getStatusCode());
      LOG.info("Response Body: " + response.getBody());
      LOG.info("Response Headers: " + response.getHeaders());

       if (response.getStatusCode() >= 400 && response.getStatusCode() <= 500) {
        LOG.error("Error sending email: " + response.getBody());        
      }
      else {
        LOG.info("Email sent successfully! Status = " + response.getStatusCode());
      }

    } catch (IOException e) {
      LOG.error("IOException occurred while sending email: ", e);
    }
  }    
}