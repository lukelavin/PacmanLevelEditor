package com.lukelavin.leveleditor;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static com.lukelavin.leveleditor.Config.*;

/**
 * Created by lukel on 7/7/2017.
 */
public class Main extends GameApplication
{
    private GameEntity[][] level;
    private String tool;
    private GameEntity selecter;

    TextNotificationHandler textNotificationHandler;
    private int fileNum = 0;

    @Override
    protected void initSettings(GameSettings gameSettings)
    {
        gameSettings.setHeight(GRID_SIZE_Y * BLOCK_SIZE);
        gameSettings.setWidth(uiOffset() + UI_SIZE);

        gameSettings.setTitle("Pacman Level Editor");
        gameSettings.setVersion("1.0");
        gameSettings.setProfilingEnabled(false);
        gameSettings.setIntroEnabled(false);
        gameSettings.setMenuEnabled(false);
        gameSettings.setCloseConfirmation(false);
    }

    @Override
    protected void initGame()
    {
        textNotificationHandler  = new TextNotificationHandler();
        initGrid();
        initBorders();
    }

    private void initBorders()
    {
        //top row
        for(int c = 0; c < level[0].length; c++)
        {
            level[0][c] = EntityFactory.makeBlock(c, 0);
            getGameWorld().addEntity(level[0][c]);
        }
        //left column
        for(int r = 1; r < level.length; r++)
        {
            level[r][0] = EntityFactory.makeBlock(0, r);
            getGameWorld().addEntity(level[r][0]);
        }
        //bottom row
        for(int c = 1; c < level[0].length; c++)
        {
            level[level.length - 1][c] = EntityFactory.makeBlock(c, level.length - 1);
            getGameWorld().addEntity(level[level.length - 1][c]);
        }
        //right column
        for(int r = 1; r < level.length - 1; r++)
        {
            level[r][level[0].length - 1] = EntityFactory.makeBlock(level[0].length - 1, r);
            getGameWorld().addEntity(level[r][level[0].length - 1]);
        }
    }

    private void initGrid(){
        level = new GameEntity[21][19];

        //add horizontal gridlines
        for(int r = 0; r <= GRID_SIZE_Y; r++){
            getGameWorld().addEntity(EntityFactory.newGridLine(0, r * BLOCK_SIZE, GRID_SIZE_X * BLOCK_SIZE, 2));
        }
        //add vertical gridlines
        for(int c = 0; c <= GRID_SIZE_X; c++)
        {
            getGameWorld().addEntity(EntityFactory.newGridLine(c * BLOCK_SIZE, 0, 2, GRID_SIZE_Y * BLOCK_SIZE));
        }
    }

    @Override
    protected void initUI()
    {
        initIcons();
        initButtons();
        initText();

        getGameWorld().addEntity(EntityFactory.newBackground());
    }

    private void initIcons()
    {
        //the tool icons are just the entities themselves for simplicity
        //these are only for the user to see, they don't detect clicks
        //the clicks are handled by input based on position, not by the node/entity clicked
        getGameWorld().addEntities(
                EntityFactory.makeBlock(GRID_SIZE_X + 2,3),
                EntityFactory.newBlinky(GRID_SIZE_X + 2, 4),
                EntityFactory.newClyde(GRID_SIZE_X + 2, 5),
                EntityFactory.newInky(GRID_SIZE_X + 2, 6),
                EntityFactory.newPinky(GRID_SIZE_X + 2, 7),
                EntityFactory.newPellet(GRID_SIZE_X + 2, 8),
                EntityFactory.newTeleporter(GRID_SIZE_X + 2, 9),
                EntityFactory.newPlayer(GRID_SIZE_X + 2, 10),
                EntityFactory.newEraser(GRID_SIZE_X + 2, 11));


        //initialize the white square used to designate the current tool selected
        selecter = EntityFactory.newSelectionMarker(GRID_SIZE_X + 2, 3);
        getGameWorld().addEntity(selecter);
        tool = "Block";
    }

    private void initButtons()
    {
        //the clicks are handled by input based on position, not by the node/entity clicked
        //these are just rectangles to signal the "buttons" where the user should click
        Rectangle clear = new Rectangle(uiOffset(), getHeight() - 50,80, 50);
        clear.setFill(Color.TOMATO);
        getGameScene().addUINode(clear);

        Rectangle save = new Rectangle(getWidth() - 80, getHeight() - 50, 80, 50);
        save.setFill(Color.PALEGREEN);
        getGameScene().addUINode(save);
    }

