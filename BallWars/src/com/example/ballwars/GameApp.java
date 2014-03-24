package com.example.ballwars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.MotionEvent;

public class GameApp extends CCColorLayer {

	protected Map<CCSprite, Integer> targets;
	protected ArrayList<Integer> targetIndexes;
	protected ArrayList<Integer> correctIndex;
	protected ArrayList<CCSprite> greenballs;
	protected static MediaPlayer gameSong;
	private boolean once = true;
	CCSprite greenBall;
	int targetsDestroyed;

	public static CCScene scene() {
		// TODO Auto-generated method stub
		CCScene scene = CCScene.node();
		//CCColorLayer layer = new GameApp(ccColor4B.ccc4(0, 191, 255, 255));
		CCColorLayer layer = new GameApp(ccColor4B.ccc4(255, 140, 0, 255));
		scene.addChild(layer);
		return scene;
	}

	protected GameApp(ccColor4B color) {
		// TODO Auto-generated constructor stub
		super(color);
		this.setIsTouchEnabled(true);
		targetsDestroyed = 0;
		targets = new HashMap<CCSprite, Integer>();
		targetIndexes = new ArrayList<Integer>(); 
		greenballs = new ArrayList<CCSprite>();
		correctIndex = new ArrayList<Integer>();
		
		// CGSize winSize = CCDirector.sharedDirector().displaySize();
		// greenBall = CCSprite.sprite("greenball.png");
		// greenBall.setPosition(CGPoint.ccp(
		// greenBall.getContentSize().width / 2.0f, 100));
		// addChild(greenBall);
		// greenBall.setTag(2);
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite hurricanes = CCSprite.sprite("hurricanes.png");
		hurricanes.setPosition(CGPoint.ccp(
				winSize.width / 2.0f, winSize.height/2.0f));
		addChild(hurricanes);
		Context context = CCDirector.sharedDirector().getActivity();
		gameSong = MediaPlayer.create(context, R.raw.small1);
		gameSong.start();

		this.schedule("gameLogic", 1.0f);
		this.schedule("update");

	}

	public boolean ccTouchesEnded(MotionEvent event) {

		CGPoint location = CCDirector.sharedDirector().convertToGL(
				CGPoint.ccp(event.getX(), event.getY()));
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		int offX = (int) (location.x - greenBall.getPosition().x);
		int offY = (int) (location.y - greenBall.getPosition().y);

		if (offX <= 0)
			return true;

		int realX = (int) (winSize.width + (greenBall.getContentSize().width / 2.0f));
		float ratio = (float) offY / (float) offX;
		int realY = (int) ((realX * ratio) + greenBall.getPosition().y);
		CGPoint realDest = CGPoint.ccp(realX, realY);

		int offRealX = (int) (realX - greenBall.getPosition().x);
		int offRealY = (int) (realY - greenBall.getPosition().y);
		float length = (float) Math.sqrt((offRealX * offRealX)
				+ (offRealY * offRealY));
		float velocity = 480.0f / 1.0f; // 480 pixels / 1 sec
		float realMoveDuration = length / velocity;

		greenBall.runAction(CCSequence.actions(
				CCMoveTo.action(realMoveDuration, realDest),
				CCCallFuncN.action(this, "spriteMoveFinished")));

		return true;

	}

	public void gameLogic(float dt) {

		addTarget();
	}

