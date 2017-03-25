package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class MoveRightRYAction extends AbstractInputAction {
	private ICamera camera;
	private float speed;
	
	public MoveRightRYAction(ICamera c, float s) {
		camera = c;
		speed = s;
	}
	
	public void performAction(float time, net.java.games.input.Event e) {
		Vector3D newLocVector = new Vector3D();
		Vector3D rightDir = camera.getRightAxis().normalize();
		Vector3D curLocVector = new Vector3D(camera.getLocation());
		
		if (e.getValue() < -0.2) {
			newLocVector = curLocVector.minus(rightDir.mult(speed * time));
		} else {
			if (e.getValue() > 0.2) {
				newLocVector = curLocVector.add(rightDir.mult(speed * time));
			} else {
				newLocVector = curLocVector;
			}		
		}
		
		double newX = newLocVector.getX();
		double newY = newLocVector.getY();
		double newZ = newLocVector.getZ();
		Point3D newLoc = new Point3D(newX, newY, newZ);
		camera.setLocation(newLoc);
	}
}