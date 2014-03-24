package com.example.ballwars;

import org.cocos2d.layers.CCScene;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;

public class GameOverApp extends CCColorLayer{

	protected CCLabel label;
	
	public static CCScene scene(String message)
	{
		CCScene scene = CCScene.node();
		GameOverApp layer = new GameOverApp(ccColor4B.ccc4(255, 255, 255, 255));
		
		layer.getLabel().setString(message);
		
		scene.addChild(layer);
		
		return scene;
	}
	
	public CCLabel getLabel()
	{
		return label;
	}
	
	protected GameOverApp(ccColor4B color)
	{
		super(color);
		
		this.setIsTouchEnabled(true);
		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		label = CCLabel.makeLabel("Hello There :)", "DroidSans", 32);
		label.setColor(ccColor3B.ccBLACK);
		label.setPosition(winSize.width / 2.0f, winSize.height / 2.0f);
		addChild(label);
		
		this.runAction(CCSequence.actions(CCDelayTime.action(3.0f), CCCallFunc.action(this, "gameOverDone")));
	}
	
	public void gameOverDone()
	{
		Intent i = new Intent("com.example.ballwars.MAINACTIVITY");
		Context context = CCDirector.sharedDirector().getActivity();
		context.startActivity(i);
	}
	
	public boolean ccTouchesEnded(MotionEvent event)
	{
		gameOverDone();
		
		return true;
	}

}
