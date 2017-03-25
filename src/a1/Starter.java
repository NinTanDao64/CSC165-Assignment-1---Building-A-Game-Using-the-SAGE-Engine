package a1;

import myGameEngine.MoveBackwardKBAction;
import myGameEngine.MoveForwardKBAction;
import myGameEngine.MoveForwardRYAction;
import myGameEngine.MoveLeftKBAction;
import myGameEngine.MoveRightKBAction;
import myGameEngine.MoveRightRYAction;
import myGameEngine.PitchDownKBAction;
import myGameEngine.PitchRYAction;
import myGameEngine.PitchUpKBAction;
import myGameEngine.QuitGameAction;
import myGameEngine.RollLeftKBAction;
import myGameEngine.RollRightKBAction;
import myGameEngine.YawLeftKBAction;
import myGameEngine.YawRYAction;
import myGameEngine.YawRightKBAction;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.input.IInputManager;
import sage.input.action.IAction;
import sage.scene.HUDString;
import sage.scene.SceneNode;
import sage.scene.shape.Cube;
import sage.scene.shape.Line;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Sphere;

public class Starter extends BaseGame {
	private int score = 0;
	private float time = 0;
	private HUDString scoreString;
	private HUDString timeString;
	
	int veggieIdx;
	private static ArrayList<SceneNode> removeList = new ArrayList<SceneNode>(); //All 'plants' in gameworld set to be removed are added here
	private static ArrayList<SceneNode> veggieList = new ArrayList<SceneNode>(); //Add all obtainable 'plants' here
	private static ArrayList<SceneNode> veggieQueue = new ArrayList<SceneNode>(); //Add 'plants' recently collided with here to check which one to place in truck
	private static ArrayList<SceneNode> truckLoad = new ArrayList<SceneNode>(); //Contains all 'plants' after they've been moved to truck
	
	IDisplaySystem display;
	ICamera camera;
	IInputManager im;
	IEventManager eventMgr;
	
	String gpName;
	String kbName;
	int numCrashes = 0;
	
	Truck theTruck;
	Cube box1;
	Cube box1Done;
	Sphere ball;
	Sphere ballDone;
	Pyramid spike;
	Pyramid spikeDone;
	Pyramid spike2;
	Pyramid spike2Done;
	Cube box2;
	Cube box2Done;
	
	/*int secondsPassed = 0;
	int timeSinceCollision = 0;
	Timer myTimer;
	TimerTask task = new TimerTask() {
		public void run() {
			secondsPassed++;
		}
	};
	
	public void startTime() {
		myTimer.scheduleAtFixedRate(task, 1000, 1000);
	}*/
	
