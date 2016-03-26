package com.shsappdev.sm4;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.omg.CORBA.MARSHAL;

/**
 * Created by ryananderson on 3/19/16.
 */
public class GameOverScreen implements Screen {

    Main game;
    OrthographicCamera cam;
    SpriteBatch sb;
    Texture replay;

    public GameOverScreen(Main game) {
        this.game = game;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Main.WIDTH, Main.HEIGHT);
        cam.position.set(0, 0, 0);
        cam.update();
        sb = new SpriteBatch();

        replay = new Texture("android-replay.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // These 2 lines
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);    // clear the screen
        // so we can draw new things

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(replay, cam.position.x - replay.getWidth() / 2, cam.position.y - replay.getHeight() / 2);
        sb.end();

        //Do some updating right now....
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            game.setScreen(new PlayScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        cam.setToOrtho(false, Main.WIDTH, Main.HEIGHT);


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

    }
}
