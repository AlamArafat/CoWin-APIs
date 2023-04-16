package com.sendEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SpringEmailControllerForOtp {
	@Autowired private SpringEmailServiceInterface emailService;
	 
    // Sending a simple Email
    @PostMapping("/sendMailOtp")
    public String
    sendMail(@RequestBody SpringEmailResponse details)
    {
        String status
            = emailService.sendSimpleMail(details);
 
        return status;
    }
	
}