	protected void initGame() {
		eventMgr = EventManager.getInstance();
		initGameObjects();
		im = getInputManager();
		gpName = im.getFirstGamepadName();
		kbName = im.getKeyboardName();
		
		IAction zAxisMoveRY = new MoveForwardRYAction(camera, 0.01f);
		IAction zAxisMoveForwardKB = new MoveForwardKBAction(camera, 0.01f);
		IAction zAxisMoveBackwardKB = new MoveBackwardKBAction(camera, 0.01f);
		IAction xAxisMoveRY = new MoveRightRYAction(camera, 0.01f);
		IAction xAxisMoveRightKB = new MoveRightKBAction(camera, 0.01f);
		IAction xAxisMoveLeftKB = new MoveLeftKBAction(camera, 0.01f);
		IAction pitchRY = new PitchRYAction(camera, 0.03f);
		IAction yawRY = new YawRYAction(camera, 0.03f);
		IAction pitchUpKB = new PitchUpKBAction(camera, 0.03f);
		IAction pitchDownKB = new PitchDownKBAction(camera, 0.03f);
		IAction rollRightKB = new RollRightKBAction(camera, 0.01f);
		IAction yawRightKB = new YawRightKBAction(camera, 0.03f);
		IAction yawLeftKB = new YawLeftKBAction(camera, 0.03f);
		IAction rollLeftKB = new RollLeftKBAction(camera, 0.01f);
		IAction quitGame = new QuitGameAction(this);
		
		//Keybindings for first GamePad found in controller array returned by im.getFirstGamepadName();
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.Y, zAxisMoveRY, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.X, xAxisMoveRY, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RY, pitchRY, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RX, yawRY, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._3, quitGame, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		//Keybindings specific to my keyboard setup (Razer BlackWidow Ultimate USB)
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.W, zAxisMoveForwardKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.S, zAxisMoveBackwardKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.D, xAxisMoveRightKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.A, xAxisMoveLeftKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.E, rollRightKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.Q, rollLeftKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.UP, pitchUpKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.DOWN, pitchDownKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.RIGHT, yawRightKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.LEFT, yawLeftKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(im.getKeyboardController(3), net.java.games.input.Component.Identifier.Key.ESCAPE, quitGame, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		
		//Keybindings for first keyboard found in the controller array returned by im.getKeyboardName()
		//Meant for testing in RVR5029 lab
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.W, zAxisMoveForwardKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.S, zAxisMoveBackwardKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.D, xAxisMoveRightKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A, xAxisMoveLeftKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.E, rollRightKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.Q, rollLeftKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.UP, pitchUpKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.DOWN, pitchDownKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.RIGHT, yawRightKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.LEFT, yawLeftKB, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.ESCAPE, quitGame, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		super.update((float) 0.0);
	}
	
	public void update(float elapsedTimeMS) {
		for (SceneNode s : getGameWorld()) {
			if (s.getWorldBound() != null && !(s instanceof Truck) && !(truckLoad.contains(s))) {
				if (s.getWorldBound().contains(camera.getLocation())) {
					removeList.add(s);
					score += 10;
					//timeSinceCollision = secondsPassed;
					numCrashes++;
					veggieQueue.add(s);
					CrashEvent newCrash = new CrashEvent(elapsedTimeMS);
					eventMgr.triggerEvent(newCrash);
				}
			}
		}
		
		if(removeList.size() > 0) {
			removeGameWorldObject(removeList.get(0));
			removeList.remove(0);
		}
		
		if(veggieQueue.size() > 0) {
			for(int i = 0; i < veggieList.size(); i++) {
				if(veggieQueue.get(0)==veggieList.get(i)) {
					addGameWorldObject(truckLoad.get(i));
				}
			}
			veggieQueue.remove(0);
		}
		
		if(theTruck.getTimeSinceCrash() != 0) {
			theTruck.incrementTimeSinceCrash(elapsedTimeMS);
			if(theTruck.getTimeSinceCrash() > 1500) {
				theTruck.revertColor();
				theTruck.resetTimeSinceCrash();
			}
		}
		
		/*if(theTruck.colorChanged() && secondsPassed - timeSinceCollision > 1.5) {
			theTruck.revertColor();
		}*/
		
		scoreString.setText("Score = " + score);
		time += elapsedTimeMS;
		DecimalFormat df = new DecimalFormat("0.0");
		timeString.setText("Time = " + df.format(time/1000));
		
		super.update(elapsedTimeMS);
	}
	
	private void initGameObjects() {
		/*myTimer = new Timer();
		startTime();*/
		
		display = getDisplaySystem();
		display.setTitle("SpaceFarming3D");
		
		timeString = new HUDString("Time = " + time);
		timeString.setLocation(0, 0.05);
		addGameWorldObject(timeString);
		scoreString = new HUDString("Score = " + score);
		addGameWorldObject(scoreString);
		
		camera = display.getRenderer().getCamera();
		camera.setPerspectiveFrustum(45, 1, 0.01, 1000);
		camera.setLocation(new Point3D(1,1,20));
		
		box1 = new Cube();
		Matrix3D box1M = box1.getLocalTranslation();
		box1M.translate(-5, 6, 4);
		box1.setLocalTranslation(box1M);
		box1.scale(0.5f, 0.5f, 0.5f);
		veggieList.add(box1);
		addGameWorldObject(box1);
		
		box1Done = new Cube();
		Matrix3D box1DoneM = box1Done.getLocalTranslation();
		box1DoneM.translate(-1, 0.5, -12);
		box1Done.setLocalTranslation(box1DoneM);
		box1Done.scale(0.5f, 0.5f, 0.5f);
		truckLoad.add(box1Done);
		//addGameWorldObject(box1Done);
		
		ball = new Sphere();
		Matrix3D ballM = ball.getLocalTranslation();
		ballM.translate(-1, 1, 3);
		ball.setLocalTranslation(ballM);
		ball.scale(0.4f, 0.4f, 0.4f);
		veggieList.add(ball);
		addGameWorldObject(ball);
		ball.updateWorldBound();
		
		ballDone = new Sphere();
		Matrix3D ballDoneM = ballDone.getLocalTranslation();
		ballDoneM.translate(0.5, 0.5, -12);
		ballDone.setLocalTranslation(ballDoneM);
		ballDone.scale(0.4f, 0.4f, 0.4f);
		truckLoad.add(ballDone);
		//addGameWorldObject(ballDone);
		
		spike = new Pyramid();
		Matrix3D spikeM = spike.getLocalTranslation();
		spikeM.translate(10, 4, -8);
		spike.setLocalTranslation(spikeM);
		spike.scale(0.5f, 2, 0.5f);
		veggieList.add(spike);
		addGameWorldObject(spike);
		
		spikeDone = new Pyramid();
		Matrix3D spikeDoneM = spikeDone.getLocalTranslation();
		spikeDoneM.translate(2, 2, -12);
		spikeDone.setLocalTranslation(spikeDoneM);
		spikeDone.scale(0.5f, 2, 0.5f);
		truckLoad.add(spikeDone);
		
		spike2 = new Pyramid();
		Matrix3D spike2M = spike2.getLocalTranslation();
		spike2M.translate(5, -2, 2);
		spike2.setLocalTranslation(spike2M);
		spike2.scale(1, 1, 0.5f);
		veggieList.add(spike2);
		addGameWorldObject(spike2);
		
		spike2Done = new Pyramid();
		Matrix3D spike2DoneM = spike2Done.getLocalTranslation();
		spike2DoneM.translate(4, 1, -12);
		spike2Done.setLocalTranslation(spike2DoneM);
		spike2Done.scale(1, 1, 0.5f);
		truckLoad.add(spike2Done);
			
		theTruck = new Truck();
		Matrix3D truckM = theTruck.getLocalTranslation();
		truckM.translate(2, 0, -12);
		theTruck.setLocalTranslation(truckM);
		addGameWorldObject(theTruck);
		
		eventMgr.addListener(theTruck, CrashEvent.class);
		
		Point3D origin = new Point3D(0,0,0);
		Point3D xEnd = new Point3D(100,0,0);
		Point3D yEnd = new Point3D(0,100,0);
		Point3D zEnd = new Point3D(0,0,100);
		Line xAxis = new Line(origin, xEnd, Color.red, 2);
		Line yAxis = new Line(origin, yEnd, Color.green, 2);
		Line zAxis = new Line(origin, zEnd, Color.blue, 2);
		addGameWorldObject(xAxis);
		addGameWorldObject(yAxis);
		addGameWorldObject(zAxis);
	}
	
	public static void main(String[] args) {
		new Starter().start();
	}
}
