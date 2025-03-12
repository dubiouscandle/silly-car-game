package com.dubiouscandle.sillycargame.physics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dubiouscandle.sillycargame.Main;

public class WheelyWheel extends Wheel {
	public WheelyWheel(float radius) {
		super(radius, Torque);
	}

	@Override
	public void draw(ShapeRenderer shapeRenderer) {
		Vector2 pos = body.getPosition();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 1, 1);
		shapeRenderer.circle(pos.x, pos.y, radius, 30);

		shapeRenderer.setColor(0, 0, 0, 1);
		final int iterations = 30;
		float interval = MathUtils.PI2 / iterations;
		for (int i = 0; i < iterations; i++) {
			float ptheta = (i - 1) * interval;
			float theta = i * interval;

			float x1 = pos.x + radius * MathUtils.cos(theta);
			float y1 = pos.y + radius * MathUtils.sin(theta);
			float x2 = pos.x + radius * MathUtils.cos(ptheta);
			float y2 = pos.y + radius * MathUtils.sin(ptheta);

			shapeRenderer.rectLine(x1, y1, x2, y2, Main.WorldStrokeWeight);
			shapeRenderer.circle(x2, y2, Main.WorldStrokeWeight * 0.5f, iterations);
		}

		shapeRenderer.circle(pos.x + 0.8f * radius * MathUtils.cos(body.getAngle()),
				pos.y + 0.8f * radius * MathUtils.sin(body.getAngle()), radius * 0.1f, 30);
		shapeRenderer.circle(pos.x + 0.8f * radius * MathUtils.cos(body.getAngle() + MathUtils.PI2 / 3),
				pos.y + 0.8f * radius * MathUtils.sin(body.getAngle() + MathUtils.PI2 / 3), radius * 0.1f, 30);
		shapeRenderer.circle(pos.x + 0.8f * radius * MathUtils.cos(body.getAngle() + MathUtils.PI2 * 2 / 3),
				pos.y + 0.8f * radius * MathUtils.sin(body.getAngle() + MathUtils.PI2 * 2 / 3), radius * 0.1f, 30);
		shapeRenderer.end();
	}

	@Override
	public void addToWorld(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;

		body = world.createBody(bodyDef);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = Density;
		fixtureDef.friction = Friction;
		Fixture fixture = body.createFixture(fixtureDef);

		fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_CAR;
		fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND;
	}

}
