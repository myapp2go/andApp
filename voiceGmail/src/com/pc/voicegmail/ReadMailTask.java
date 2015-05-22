package com.pc.voicegmail;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;

public class ReadMailTask extends AsyncTask {

	private ProgressDialog statusDialog;
	private Activity readMailActivity;
	
	public ReadMailTask(MainActivity mainActivity) {
		readMailActivity = mainActivity;
	}

	protected void onPreExecute() {
		statusDialog = new ProgressDialog(readMailActivity);
		statusDialog.setMessage("Reading mail...");
		statusDialog.setIndeterminate(false);
		statusDialog.setCancelable(false);
		statusDialog.show();
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		ReadMail.readEmailByIMAP((TextToSpeech)params[1], params[2].toString(), params[3].toString());
		return null;
	}

	@Override
	public void onProgressUpdate(Object... values) {
		statusDialog.setMessage(values[0].toString());

	}

	@Override
	public void onPostExecute(Object result) {
		statusDialog.dismiss();
	}
	
}