    private void initText()
    {
        Label selectTool = new Label("Select Tool:");
        selectTool.setFont(font);
        selectTool.setTextFill(Color.WHITE);
        selectTool.setTranslateX(uiOffset() + 60);
        selectTool.setTranslateY(90);
        selectTool.setTranslateZ(1);
        getGameScene().addUINode(selectTool);

        Label clear = new Label("Clear");
        clear.setFont(font);
        clear.setTextFill(Color.BLACK);
        clear.setTranslateX(uiOffset() + 16);
        clear.setTranslateY(getHeight() - 35);
        clear.setTranslateZ(1);
        getGameScene().addUINode(clear);

        Label save = new Label("Save");
        save.setFont(font);
        save.setTextFill(Color.BLACK);
        save.setTranslateX(getWidth() - 55);
        save.setTranslateY(getHeight() - 35);
        save.setTranslateZ(1);
        getGameScene().addUINode(save);
    }

    @Override
    protected void initInput()
    {
        getInput().addAction(new UserAction("MOUSE CLICK")
        {
            @Override
            protected void onActionBegin()
            {
                handleClick(getInput().getMousePositionUI());
                //System.out.println("click");
            }
        }, MouseButton.PRIMARY);
    }

    private void handleClick(Point2D mousePos)
    {
        if(mousePos.getX() < uiOffset()) //check if the click is within the grid
        {
            handleGridClick(mousePos);
        }
        else if(mousePos.getX() >= (GRID_SIZE_X + 2) * BLOCK_SIZE && mousePos.getX() < (GRID_SIZE_X + 3) * BLOCK_SIZE)  //check if selecting a tool
        {
            handleToolSelectClick(mousePos);
        }
        else if(mousePos.getY() >= getHeight() - 50) //check if the click is at the bottom
        {
            handleButtonClick(mousePos);
        }
    }

    private void handleGridClick(Point2D mousePos)
    {
        int mouseXTile = (int) (mousePos.getX() / BLOCK_SIZE);
        int mouseYTile = (int) (mousePos.getY() / BLOCK_SIZE);

        if(tool.equals("Block") && level[mouseYTile][mouseXTile] == null){
            level[mouseYTile][mouseXTile] = EntityFactory.makeBlock(mouseXTile, mouseYTile);
            getGameWorld().addEntity(level[mouseYTile][mouseXTile]);
        }
        else if(tool.equals("Blinky") && level[mouseYTile][mouseXTile] == null){
            level[mouseYTile][mouseXTile] = EntityFactory.newBlinky(mouseXTile, mouseYTile);
            getGameWorld().addEntity(level[mouseYTile][mouseXTile]);
        }
        else if(tool.equals("Clyde") && level[mouseYTile][mouseXTile] == null){
            level[mouseYTile][mouseXTile] = EntityFactory.newClyde(mouseXTile, mouseYTile);
            getGameWorld().addEntity(level[mouseYTile][mouseXTile]);
        }
        else if(tool.equals("Inky") && level[mouseYTile][mouseXTile] == null){
            level[mouseYTile][mouseXTile] = EntityFactory.newInky(mouseXTile, mouseYTile);
            getGameWorld().addEntity(level[mouseYTile][mouseXTile]);
        }
        else if(tool.equals("Pinky") && level[mouseYTile][mouseXTile] == null){
            level[mouseYTile][mouseXTile] = EntityFactory.newPinky(mouseXTile, mouseYTile);
            getGameWorld().addEntity(level[mouseYTile][mouseXTile]);
        }
        else if(tool.equals("Pellet") && level[mouseYTile][mouseXTile] == null){
            level[mouseYTile][mouseXTile] = EntityFactory.newPellet(mouseXTile, mouseYTile);
            getGameWorld().addEntity(level[mouseYTile][mouseXTile]);
        }
        else if(tool.equals("Teleporter") && level[mouseYTile][mouseXTile] == null){
            level[mouseYTile][mouseXTile] = EntityFactory.newTeleporter(mouseXTile, mouseYTile);
            getGameWorld().addEntity(level[mouseYTile][mouseXTile]);
        }
        else if(tool.equals("Pacman") && level[mouseYTile][mouseXTile] == null){
            level[mouseYTile][mouseXTile] = EntityFactory.newPlayer(mouseXTile, mouseYTile);
            getGameWorld().addEntity(level[mouseYTile][mouseXTile]);
        }
        else if(tool.equals("Eraser")){
            if(level[mouseYTile][mouseXTile] != null)
            {
                getGameWorld().removeEntity(level[mouseYTile][mouseXTile]);
                level[mouseYTile][mouseXTile] = null;
            }
        }
    }

