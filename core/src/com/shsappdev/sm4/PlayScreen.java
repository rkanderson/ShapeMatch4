package com.shsappdev.sm4;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by ryananderson on 3/17/16.
 */
public class PlayScreen implements Screen, InputProcessor, ContactListener {

    public static final float PPM = 32;

    Main game;
    OrthographicCamera gameCamera;
    OrthographicCamera b2drCamera;

    World world;
    Box2DDebugRenderer b2dr;
    ShapeRenderer sr;
    SpriteBatch sb;
    Array<Ball> balls1, balls2; //different arrays for balls of type 1 and 2
    Sorter sorter;
    BallSensor bs0west, bs0southwest, bs1east, bs1southeast; //The ball sensors to the left, right, and left/right halves of bottom

    //input booleans
    boolean reset = false;
    boolean showB2DRLines = false;

    public PlayScreen(Main game) {
        super();
        Gdx.input.setInputProcessor(this);
        this.game = game;


        world = new World(new Vector2(0, -9.8f), false); //A new box2D World with
                                                         // the same gravity as earth!
        world.setContactListener(this);
        b2dr = new Box2DDebugRenderer();
        sr =  new ShapeRenderer();
        sb = new SpriteBatch();

        balls1 = new Array<Ball>();
        balls2 = new Array<Ball>();
        createBall(0, 20, 1);

        sorter = new Sorter(world, 0, 0);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Main.WIDTH, Main.HEIGHT);
        b2drCamera = new OrthographicCamera();
        b2drCamera.setToOrtho(false, Main.WIDTH / PPM, Main.HEIGHT / PPM);

        updateCameraPositions(sorter.body.getPosition().x, sorter.body.getPosition().y + 8);


        //O boy, here comes something mossive...
        bs0west = new BallSensor(world, b2drCamera.position.x - b2drCamera.viewportWidth/2,
                b2drCamera.position.y, 1, b2drCamera.viewportHeight,0);
        bs1east = new BallSensor(world, b2drCamera.position.x + b2drCamera.viewportWidth/2,
                b2drCamera.position.y, 1, b2drCamera.viewportHeight, 1);
        bs0southwest = new BallSensor(world, b2drCamera.position.x - b2drCamera.viewportWidth/4,
                b2drCamera.position.y - b2drCamera.viewportHeight/2, b2drCamera.viewportWidth/2, 1, 0);
        bs1southeast = new BallSensor(world, b2drCamera.position.x+b2drCamera.viewportWidth/4,
                b2drCamera.position.y - b2drCamera.viewportHeight/2, b2drCamera.viewportWidth/2, 1, 1);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //There will be 2 parts to this method
        //  1) The Rendering (drawing to screen)
        //  2) The updating (this method calls update method calls handleInput method)

