package com.shsappdev.sm4;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by ryananderson on 3/18/16.
 */
public class BallSensor {

    //public static final String SENSOR_TYPE_0 = "sensor type 0";
    //public static final String SENSOR_TYPE_1 = "sensor type 1";

    World world;
    Body body;
    Fixture fixture;
    int type;
    float width, height;

    public BallSensor(World world, float x, float y, float width, float height, int type) {
        this.world = world;
        this.type = type;
        this.width = width;
        this.height = height;

        //make box2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x,y);
        bodyDef.fixedRotation=true;
        body = world.createBody(bodyDef);

        //Create fixture (acts as sensor)
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(width/2, height/2);
        fixtureDef.shape = box;
        fixtureDef.isSensor = true;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
    }
}
