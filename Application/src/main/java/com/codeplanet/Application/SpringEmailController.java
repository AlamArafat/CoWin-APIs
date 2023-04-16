package com.codeplanet.Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//Annotation
@RestController
//Class
public class SpringEmailController {
	@Autowired private SpringEmailFileService emailService;
	 
    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody SpringEmailFileResponse details)
    {
        String status
            = emailService.sendSimpleMail(details);
 
        return status;
    }
 
    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
        @RequestBody SpringEmailFileResponse details)
    {
        String status
            = emailService.sendMailWithAttachment(details);
 
        return status;
    }
}