        //---PART I--- The Rendering
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // These 2 lines
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);    // clear the screen
        // so we can draw new things


        sr.setProjectionMatrix(gameCamera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        //Draw blue balls
        sr.setColor(0, 0.2f, 8.2f, 1);
        for(Ball b : balls1){
            sr.circle(b.body.getPosition().x*PPM, b.body.getPosition().y*PPM, b.radius*PPM);
        }

        //Draw blue ball sensors
        sr.rect(bs0west.body.getPosition().x*PPM-bs0west.width/2*PPM,
                bs0west.body.getPosition().y*PPM-bs0west.height/2*PPM,
                bs0west.width*PPM, bs0west.height*PPM); //blue left (bs0west)
        sr.rect(bs0southwest.body.getPosition().x*PPM-bs0southwest.width/2*PPM,
                bs0southwest.body.getPosition().y*PPM-bs0southwest.height/2*PPM,
                bs0southwest.width*PPM, bs0southwest.height*PPM); //blue lower left (bs0southwest)

        //Draw red balls
        sr.setColor(8.2f, 0.2f, 0, 1);
        for(Ball b : balls2){
            sr.circle(b.body.getPosition().x*PPM, b.body.getPosition().y*PPM, b.radius*PPM);
        }

        //Draw red ball sensors
        sr.rect(bs1east.body.getPosition().x * PPM - bs1east.width / 2 * PPM,
                bs1east.body.getPosition().y * PPM - bs1east.height / 2 * PPM,
                bs1east.width * PPM, bs1east.height * PPM); //red right (bs1east)
        sr.rect(bs1southeast.body.getPosition().x * PPM - bs1southeast.width / 2 * PPM,
                bs1southeast.body.getPosition().y * PPM - bs1southeast.height / 2 * PPM,
                bs1southeast.width * PPM, bs1southeast.height * PPM); //red lower right (bs1southeast)

        sr.setColor(0, 0, 0, 1);
        if(sorter.angle > 0)
            sr.rectLine((sorter.body.getPosition().x - sorter.xDisplacement)*PPM,
                (sorter.body.getPosition().y - sorter.yDisplacement)*PPM,
                (sorter.body.getPosition().x + sorter.xDisplacement)*PPM,
                (sorter.body.getPosition().y + sorter.yDisplacement)*PPM,
                sorter.height*PPM);

        else
            sr.rectLine((sorter.body.getPosition().x - sorter.xDisplacement)*PPM,
                (sorter.body.getPosition().y + sorter.yDisplacement)*PPM,
                (sorter.body.getPosition().x + sorter.xDisplacement)*PPM,
                (sorter.body.getPosition().y - sorter.yDisplacement)*PPM,
                sorter.height*PPM);

        sr.end();

        sb.setProjectionMatrix(gameCamera.combined);
        sb.begin();

        // Draw the player "Sorter" image

        sb.end();







        if(showB2DRLines)b2dr.render(world, b2drCamera.combined); //Shows the outlines of box2D shapes


        //---PART II--- The Updating
        update(delta);

    }

    public void update(float delta){
        handleInput(delta);

        updateCameraPositions(sorter.body.getPosition().x, sorter.body.getPosition().y + 8);

        world.step(1 / 60f, 8, 3); //6 2   // 60 FPS

        //Make a new ball?
        if(Math.random()>.99) createBall(0, b2drCamera.position.y+20, 1);

        //Destroy any balls that need to be destroyed
        for(Ball b: balls1) if(b.isDestroyed) {
            balls1.removeValue(b, true);
            world.destroyBody(b.body);
        }
        for(Ball b: balls2) if(b.isDestroyed) {
            balls2.removeValue(b, true);
            world.destroyBody(b.body);
        }



    }

    //A method to update the cameras' positions
    //@param x and y are in meters not pixels
    public void updateCameraPositions(float x, float y){

        b2drCamera.position.set(x, y, 0);
        b2drCamera.update();

        gameCamera.position.set(b2drCamera.position.x * PPM, b2drCamera.position.y * PPM, 0);
        gameCamera.update();
    }

    public void handleInput(float delta){
        //Probably, a lot of input handling will be done by the InputProcessor interface
        if(reset) game.setScreen(new PlayScreen(game));
    }

    public void gameOver(){
        game.setScreen(new GameOverScreen(game));
    }

    public void createBall(float x, float y, float radius){
        Ball newBall = new Ball(world, x, y, radius);
        if(newBall.type==0)balls1.add(newBall);
        else if(newBall.type==1)balls2.add(newBall);
        else System.out.println("Ball with unknown type created. It will be deleted.");
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.setToOrtho(false, Main.WIDTH, Main.HEIGHT);
        b2drCamera.setToOrtho(false, Main.WIDTH / PPM, Main.HEIGHT / PPM);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        sr.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode== Input.Keys.SPACE) {
            sorter.buttonDown();
        } else if(keycode == Input.Keys.R){
            reset=true;
        } else if(keycode == Input.Keys.TAB){
            showB2DRLines = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.SPACE){
            sorter.buttonReleased();
        } else if(keycode == Input.Keys.TAB){
            showB2DRLines = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        sorter.buttonDown();
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        sorter.buttonReleased();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        //Possibilities I'm looking for
        //1) Ball of type 0 hit sensor of type 0 in which case a match is made
        //2) ball of type 1 hits sensor of type 0 resulting in game over
        //3) ball of type 1 hits sensor of type 1 resulting in a match
        //4) ball of type 0 hits sensor of type 1 resulting in game over

        //First things first, I'll make sure this is a collision between a sensor and ball
        if(fixtureA.getUserData() instanceof Ball.BallInternalSensor && fixtureB.getUserData() instanceof BallSensor ||
                fixtureB.getUserData() instanceof Ball.BallInternalSensor && fixtureA.getUserData() instanceof BallSensor){
            Ball.BallInternalSensor ball = fixtureA.getUserData() instanceof Ball.BallInternalSensor ?
                    (Ball.BallInternalSensor)fixtureA.getUserData() :
                    (Ball.BallInternalSensor)fixtureB.getUserData();
            BallSensor ballSensor = fixtureB.getUserData() instanceof BallSensor ?
                    (BallSensor)fixtureB.getUserData() : (BallSensor)fixtureA.getUserData();

            if(ball.parent.type==0 && ballSensor.type==0){
                //A match with ball and ball Sensor
                //Make the ball explode ; all is happy and keep on playing!
                ball.parent.isDestroyed=true;
            } else if(ball.parent.type==1 && ballSensor.type==0){
                //OOOOOHHHHH GAME OVER
                gameOver();
                //System.out.println("1 mismatched with 0");
            } else if(ball.parent.type==1 && ballSensor.type==1){
                //HELL YAH MATCH!!!! :DDDD
                //System.out.println("1 matched with 1");
                ball.parent.isDestroyed=true;
            } else if(ball.parent.type==0 && ballSensor.type== 1){
                //U LOSE :(
                //System.out.println("0 mismatched with 1");
                gameOver();
            }


        } else return;
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
