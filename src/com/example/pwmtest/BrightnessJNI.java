package com.example.pwmtest;

public class BrightnessJNI {
	private static final String LIB_NAME = "brightnessjni";

	public native int getBrightness();
	public native int setBrightness(int val);


	static {
		System.loadLibrary(LIB_NAME);
	}
}
