package com.dubiouscandle.sillycargame.physics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Wheel {
	public static float Density = 0.1f;
	public static float Friction = 1f;

	public static float Torque = 0.3f;

	public static float MaxRadius = 0.15f;
	public static float MinRadius = 0.05f;
	public static float MotorSpeed = 2f * MathUtils.PI2;

	protected static BodyDef bodyDef = new BodyDef();

	static {
		bodyDef.active = true;
		bodyDef.allowSleep = false;
		bodyDef.awake = true;
		bodyDef.type = BodyType.DynamicBody;
	}

	protected final float torque;
	protected final float radius;

	protected Body body;

	public Wheel(float radius, float torque) {
		this.radius = radius;
		this.torque = torque;
	}

	public abstract void draw(ShapeRenderer shapeRenderer);

	public abstract void addToWorld(World world);

	public void setTranslation(float translationX, float translationY) {
		body.setTransform(body.getTransform().getPosition().add(translationX, translationY), 0);
	}
}
