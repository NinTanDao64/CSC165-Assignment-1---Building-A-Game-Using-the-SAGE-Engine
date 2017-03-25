package a1;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Timer;
import java.util.TimerTask;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.TriMesh;

public class Truck extends TriMesh implements IEventListener {
	
	//Red: 1,1,0,1
	//Green: 1,0,1,1
	//Blue: 1,1,0,0
	//Yellow: 1,0,1,0
	//Magenta: 1,0,0,1
	
	private boolean colorChanged = false;
	float timeSinceCrash = 0;

	private static float[] vrts = new float[] {-4,0,-2,4,0,-2,4,0,2,-4,0,2,-4,2,-2,4,2,-2,4,2,2,-4,2,2,-6,0,2,-6,0,-2,-6,4,-2,-4,4,-2,-4,4,2,-6,4,2};
	private static float[] colorBuffer1 = new float[] {1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,1,1,1,0,1,1,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0};
	private static float[] colorBuffer2 = new float[] {255,255,255,0,255,255,255,0,255,255,255,0,255,255,255,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,255,255,255,0,255,255,255,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0};
	private static int[] triangles = new int[] {0,1,2,0,2,3,0,1,4,1,4,5,2,3,6,3,6,7,0,3,4,3,4,7,0,3,12,0,11,12,0,3,9,3,8,9,0,9,10,0,10,11,3,8,12,8,12,13,8,9,10,8,10,13,10,11,12,10,12,13};
	
	FloatBuffer colorBuf1 = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(colorBuffer1);
	FloatBuffer colorBuf2 = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(colorBuffer2);
	
	public Truck() {
		FloatBuffer vertBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
		FloatBuffer colorBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(colorBuffer1);
		IntBuffer triangleBuf = com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
		this.setVertexBuffer(vertBuf);
		this.setColorBuffer(colorBuf);
		this.setIndexBuffer(triangleBuf);
	}
	
	public void revertColor() {
		this.setColorBuffer(colorBuf1);
		colorChanged = false;
	}
	
	public boolean colorChanged() {
		return colorChanged;
	}
	
	public float getTimeSinceCrash() {
		return timeSinceCrash;
	}
	
	public void incrementTimeSinceCrash(float t) {
		this.timeSinceCrash += t;
	}
	
	public void resetTimeSinceCrash() {
		this.timeSinceCrash = 0;
	}
	
	public boolean handleEvent(IGameEvent event) {
		CrashEvent cevent = (CrashEvent) event;
		timeSinceCrash = cevent.getWhichCrash();

		this.setColorBuffer(colorBuf2);
		colorChanged = true;
		//if (crashCount % 2 == 0) this.setColorBuffer(colorBuf1);
		//else this.setColorBuffer(colorBuf2);
		return true;
	}
}
