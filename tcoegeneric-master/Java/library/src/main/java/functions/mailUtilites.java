package functions;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
public class mailUtilites {
private 	Session session;
private Store store ;
private Transport t;
private Message message;
 private String EmailUser,EmailPassword;
 Address[] fromAddress;
public void mailSessionInit(String configFile ) throws MessagingException, IOException
{
	this.EmailUser=Utilities.getPropertyValue("emailID",configFile); 
	this.EmailPassword = Utilities.getPropertyValue("emailpassword",configFile);
	
		Properties props = new Properties();
		props.put("mail.smtp.host", "webmail.limitedbrands.com");
		props.put("mail.imaps.port", "993");
	    props.put("mail.imaps.socketFactory.fallback", "false");
	    props.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.store.protocol", "imaps");
	    props.put("mail.smtp.host", "webmail.limitedbrands.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "webmail.limitedbrands.com");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.connectiontimeout", "10000");
		this.session = Session.getDefaultInstance(props);
		/*  Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EmailUser, EmailPassword);
			}
		});*/
		this.store = session.getStore("imaps");
		System.out.println(store.isConnected());
		store.connect("webmail.limitedbrands.com",EmailUser,EmailPassword);
		System.out.println(store.isConnected());
	    this.t = session.getTransport("smtp");
	  
	    this.message = new MimeMessage(session);

}
private static  String saveDirectory = "/home/content"; // directory to save the downloaded documents

/**
 * Sets the directory where attached files will be stored.
 * @param dir absolute path of the directory
 */
public void setSaveDirectory(String dir) {
	mailUtilites.saveDirectory = dir;
}
/****************************************************************************
 * Constructor : sendMailWithAttachment
 * Purpose : Sending mail with attachment 
 * Created By :  
 * Created On : 
 * Comments :
 * Updates :
 * @throws Exception 
 ******************************************************************************/

		public  void sendMailWithAttachment(String fromAddress,String toAddress,String msgSubject,String msgBody,String[] attachFiles) throws UnsupportedEncodingException, MessagingException
		{

			InternetAddress fromAddressObj = new InternetAddress(fromAddress, "");
			InternetAddress toAddressObj = null;

			Address[] TO =null;
			//Message msg = new MimeMessage(session);
			/*
			 * if want to send multiple recipients 
			 */
			if (toAddress.contains(";") )
			{
				String[] toAddressary = toAddress.split(";");
				int toAddresslen = toAddressary.length;

				TO = new Address[toAddresslen];
				for ( int i=0;i<toAddresslen;i++)
				{
					TO[i] =  new InternetAddress(toAddressary[i]);
				}
				/* Address[] cc = new Address[] {
	                    ,
	                     new InternetAddress("sWER@gmail.com")};*/

				message.addRecipients(Message.RecipientType.TO,TO);
			}
			else
			{
				toAddressObj =new InternetAddress(toAddress, "");
				message.addRecipient(Message.RecipientType.TO,toAddressObj);
			}

			message.setFrom(fromAddressObj);
			//msg.setRecipients(Message.RecipientType.TO , "");

			message.setSubject(msgSubject);
			message.setSentDate(new Date());

			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msgBody, "text/html");

			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if (attachFiles != null && attachFiles.length > 0) 
			{
				for (String filePath : attachFiles) {
					MimeBodyPart attachPart = new MimeBodyPart();

					try {
						attachPart.attachFile(filePath);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					multipart.addBodyPart(attachPart);
				}
			}
			// sets the multi-part as e-mail's content
			message.setContent(multipart);

			t = session.getTransport("smtp");
			t.connect();
			t.sendMessage(message, message.getAllRecipients());


}
	
	
		/**
		 * Downloads new messages and saves attachments to disk if any.
		 * @param host
		 * @param port
		 * @param userName
		 * @param password
		 * @throws IOException 
		 */
		public  void downloadEmailAttachments() throws IOException {
			
		boolean attachementExist  = false;
		try {
			//Message msg = new MimeMessage(session);
			// connects to the message store
		
		//Message replyMessage = new MimeMessage(session);
			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// fetches new messages from server
			Message[] arrayMessages = folderInbox.getMessages();

			for (int i = 0; i < arrayMessages.length; i++) {
				Message message = arrayMessages[i];
			
				this.fromAddress = message.getFrom();
				String from = fromAddress[0].toString();
				String subject = message.getSubject();
				String sentDate = message.getSentDate().toString();
				Address[] ccAddress = message.getRecipients(RecipientType.CC);
				String contentType = message.getContentType();
				String messageContent = "";
				String to =InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
				// store attachment file name, separated by comma
				String attachFiles = "";
			
				if (contentType.contains("multipart")) {
					// content may contain attachments
					Multipart multiPart = (Multipart) message.getContent();
					int numberOfParts = multiPart.getCount();
					for (int partCount = 0; partCount < numberOfParts; partCount++) {
						MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							// this part is attachment
							attachementExist =true;
							String fileName = part.getFileName();
							attachFiles += fileName + ", ";
							part.saveFile(saveDirectory + File.separator + fileName);

						} else {
							// this part may be the message content
							messageContent = part.getContent().toString();
						}
					}
					if(!attachementExist)
					{
						replayMail( to, "Mail Does not have the attachement \n Thanks  ");
					}

				} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
					
					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}
					else
					{
						if(!attachementExist)
						{
							replayMail( to, "Mail Does not have the attachement \n Thanks  ");
						}
					}
					Object content = message.getContent();
					if (content != null) {
						messageContent = content.toString();
					}
				}

		
			}

			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex11) {
			System.out.println("No provider for pop3.");
			ex11.printStackTrace();
			
		} catch (MessagingException ex12) {
			System.out.println("Could not connect to the message store");
			ex12.printStackTrace();
			
		} catch (IOException ex13) {
			ex13.printStackTrace();
			
	}
}
		public  void replayMail(String to,String msg) throws IOException, MessagingException
		{
			
		//	Message replyMessage = new MimeMessage(session);
			Message replyMessage = (MimeMessage)	 message.reply(true);
			replyMessage.setFrom(new InternetAddress(to));
			replyMessage.setRecipients(RecipientType.CC, message.getRecipients(RecipientType.CC));
			replyMessage.setText(msg);
			replyMessage.setReplyTo( message.getFrom());
			
			  // t = session.getTransport("smtp");
			   t.connect(EmailUser, EmailPassword);
              try {
   	     //connect to the smpt server using transport instance
	     //change the user and password accordingly	
          
             t.sendMessage(replyMessage,
            		 fromAddress);
              } catch (Exception e) {
            	  System.out.println(e.getMessage());
				// TODO: handle exception
			}
              finally {
                 t.close();
              }
              System.out.println("message replied successfully ....");

		}
		
	/*	//Runs this program with Gmail POP3 server
		public static void main(String[] args) throws IOException, MessagingException {
			

			mailUtilites receiver = new mailUtilites();
			receiver.setSaveDirectory(saveDirectory);
			receiver.mailSessionInit("Config.properties");
//			mailUtilites.downloadEmailAttachments(host, port);
			receiver.downloadEmailAttachments();
		} */
}