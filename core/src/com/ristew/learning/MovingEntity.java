package com.ristew.learning;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by riley on 5/15/2015.
 */
public abstract class MovingEntity {
    float x, y, speedX, speedY;
    Sprite currentSprite;
    ArrayList<Sprite> spriteList;
    void draw(SpriteBatch sb) {
        currentSprite.draw(sb);
    }
    void update() {

    }

}
