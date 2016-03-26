package com.shsappdev.sm4;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by ryananderson on 3/17/16.
 */
public class Sorter {
    //The sorter machine the player will control
    //Like a diagonal bar
    // Will deflect falling shapes to the left is the player isn't holding down a button
    //(button can mean pressing key or touch screen)
    // And deflect to the right if the button is being held
    //Only the meter unit from libgdx will be used here.

    static final float defaultAngle = 20;
    static final float length = 4.0f; //length in meters
    static final float height = 0.5f; //height in meters

    World world;
    float angle; //in degrees not radians
    Body body;
    Fixture fixture;
    Texture image;

    public float xDisplacement, yDisplacement; //Displacements from center for drawing rectLine

    public Sorter(World world, float centerX, float centerY) {
        this.world = world;
        angle = defaultAngle; //30 degrees to the left by default.
        //Calculate variables do be used in drawing by the play screen.
        xDisplacement = (float)(Math.cos(Math.toRadians(defaultAngle)) * (length/2));
        yDisplacement = (float)(Math.sin(Math.toRadians(defaultAngle)) * (length/2));


        //Create Box2dBody
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(centerX, centerY);
        bodyDef.fixedRotation=false;
        body = world.createBody(bodyDef);

        //shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(length/2, height/2);

        //Create fixture for collisions
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 1.0f;
        fixtureDef.friction = 0.01f;
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

        updateAngleProperties();

        image = new Texture("sorter.png");
    }

    public void buttonDown(){
        angle = -defaultAngle;
        updateAngleProperties();
    }

    public void buttonReleased(){
        angle = defaultAngle;
        updateAngleProperties();
    }

    public void updateAngleProperties(){

        body.setTransform(body.getPosition().x, body.getPosition().y, angle * MathUtils.degreesToRadians);

    }
}
