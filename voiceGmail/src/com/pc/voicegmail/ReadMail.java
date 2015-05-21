package com.pc.voicegmail;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import android.speech.tts.TextToSpeech;

import com.sun.mail.pop3.POP3Store;

public class ReadMail {
	
	private static String host = "pop.gmail.com";
	private static String port = "995";
	private static String storeType = "pop3s";
	private static String user;
	private static String pwd;
	
	private static String imapHost = "imap.gmail.com";
	private static String imapStoreType = "imaps";
	
	public static void readEmailByIMAP(TextToSpeech tts, String mailAccount, String password) {	
		user = mailAccount;
		pwd = password;
		
		Store emailStore = null;
		Folder emailFolder = null;
	
		Properties properties = new Properties();
		properties.setProperty("mail.store.protocol", "imaps");
		
		try {
			Session emailSession = Session.getDefaultInstance(properties);	
			emailStore = (Store) emailSession.getStore(imapStoreType);	
			emailStore.connect(imapHost, user, password);			
			emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			readMessage(tts, emailFolder);
			
//			readMessage();
			
			emailFolder.close(false);
			emailFolder = null;
			emailStore.close();
			emailStore = null;
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
			if (emailFolder != null)
				try {
					emailFolder.close(false);
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}
			if (emailStore != null)
				try {
					emailStore.close();
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}
	      }
		
		// TODO Auto-generated method stub
		System.out.println("************************** PWD " + mailAccount + " " + password);
	}

	private static void readMessage(TextToSpeech tts, Folder emailFolder) throws MessagingException,
			IOException {
		Message[] messages = emailFolder.getMessages();
		for (int i = 0; i < 10; i++) {
			
			Message message = messages[i];
			System.out.println("----------------------------------");
			System.out.println("Email Number " + (i + 1));
			System.out.println("Subject: " + message.getSubject());
			System.out.println("From: " + message.getFrom()[0]);
			System.out.println("Text: " + message.getContent().toString());

			tts.speak("mail number :" + (i + 1) + message.getSubject(), TextToSpeech.QUEUE_ADD, null);
/*			
			Object msgContent = message.getContent();
			String content = "";
			
			if (msgContent instanceof Multipart) {
				Multipart multipart = (Multipart) msgContent;
				System.out.println("BodyPart" + "MultiPartCount: " + multipart.getCount());

				for (int j = 0; j < multipart.getCount(); j++) {
					BodyPart bodyPart = multipart.getBodyPart(j);

					String disposition = bodyPart.getDisposition();

					if (j == 0) {
					if (disposition != null
							&& (disposition.equalsIgnoreCase("ATTACHMENT"))) {
						System.out.println("Mail have some attachment");

						DataHandler handler = bodyPart.getDataHandler();
						System.out.println("file name : " + handler.getName());
					} else {
						content = bodyPart.getContent().toString();
						System.out.println("getText " + content);
					}
					}
				}
			}
			*/
		}
	}
	
	public static void readEmail(String mailAccount, String password) {
		user = mailAccount;
		pwd = password;
		
		POP3Store emailStore = null;
		Folder emailFolder = null;
	
		Properties properties = new Properties();
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", port);
		properties.put("mail.pop3.starttls.enable", "true");
		
		try {
			/*
			Session emailSession = Session.getInstance(properties, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(user, pwd);
			    }
			});
			*/
			Session emailSession = Session.getDefaultInstance(properties);
			
			emailStore = (POP3Store) emailSession.getStore(storeType);	
			emailStore.connect(host, user, password);			
			emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			Message[] messages = emailFolder.getMessages();
			System.out.println("*****MSG "+messages.length );
			
			emailFolder.close(false);
			emailStore.close();
		} catch (NoSuchProviderException e) {
			if (emailFolder != null)
				try {
					emailFolder.close(false);
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if (emailStore != null)
				try {
					emailStore.close();
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			if (emailFolder != null)
				try {
					emailFolder.close(false);
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if (emailStore != null)
				try {
					emailStore.close();
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		
		// TODO Auto-generated method stub
		System.out.println("************************** PWD " + mailAccount + " " + password);
	}

}
