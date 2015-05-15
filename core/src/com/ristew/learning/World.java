package com.ristew.learning;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by ras on 5/12/15.
 */
public class World {
    public static int GRASSTILE = 0;
    public static int DIRTTILE = 1;
    ArrayList<Sprite> worldSprites;
    // Actually 3-tuples - I should probably make this more OO
    ArrayList<Integer[]> worldTiles;
    ArrayList<Sprite> detritus;

    // I can into patterns
    static World theWorld;


    public World () {
        worldSprites = new ArrayList<>();
        worldTiles = new ArrayList<>();
        detritus = new ArrayList<>();
        load_sprites();
    }

    public static World instance() {
        if (theWorld == null) {
            theWorld = new World();
        }
        return theWorld;
    }

    void load_sprites() {
        Texture img = new Texture("core/assets/world-tiles.png");
        for (int i = 0; i < img.getWidth(); i += 32) {
            worldSprites.add(new Sprite(img, i, 0, 32, 32));
        }
        add_tiles(0, 40, 0, 3, DIRTTILE);
        add_tiles(0, 2, 3, 4, GRASSTILE);
        add_tiles(1, 2, 4, 5, GRASSTILE);
        add_tiles(2, 40, 3, 6, GRASSTILE);
    }

    void add_tiles(int startX, int endX, int startY, int endY, int tile) {
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                worldTiles.add(new Integer[]{x, y, tile});
            }
        }
    }

    int get_tile_type(float x, float y) {
        int iX = (int) x / 32;
        int iY = (int) y / 32;
        int ret = -1;
        for (Integer [] a : worldTiles) {
            if (a[0] == iX && a[1] == iY) {
                ret = a[2];
                break;
            }
        }
        return ret;
    }

    void add_detritus(Sprite det) {
        detritus.add(det);
        if (detritus.size() > 10) {
            detritus.remove(0);
        }
    }

    void draw(SpriteBatch batch) {
        for (Integer [] tuple : worldTiles) {
            if (tuple.length != 3) {
                System.out.println("Invalid 3-tuple!");
            }
            else {
                batch.draw(worldSprites.get(tuple[2]), tuple[0] * 32f, tuple[1] * 32f);
            }

        }
        for (Sprite s : detritus) {
            s.draw(batch);
        }
    }

}
