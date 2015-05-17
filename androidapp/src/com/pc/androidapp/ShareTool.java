package com.pc.androidapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;

public class ShareTool {

	static void sharePut(View view, String name) {
		EditText editText = (EditText) view;
		String text = editText.getText().toString();
		
		SharedPreferences pref = view.getContext().getSharedPreferences("MyPref", Activity.MODE_PRIVATE); 
		Editor editor = pref.edit();
		editor.putString(name, text);
		editor.commit();
	}

	static String shareGet(View view, String name) {	
		SharedPreferences pref = view.getContext().getSharedPreferences("MyPref", Activity.MODE_PRIVATE); 
		return pref.getString(name, null);
	}
	
	static String shareGet(Context context, String name) {	
		SharedPreferences pref = context.getSharedPreferences("MyPref", Activity.MODE_PRIVATE); 
		return pref.getString(name, null);
	}
	
}
