package com.example.ballwars;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.os.Bundle;

public class BallApp extends Activity{
	
	protected CCGLSurfaceView glSurface;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		glSurface = new CCGLSurfaceView(this);
		setContentView(glSurface);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		CCDirector.sharedDirector().attachInView(glSurface);	
		CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
		CCDirector.sharedDirector().setDisplayFPS(true);
		CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);	
		CCScene scene = GameApp.scene();
		CCDirector.sharedDirector().runWithScene(scene);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CCDirector.sharedDirector().pause();
		GameApp.gameSong.pause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		CCDirector.sharedDirector().resume();
		GameApp.gameSong.start();
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		CCDirector.sharedDirector().end();
		GameApp.gameSong.stop();
	}
	

}
