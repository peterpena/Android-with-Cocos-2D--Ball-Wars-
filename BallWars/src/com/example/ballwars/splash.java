package com.example.ballwars;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class splash extends Activity{
MediaPlayer ourSong;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		//ourSong = MediaPlayer.create(splash.this, R.raw.space_sound);
		
		//SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		//boolean music = getPrefs.getBoolean("checkbox", true);
		
		//if(music == true)
			//ourSong.start();
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(5000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openStartingPoint = new Intent("com.example.ballwars.MAINACTIVITY");
					startActivity(openStartingPoint);
				}
				
			}
		}; 
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//ourSong.release();
		finish();
	}
	
	

}
