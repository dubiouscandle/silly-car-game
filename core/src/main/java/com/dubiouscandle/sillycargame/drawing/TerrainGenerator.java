package com.dubiouscandle.sillycargame.drawing;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.physics.CollisionCategories;
import com.dubiouscandle.sillycargame.utils.Geometry;

public class TerrainGenerator {
	private static final float brightness = 0.6f;
	private static Color Gray = new Color(brightness, brightness, brightness, 1);

	final float max = 99.5f;

	Array<Body> obstacles = new Array<>();

	private Random random;
	private final int numWaves = 50;
	private final float[] hArr = new float[numWaves];
	private final float[] pArr = new float[numWaves];
	private final float[] oArr = new float[numWaves];

	private final float pCoeff = 5f;

	public TerrainGenerator() {
		this("i love computer science".hashCode());
	}

	public TerrainGenerator(long seed) {
		random = new Random(seed);

		generateArrs();
	}

	private void generateArrs() {
		for (int i = 0; i < numWaves; i++) {
			hArr[i] = random.nextFloat();
			pArr[i] = random.nextFloat(pCoeff);
			oArr[i] = random.nextFloat(MathUtils.PI2);
		}
	}

	FloatArray terrain;

	private float amplitude;

	public FloatArray create(World world, float amplitude, float interval) {
		this.amplitude = amplitude;
		createObstacles(world, max);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		Body body = world.createBody(bodyDef);

		ChainShape chainShape = new ChainShape();

		FloatArray vertices = new FloatArray();

		for (float x = 0; x <= max; x += interval) {
			vertices.add(x, amplitude * getHeight(x));
		}
		chainShape.createChain(vertices.items, 0, vertices.size);

		Fixture fixture = body.createFixture(chainShape, 1);
		fixture.setFriction(1);
		fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
		fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;

		terrain = vertices;
		return vertices;
	}

	private void createObstacles(World world, float max) {
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			Body body = world.createBody(bodyDef);
			obstacles.add(body);

			body.setTransform(101, -19.5f, 0);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(1.5f, 0.1f);
			Fixture fixture = body.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			obstacles.add(body);
		}
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			Body body = world.createBody(bodyDef);
			obstacles.add(body);