    private void handleToolSelectClick(Point2D mousePos)
    {
        double toolsXPos = GRID_SIZE_X + 2;

        if(mousePos.getY() >= 12 * BLOCK_SIZE)
        {
            // do nothing if not in proper range of the tools
        }
        else if(mousePos.getY() >= 11 * BLOCK_SIZE)
        {
            tool = "Eraser";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 11 * BLOCK_SIZE));
        }
        else if(mousePos.getY() >= 10 * BLOCK_SIZE)
        {
            tool = "Pacman";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 10 * BLOCK_SIZE));
        }
        else if(mousePos.getY() > 9 * BLOCK_SIZE)
        {
            tool = "Teleporter";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 9 * BLOCK_SIZE));
        }
        else if(mousePos.getY() > 8 * BLOCK_SIZE)
        {
            tool = "Pellet";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 8 * BLOCK_SIZE));
        }
        else if(mousePos.getY() > 7 * BLOCK_SIZE)
        {
            tool = "Pinky";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 7 * BLOCK_SIZE));
        }
        else if(mousePos.getY() > 6 * BLOCK_SIZE)
        {
            tool = "Inky";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 6 * BLOCK_SIZE));
        }
        else if(mousePos.getY() > 5 * BLOCK_SIZE)
        {
            tool = "Clyde";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 5 * BLOCK_SIZE));
        }
        else if(mousePos.getY() > 4 * BLOCK_SIZE)
        {
            tool = "Blinky";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 4 * BLOCK_SIZE));
        }
        else if(mousePos.getY() > 3 * BLOCK_SIZE)
        {
            tool = "Block";
            selecter.setPosition(new Point2D(toolsXPos * BLOCK_SIZE, 3 * BLOCK_SIZE));
        }
    }

    private void handleButtonClick(Point2D mousePos)
    {
        /*
        Only the x values need to be checked because the initial
        check in handleClick() would have called a different method
        if the y value wasn't valid. In this way, any x less than
        the uiOffset doesn't need to be addressed because if the
        grid were clicked, this method wouldn't have been called.
        */

        double clearXMax = uiOffset() + 80; //the last x value that represents the clear button
        double saveXMin = getWidth() - 80; //the first x value that represents the save button
        if(mousePos.getX() > saveXMin) //if the x value of the mouse matches the range of the save button
        {
            writeLevelFile();
            textNotificationHandler.newNotif("Level saved as \n" +
                                                  "\"customLevel" + fileNum + ".txt\"" +
                                                  "\nin the working directory.");
        }
        else if(mousePos.getX() <= clearXMax) //if the x value of the mouse matches the range of the clear button
        {
            resetLevel();
            textNotificationHandler.newNotif("Level cleared.");
        }
    }

    private void resetLevel()
    {
        /*clear level by finding every GameEntity in the level array,
        then removing them from the world and setting them to null */
        for(int r = 0; r < level.length; r++)
        {
            for(int c = 0; c < level[r].length; c++)
            {
                if(level[r][c] != null)
                {
                    getGameWorld().removeEntity(level[r][c]);
                    level[r][c] = null;
                }
            }
        }

        //after clearing the level, re-add borders
        initBorders();
    }


    private void writeLevelFile()
    {
        try{
            /*
            This section of code ensures that the current level in the
            editor will be saved to a new file using the lowest available
            number. Saving a level will never overwrite another.
             */
            String path = "customLevel" + fileNum + ".txt";
            File f = new File(path);
            while(f.exists() && !f.isDirectory()) // does a file exist at the path with the current fileNum?
            {
                //if so, increment fileNum and retry
                fileNum++;
                path = "customLevel" + fileNum + ".txt";
                f = new File(path);
            }

            /*
            This section actually writes the file using the GameEntities in
            the level array. Each GameEntity has a type that stores the
            corresponding character code for the level parser in Pacman.
            If a space in the level is empty, a space is added instead.
             */
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            String levelText = "";

            for(int r = 0; r < level.length; r++) //go line by line
            {
                for(int c = 0; c < level[r].length; c++)
                {
                    if(level[r][c] == null) // add a space where there is no entity
                        levelText += " ";
                    else // add the correct char if there is an entity
                        levelText += ((TileType) level[r][c].getType()).getCode();
                }
                if(r < level.length - 1) //move to the next line (except on the last iteration)
                    levelText += "\n";
            }
            writer.print(levelText); //save the levelText to a new file at the determined path
            writer.close();
        } catch (IOException e) {
            // this shouldn't happen
        }
    }

    @Override
    protected void onUpdate(double tpf)
    {
        textNotificationHandler.onUpdate();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
