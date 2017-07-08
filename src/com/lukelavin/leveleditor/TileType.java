package com.lukelavin.leveleditor;

/**
 * Created by lukel on 7/7/2017.
 */
public enum TileType
{
    BLOCK('B'), BLINKY('b'), PINKY('p'), INKY('i'), CLYDE('c'), PACMAN('P'), PELLET('.'), TELEPORTER('T');

    private char code;

    TileType(char code){
        this.code = code;
    }

    public char getCode()
    {
        return code;
    }
}
