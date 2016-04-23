package com.shsappdev.sm4.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.shsappdev.sm4.Main;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration((int)(Main.WIDTH/1.25), (int)(Main.HEIGHT/1.25));
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Main();
        }
}