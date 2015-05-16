package com.pc.androidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;

import com.google.tts.TTS;

public class PcTTS extends Activity {

	private TextToSpeech myTts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.
				ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy); 
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		myTts = new TTS(this, ttsInitListener, true);
		myTts = new TextToSpeech(this, ttsInitListener);
		
//		addListenerOnButton();
	}
	
	private TextToSpeech.OnInitListener ttsInitListener = new TextToSpeech.OnInitListener() {
		public void onInit(int version) {
			try {
				readPage();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			myTts.speak("獲得文本框中的字符串", 0, null);
			myTts.speak("Hello Paul, How are you. today is Sunday.", 0, null);
		}
	};
	  
	private void readPage() throws SAXException {
	    try {
	        // Create a URL for the desired page
	    	URL url = new URL("http://www.google.com");
	    	Document doc = null;
			try {
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			    
				String line = "";
			    while ((line = in.readLine()) != null) {
			      System.out.println(line);
			    }
				
				/*
				
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				*/
//				readStream(con.getInputStream());
		        
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setValidating(false);
				dbf.setNamespaceAware(true);
				dbf.setIgnoringComments(false);
				dbf.setIgnoringElementContentWhitespace(false);
				dbf.setExpandEntityReferences(false);
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.parse(new InputSource(in));
				
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
			System.out.println("HELLO);");

	        // Read all the text returned by the server
//	        BufferedReader in = new BufferedReader(con.getInputStream());
	        

			
	        /*
	        String str;
	        while ((str = in.readLine()) != null) {
	            str = in.readLine().toString();
	            System.out.println(str);
	            // str is one line of text; readLine() strips the newline character(s)
	        }
	        */
	        
	    } catch (MalformedURLException e) {
	    } catch (IOException e) {

	    } catch (Exception e) {
	    	int i = 0;
	    	i++;
		}
	}
	
	private void toDOM() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(false);
		dbf.setExpandEntityReferences(false);
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		return db.parse(new InputSource(new StringReader(source)));
	}
}
