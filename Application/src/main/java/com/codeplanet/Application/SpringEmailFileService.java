// Java Program to Illustrate Creation Of
//Service Interface
package com.codeplanet.Application;


//Importing required classes

import com.codeplanet.Application.SpringEmailFileResponse;

public interface SpringEmailFileService {
	// Method
    // To send a simple email
    String sendSimpleMail(SpringEmailFileResponse details);
 
    // Method
    // To send an email with attachment
    String sendMailWithAttachment(SpringEmailFileResponse details);
}

/*
 * 
 JavaMailSender interface of JavaMail API is used here to send simple text email. 

 To send a more sophisticated email with an attachment, MimeMessage can be used. 
MimeMessageHelper works as a helper class for MimeMessage to add the attachment and other details required to send the mail.
 * 
 */

//EmailService