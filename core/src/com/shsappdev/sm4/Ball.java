package com.shsappdev.sm4;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import sun.util.resources.cldr.fa.CalendarData_fa_IR;

/**
 * Created by ryananderson on 3/17/16.
 */
public class Ball {

    //This class will use box2d meters

    //public static final String BALL_TYPE_0 = "ball type 0";
    //public static final String BALL_TYPE_1 = "ball type 1";

    World world;
    Body body;
    Fixture fixture;
    Fixture sensorFixture;

    float radius;
    int type;
    boolean isDestroyed = false;

    public Ball(World world, float x, float y, float radius) {
        this.world = world;
        this.radius = radius;

        //randomly determine type
        if(Math.random()>=0.5) type = 0;
        else type = 1;

        //Create box2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation  = false;
        body = world.createBody(bodyDef);

        //shape
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.01f;
        fixtureDef.restitution = 0.9f;
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        //Make a sensor fixture for ballSensots
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.isSensor = true;
        CircleShape tinyCircle = new CircleShape(); tinyCircle.setRadius(0.01f);
        fixtureDef1.shape = tinyCircle;
        sensorFixture = body.createFixture(fixtureDef1);
        sensorFixture.setUserData(new BallInternalSensor(this));

        shape.dispose();
        tinyCircle.dispose();

    }

    public class BallInternalSensor {
        Ball parent;
        public BallInternalSensor(Ball parent){
            this.parent = parent;
        }
    }

}
