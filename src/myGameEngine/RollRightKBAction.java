package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class RollRightKBAction extends AbstractInputAction {
	private ICamera camera;
	private float speed;
	
	public RollRightKBAction(ICamera c, float s) {
		camera = c;
		speed = s;
	}
	
	public void performAction(float time, net.java.games.input.Event e) {
		Matrix3D rotationAmount = new Matrix3D();
		Vector3D viewDir = camera.getViewDirection();
		Vector3D upDir = camera.getUpAxis();
		Vector3D rightDir = camera.getRightAxis();
		
		rotationAmount.rotate(-speed * time, viewDir);
		
		//viewDir = viewDir.mult(rotationAmount);
		rightDir = rightDir.mult(rotationAmount);
		upDir = upDir.mult(rotationAmount);
		camera.setUpAxis(upDir.normalize());
		camera.setRightAxis(rightDir.normalize());
		//camera.setViewDirection(viewDir.normalize());
	}
}