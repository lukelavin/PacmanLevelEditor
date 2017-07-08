package com.lukelavin.leveleditor;

import com.almasb.fxgl.entity.RenderLayer;
import javafx.scene.text.Font;

/**
 * Created by lukel on 7/7/2017.
 */
public class Config
{
    public static final int GRID_SIZE_X = 19;
    public static final int GRID_SIZE_Y = 21;
    public static final int BLOCK_SIZE = 40;
    public static final int UI_SIZE = 200;

    public static int uiOffset(){
        return GRID_SIZE_X * BLOCK_SIZE;
    }

    public static Font font = new Font("Arial", 16);

    public static RenderLayer background = new RenderLayer()
    {
        @Override
        public String name()
        {
            return "background";
        }

        @Override
        public int index()
        {
            return 0;
        }
    };

    public static RenderLayer bottom = new RenderLayer()
    {
        @Override
        public String name()
        {
            return "bottom";
        }

        @Override
        public int index()
        {
            return 1;
        }
    };

    public static RenderLayer top = new RenderLayer()
    {
        @Override
        public String name()
        {
            return "top";
        }

        @Override
        public int index()
        {
            return 100;
        }
    };
}