			body.setTransform(-1, 0, 0);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(1, 100);
			Fixture fixture = body.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			obstacles.add(body);
		}
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			Body body = world.createBody(bodyDef);
			obstacles.add(body);

			body.setTransform(102.5f, 0, 0);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(1, 100);
			Fixture fixture = body.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			obstacles.add(body);
		}
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			Body body = world.createBody(bodyDef);
			obstacles.add(body);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(0.001f, 100f);
			Fixture fixture = body.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			obstacles.add(body);

			body.setTransform(max, amplitude * getHeight(max) - 100, 0);
		}
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			Body body = world.createBody(bodyDef);
			obstacles.add(body);

			body.setTransform(1.5f, 0, 0);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(1.5f, 0.1f);
			Fixture fixture = body.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			obstacles.add(body);
		}

		for (float x = 10f; x < max; x += random.nextFloat(10)) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			Body body = world.createBody(bodyDef);
			obstacles.add(body);

			body.setTransform(x, 1, random.nextFloat(MathUtils.PI2));

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(random.nextFloat(0.1f, 0.3f), random.nextFloat(0.03f, 0.07f));
			Fixture fixture = body.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			obstacles.add(body);
		}

		for (float x = 10f; x < max; x += random.nextFloat(10)) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			Body body = world.createBody(bodyDef);
			obstacles.add(body);

			body.setTransform(x, 1, random.nextFloat(MathUtils.PI2));

			PolygonShape shape = new PolygonShape();

			float[] vertices = new float[] { random.nextFloat(0.001f, 0.4f), random.nextFloat(0.001f, 0.4f),
					random.nextFloat(0.001f, 0.4f), random.nextFloat(0.001f, 0.4f), random.nextFloat(0.001f, 0.4f),
					random.nextFloat(0.001f, 0.4f), };

			float area = Geometry.triangleArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4],
					vertices[5]);
			if (area < 0.01f) {
				continue;
			}

			shape.set(vertices);
			Fixture fixture = body.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			obstacles.add(body);
		}

		for (float x = 10f; x < max; x += random.nextFloat(30)) {
			for (int i = 0; i < 20; i++) {
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyType.DynamicBody;
				Body body = world.createBody(bodyDef);
				obstacles.add(body);

				body.setTransform(x, 1, random.nextFloat(MathUtils.PI2));

				CircleShape shape = new CircleShape();

				float r = random.nextFloat(0.01f, 0.1f);

				shape.setRadius(r);
				Fixture fixture = body.createFixture(shape, 1);
				fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
				fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND
						| CollisionCategories.CATEGORY_CAR;
				obstacles.add(body);
			}
		}

		Array<Body> chain = new Array<>();
		boolean first = true;
		final float w = 0.1f;
		final float h1 = 1.2f;
		final float interval = 2.6f * w;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		for (float x = 25f; x <= 35f; x += interval) {
			Body body = world.createBody(bodyDef);
			body.setLinearDamping(0.1f);
			body.setAngularDamping(0.1f);
			PolygonShape box = new PolygonShape();
			box.setAsBox(w, 0.02f);
			Fixture fixture = body.createFixture(box, 0.1f);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;

			obstacles.add(body);
			body.setTransform(x, h1, 0);

			chain.add(body);
			if (!first) {
				Body fix1 = chain.get(chain.size - 2);
				Body fix2 = chain.get(chain.size - 1);

				DistanceJointDef distanceJointDef = new DistanceJointDef();
				distanceJointDef.bodyA = fix1;
				distanceJointDef.bodyB = fix2;

				distanceJointDef.localAnchorA.set(w, 0);
				distanceJointDef.localAnchorB.set(-w, 0);

				distanceJointDef.dampingRatio = 1;
				distanceJointDef.frequencyHz = 40f;
				distanceJointDef.length = 0;

				world.createJoint(distanceJointDef);
			} else {
				first = false;
			}
		}

		{
			Body ramp = world.createBody(bodyDef);
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(2f, 0.02f);
			Fixture fixture = ramp.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			ramp.setTransform(3, 3, 0);
			ramp.setType(BodyType.DynamicBody);
			RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
			revoluteJointDef.bodyA = chain.get(0);
			revoluteJointDef.bodyB = ramp;

			revoluteJointDef.enableMotor = true;
			revoluteJointDef.maxMotorTorque = 5f;
			revoluteJointDef.motorSpeed = 2f;
			revoluteJointDef.localAnchorA.set(-w, 0);
			revoluteJointDef.localAnchorB.set(-2f, 0);

			revoluteJointDef.collideConnected = false;
			revoluteJointDef.enableLimit = false;

			world.createJoint(revoluteJointDef);

			obstacles.add(ramp);
		}
		{
			Body ramp = world.createBody(bodyDef);
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(1f, 0.02f);
			Fixture fixture = ramp.createFixture(shape, 1);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_GROUND;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND | CollisionCategories.CATEGORY_CAR;
			ramp.setTransform(3, 3, 0);
			ramp.setType(BodyType.DynamicBody);

			RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
			revoluteJointDef.bodyA = chain.get(chain.size - 1);
			revoluteJointDef.bodyB = ramp;

			revoluteJointDef.enableMotor = true;
			revoluteJointDef.maxMotorTorque = 1f;
			revoluteJointDef.motorSpeed = -2f;
			revoluteJointDef.localAnchorA.set(w, 0);
			revoluteJointDef.localAnchorB.set(-1f, 0);

			revoluteJointDef.collideConnected = false;
			revoluteJointDef.enableLimit = false;

			world.createJoint(revoluteJointDef);

			obstacles.add(ramp);
		}

		chain.get(0).setType(BodyType.StaticBody);
		chain.get(chain.size - 1).setType(BodyType.StaticBody);
		chain.get(chain.size - 1).setTransform(chain.get(chain.size - 1).getTransform().getPosition().add(1, .2f), 0);
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);

		Main.shapeRenderer.setColor(Gray);
		for (int i = 0; i < terrain.size - 2; i += 2) {
			float x1 = terrain.items[(i) % terrain.size];
			float y1 = terrain.items[(i + 1) % terrain.size];
			float x2 = terrain.items[(i + 2) % terrain.size];
			float y2 = terrain.items[(i + 3) % terrain.size];

			temp.clear();
			float[] vertices = new float[] { x1, y1, x2, y2, x2, -1000, x1, -1000 };
			for (int j = 0; j < vertices.length; j++) {
				temp.add(vertices[j]);
			}
			Geometry.fillPoly(Main.shapeRenderer, temp);

		}
		Main.shapeRenderer.setColor(0, 0, 0, 1);
		for (int i = 0; i < terrain.size - 2; i += 2) {
			float x1 = terrain.items[(i) % terrain.size];
			float y1 = terrain.items[(i + 1) % terrain.size];
			float x2 = terrain.items[(i + 2) % terrain.size];
			float y2 = terrain.items[(i + 3) % terrain.size];

			Main.shapeRenderer.setColor(0, 0, 0, 1);
			Main.shapeRenderer.rectLine(x1, y1, x2, y2, Main.WorldStrokeWeight);
		}

		for (Body body : obstacles) {
			for (Fixture fixture : body.getFixtureList()) {
				Shape shape = fixture.getShape();

				if (shape instanceof PolygonShape) {
					PolygonShape polygonShape = (PolygonShape) shape;

					Vector2 vertex = new Vector2();
					FloatArray vertices = new FloatArray();
					for (int i = 0; i < polygonShape.getVertexCount(); i++) {
						polygonShape.getVertex(i, vertex);

						vertex.rotateRad(body.getAngle());
						vertex.add(body.getPosition());

						vertices.add(vertex.x, vertex.y);
					}

					shapeRenderer.setColor(Gray);
					Geometry.fillPoly(shapeRenderer, vertices);
					shapeRenderer.setColor(0, 0, 0, 1);
					Geometry.drawPoly(shapeRenderer, vertices, Main.WorldStrokeWeight);
				} else if (shape instanceof CircleShape) {
					CircleShape circleShape = (CircleShape) shape;

					Vector2 pos = body.getPosition();
					shapeRenderer.setColor(Gray);
					shapeRenderer.circle(pos.x, pos.y, circleShape.getRadius(), 30);
					shapeRenderer.setColor(0, 0, 0, 1);
					Geometry.drawCircle(shapeRenderer, pos.x, pos.y, circleShape.getRadius(), 30,
							Main.WorldStrokeWeight);

				}
			}
		}

		Main.shapeRenderer.end();
	}

	private FloatArray temp = new FloatArray();

	private float getHeight(float x) {
		float sum = 0;

		for (int i = 0; i < numWaves; i++) {
			sum += hArr[i] * MathUtils.sin(pArr[i] * x + oArr[i]);
		}

		sum /= numWaves;
		{
			float exp = (x - 70) / 15f;
			sum += 2.4f * Math.exp(-exp * exp);
		}
		{
			float exp = (x - 30) / 3.66f;
			sum -= 2f * Math.exp(-exp * exp);
		}
		return sum;

	}
}