	public void update(float dt) {

		ArrayList<CCSprite> ballsToDelete = new ArrayList<CCSprite>();
		

		for (CCSprite greenBall : greenballs) {

			CGRect greenBallRect = CGRect.make(greenBall.getPosition().x
					- (greenBall.getContentSize().width / 2.0f),
					greenBall.getPosition().y
							- (greenBall.getContentSize().height / 2.0f),
					greenBall.getContentSize().width,
					greenBall.getContentSize().height);
			
			CGSize winSize = CCDirector.sharedDirector().displaySize();
			
			if((greenBall.getPosition().y == (0 + greenBall.getContentSize().height)) ||
			   (greenBall.getPosition().x == (0 + greenBall.getContentSize().width)) ||
			   (greenBall.getPosition().x == (winSize.width - greenBall.getContentSize().width)) ||
			   (greenBall.getPosition().y == (winSize.height - greenBall.getContentSize().height)))
				greenBall.setRotation(180.0f);

			ArrayList<CCSprite> targetsToDelete = new ArrayList<CCSprite>();

			for (CCSprite target : targets.keySet()) {
				CGRect targetRect = CGRect.make(target.getPosition().x
						- (target.getContentSize().width),
						target.getPosition().y
								- (target.getContentSize().height),
						target.getContentSize().width,
						target.getContentSize().height);

				if (CGRect.intersects(greenBallRect, targetRect)){
					targetsToDelete.add(target);
					targetIndexes.add(targets.get(target));

				}

			}

			for (CCSprite target : targetsToDelete) {
				targets.remove(target);
				removeChild(target, true);
				
				for(int index = 0; index < targetIndexes.size(); index++){
					
					System.out.println("targetIndex[" + index + "]:" + targetIndexes.get(index));
					System.out.println("correctIndex[" + index + "]:" + correctIndex.get(index));
					
					if(targetIndexes.get(index) != correctIndex.get(index)){
						CCDirector.sharedDirector().replaceScene(
								GameOverApp.scene("You Lose..."));						
					}
				}
				

				if (++targetsDestroyed >= 5) {
					targetsDestroyed = 0;
					CCDirector.sharedDirector().replaceScene(
							GameOverApp.scene("You Win!"));
				}
			}
			
			if(targetsToDelete.size() > 0)
				ballsToDelete.add(greenBall);
		}
		
		for(CCSprite ball : ballsToDelete){
			greenballs.remove(ball);
			removeChild(ball, true);
		}

	}

	protected void addTarget() {

		Random rand = new Random();
		int actualX;

		greenBall = CCSprite.sprite("greenball.png");
		greenBall.setPosition(CGPoint.ccp(
				greenBall.getContentSize().width / 2.0f, 100));
		addChild(greenBall);
		greenballs.add(greenBall);

		if (once) {
			CCSprite target1 = CCSprite.sprite("greenball1.png");
			CGSize winSize = CCDirector.sharedDirector().displaySize();
			//int minY = (int) ((target1.getContentSize().height / 2.0f) + 20.0f);
			int minY = (int)(winSize.height / 2.0f);
			int maxY = (int) (winSize.height - target1.getContentSize().height / 2.0f);
			int rangeY = maxY - minY;
			actualX = rand.nextInt((int) winSize.width);
			int actualY = rand.nextInt(rangeY) + minY;
			target1.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target1);
			targets.put(target1, 0);
			correctIndex.add(0);

			CCSprite target2 = CCSprite.sprite("greenball2.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target2.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target2);
			targets.put(target2, 1);
			correctIndex.add(1);

			CCSprite target3 = CCSprite.sprite("greenball3.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target3.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target3);
			targets.put(target3, 2);
			correctIndex.add(2);

			CCSprite target4 = CCSprite.sprite("greenball4.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target4.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target4);
			targets.put(target4, 3);
			correctIndex.add(3);

			CCSprite target5 = CCSprite.sprite("greenball5.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target5.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target5);
			targets.put(target5, 4);
			correctIndex.add(4);

			/*CCSprite target6 = CCSprite.sprite("greenball6.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target6.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target6);
			target6.setTag(1);
			targets.add(target6);
			correctIndex.add(5);

			CCSprite target7 = CCSprite.sprite("greenball7.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target7.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target7);
			target7.setTag(1);
			targets.add(target7);
			correctIndex.add(6);

			CCSprite target8 = CCSprite.sprite("greenball8.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target8.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target8);
			target8.setTag(1);
			targets.add(target8);
			correctIndex.add(7);

			CCSprite target9 = CCSprite.sprite("greenball9.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target9.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target9);
			target9.setTag(1);
			targets.add(target9);
			correctIndex.add(8);

			CCSprite target10 = CCSprite.sprite("greenball10.png");
			actualX = rand.nextInt((int) winSize.width);
			actualY = rand.nextInt(rangeY) + minY;
			target10.setPosition(CGPoint.ccp(actualX, actualY));
			addChild(target10);
			target10.setTag(1);
			targets.add(target10);
			correctIndex.add(9);*/

			once = false;
		}

	}

	public void spriteMoveFinished(Object sender) {
		CCSprite sprite = (CCSprite) sender;

		this.removeChild(sprite, true);

	}
}
