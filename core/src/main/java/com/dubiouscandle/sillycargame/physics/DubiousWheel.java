package com.dubiouscandle.sillycargame.physics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.FloatArray;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.utils.Geometry;

public class DubiousWheel extends Wheel {
	static final float HalfSqrt2 = (float) (0.5 * Math.sqrt(2));

	final float hSize;
	final float[] vertices = new float[] { 1, 1, -1, 1, -1, -1, 1, -1 };
	final FloatArray transformedVertices = new FloatArray(8);
	Vector2 corner = new Vector2();

	public DubiousWheel(float radius) {
		super(radius,Torque);
		hSize = radius * HalfSqrt2;
	}

	@Override
	public void draw(ShapeRenderer shapeRenderer) {
		Vector2 pos = body.getPosition();

		transformedVertices.clear();
		for (int i = 0; i < 8; i += 2) {
			corner.set(vertices[i], vertices[i + 1]);
			corner.scl(hSize);
			corner.rotateRad(body.getAngle());
			corner.add(pos);
			transformedVertices.add(corner.x, corner.y);
		}
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 1, 1, 1);
		Geometry.fillPoly(shapeRenderer, transformedVertices);
		shapeRenderer.setColor(0, 0, 0, 1);
		Geometry.drawPoly(shapeRenderer, transformedVertices, Main.WorldStrokeWeight);
		shapeRenderer.end();
	}

	@Override
	public void addToWorld(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;

		body = world.createBody(bodyDef);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(hSize, hSize);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = Density;
		fixtureDef.friction = Friction;
		Fixture fixture = body.createFixture(fixtureDef);

		fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_CAR;
		fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND;
	}

}
