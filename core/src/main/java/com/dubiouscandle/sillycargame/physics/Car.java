package com.dubiouscandle.sillycargame.physics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.utils.Geometry;

public class Car {
	final float mainBodyDensity = 1f;

	private Array<WheelPlacement> wheelPlacements = new Array<>();

	private Array<Wheel> wheels = new Array<>();
	FloatArray shape;

	private Body mainBody;

	public Car(FloatArray shape) {
		this.shape = shape;
	}

	public void addWheel(WheelPlacement wheelPlacement) {
		wheelPlacements.add(wheelPlacement);
	}

	/**
	 * @apiNote this should only be called once per car
	 */
	public void addToWorld(World world) {
		mainBody = world.createBody(bodyDef);

		ShortArray indices = Main.triangulator.computeTriangles(shape.items, 0, shape.size);
		for (int i = 0; i < indices.size; i += 3) {
			int index1 = 2 * indices.get(i);
			int index2 = 2 * indices.get(i + 1);
			int index3 = 2 * indices.get(i + 2);

			float area = Geometry.triangleArea(shape.get(index1), shape.get(index1 + 1), shape.get(index2),
					shape.get(index2 + 1), shape.get(index3), shape.get(index3 + 1));
			if (area < 0.0001f) {
				continue;
			}

			PolygonShape triangle = new PolygonShape();
			triangle.set(new float[] { shape.get(index1), shape.get(index1 + 1), shape.get(index2),
					shape.get(index2 + 1), shape.get(index3), shape.get(index3 + 1), });

			Fixture fixture = mainBody.createFixture(triangle, mainBodyDensity);
			fixture.getFilterData().categoryBits = CollisionCategories.CATEGORY_CAR;
			fixture.getFilterData().maskBits = CollisionCategories.CATEGORY_GROUND;
		}

		for (WheelPlacement wheelPlacement : wheelPlacements) {
			Wheel wheel = wheelPlacement.type.createWheel(wheelPlacement.radius);

			wheels.add(wheel);

			wheel.addToWorld(world);

			wheel.setTranslation(wheelPlacement.x, wheelPlacement.y);
			WheelJointDef jointDef = new WheelJointDef();
			jointDef.bodyA = mainBody;
			jointDef.bodyB = wheel.body;
			jointDef.localAnchorA.set(wheelPlacement.x, wheelPlacement.y);
			jointDef.enableMotor = true;
			jointDef.maxMotorTorque = wheel.torque;
			jointDef.motorSpeed = -Wheel.MotorSpeed;
			jointDef.dampingRatio = 1;

			jointDef.frequencyHz = 50;
			world.createJoint(jointDef);
		}

	}

	private static BodyDef bodyDef;

	static {
		bodyDef = new BodyDef();

		bodyDef.type = BodyType.DynamicBody;
		bodyDef.active = true;
		bodyDef.allowSleep = false;

		bodyDef.linearDamping = 0.1f;
	}

	public void drawShape(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 1, 0, 1);

		FloatArray local = new FloatArray();
		Vector2 vertex = new Vector2();
		for (Fixture fixture : mainBody.getFixtureList()) {
			local.clear();
			Transform transform = mainBody.getTransform();
			PolygonShape polygonShape = (PolygonShape) fixture.getShape();
			for (int i = 0; i < polygonShape.getVertexCount(); i++) {
				polygonShape.getVertex(i, vertex);
				vertex.rotateRad(transform.getRotation());
				vertex.add(transform.getPosition());
				local.add(vertex.x, vertex.y);
			}
			Geometry.fillPoly(shapeRenderer, local);
		}

		local.clear();
		for (int i = 0; i < shape.size; i += 2) {
			vertex.set(shape.get(i), shape.get(i + 1));
			vertex.rotateRad(mainBody.getAngle());
			vertex.add(mainBody.getPosition());
			local.add(vertex.x, vertex.y);
		}

		shapeRenderer.setColor(0, 0, 0, 1);
		Geometry.drawPoly(shapeRenderer, local, Main.WorldStrokeWeight);
		shapeRenderer.end();

		for (Wheel wheel : wheels) {
			wheel.draw(shapeRenderer);
		}
	}

	public Body getMainBody() {
		return mainBody;
	}

	public void setPos(float tx, float ty) {
		mainBody.setTransform(tx, ty, 0);

		for (Wheel wheel : wheels) {
			Vector2 old = wheel.body.getTransform().getPosition();

			wheel.setTranslation(old.x + tx, old.y + ty);
		}

	}
}
