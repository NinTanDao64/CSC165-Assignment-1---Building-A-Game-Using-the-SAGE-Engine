package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class YawLeftKBAction extends AbstractInputAction {
	private ICamera camera;
	private float speed;
	
	public YawLeftKBAction(ICamera c, float s) {
		camera = c;
		speed = s;
	}
	
	public void performAction(float time, net.java.games.input.Event e) {
		Matrix3D rotationAmount = new Matrix3D();
		Vector3D viewDir = camera.getViewDirection();
		Vector3D upDir = camera.getUpAxis();
		Vector3D rightDir = camera.getRightAxis();

		rotationAmount.rotate(-speed * time, upDir);
		
		viewDir = viewDir.mult(rotationAmount);
		rightDir = rightDir.mult(rotationAmount);
		camera.setRightAxis(rightDir.normalize());
		camera.setViewDirection(viewDir.normalize());
	}
}