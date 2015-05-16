package com.ristew.learning;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by riley on 5/15/2015.
 */
public class Skeleman extends MovingEntity {
    Sprite currentSprite;
    ArrayList<Sprite> skeletonSprites;
    ArrayList<Sprite> spriteList;
    float x, y, speedX, speedY;
    float hitpoints;
    int ticks;
    ArrayList<Arrow> stuckArrows;

    public Skeleman(float x, float y) {
        this.x = x;
        this.y = y;
        this.speedX = 0;
        this.speedY = 0;
        this.hitpoints = 10;
        ticks = 0;
        stuckArrows = new ArrayList<>();
        skeletonSprites = new ArrayList<>();
        spriteList = skeletonSprites;
    }

    public void draw(SpriteBatch sb) {
        currentSprite.draw(sb);
    }

    public void update() {

    }
}
