package com.ristew.learning;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * Created by ras on 5/12/15.
 */
public class World {
    ArrayList<Sprite> worldSprites;

    public World () {
        worldSprites = new ArrayList<Sprite>();
        load_sprites();
    }

    void load_sprites() {
        Texture img = new Texture("core/assets/world-tiles.png");
        for (int i = 0; i < img.getWidth(); i += 32) {
            worldSprites.add(new Sprite(img, i, 0, 32, 32));
        }
    }

}
