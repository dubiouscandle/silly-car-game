package com.dubiouscandle.sillycargame.physics;

public class WheelPlacement {
	public final WheelType type;
	public final float x, y;
	public final float radius;

	public WheelPlacement(WheelType type, float x, float y, float radius) {
		super();
		this.type = type;
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
}
