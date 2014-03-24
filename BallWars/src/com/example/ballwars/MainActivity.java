package com.example.ballwars;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	
	Button play;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		play = (Button)findViewById(R.id.bPlay);
		play.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg) {
		// TODO Auto-generated method stub
		
		switch(arg.getId()){
			
		case R.id.bPlay:			
			Intent i = new Intent("com.example.ballwars.BALLAPP");
			startActivity(i);		
			break;
		}
			
		
	}

	//@Override
/*	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
