package com.dubiouscandle.sillycargame.physics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.utils.Geometry;

public class InfinityWheel extends Wheel {
	public InfinityWheel(float radius) {
		super(radius, Torque);

		final int iterations = 50;
		float interval = MathUtils.PI2 / iterations;

		for (int i = 0; i < iterations; i++) {
			float theta = i * interval;
			float r = radius / 2 * (1.01f + MathUtils.cos(2 * theta));
			float x = r * MathUtils.cos(theta);
			float y = r * MathUtils.sin(theta);
			shape.add(x, y);
		}
	}

	FloatArray transformedShape = new FloatArray();
	Vector2 vertex = new Vector2();
	FloatArray shape = new FloatArray();

	Vector2 center1 = new Vector2();
	Vector2 center2 = new Vector2();

	@Override
	public void draw(ShapeRenderer shapeRenderer) {
		Vector2 pos = body.getPosition();
		float x = pos.x;
		float y = pos.y;

		transformedShape.clear();
		for (int i = 0; i < shape.size; i += 2) {
			vertex.set(shape.get((i) % shape.size), shape.get((i + 1) % shape.size));

			vertex.rotateRad(body.getAngle());
			vertex.add(x, y);

			transformedShape.add(vertex.x, vertex.y);
		}

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, .6f, .11f, 1);
		Geometry.fillPoly(shapeRenderer, transformedShape);
		shapeRenderer.setColor(0, 0, 0, 1);
		Geometry.drawPoly(shapeRenderer, transformedShape, Main.WorldStrokeWeight);

		shapeRenderer.end();
	}

	@Override
	public void addToWorld(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;

		body = world.createBody(bodyDef);

		ShortArray indices = Main.triangulator.computeTriangles(shape.items, 0, shape.size);

		for (int i = 0; i < indices.size; i += 3) {
			int index1 = 2 * indices.get(i);
			int index2 = 2 * indices.get(i + 1);
			int index3 = 2 * indices.get(i + 2);
			PolygonShape triangle = new PolygonShape();
			
			if (Geometry.triangleArea(shape.get(index1), shape.get(index1 + 1), shape.get(index2),
					shape.get(index2 + 1), shape.get(index3), shape.get(index3 + 1)) < 0.0001f) {
				continue;
			}

			triangle.set(new float[] { shape.get(index1), shape.get(index1 + 1), shape.get(index2),
					shape.get(index2 + 1), shape.get(index3), shape.get(index3 + 1), });

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = triangle;
			fixtureDef.density = Density;
			fixtureDef.friction = Friction;

			Fixture fixture = body.createFixture(fixtureDef);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_CAR;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND;
		}

	}

}
