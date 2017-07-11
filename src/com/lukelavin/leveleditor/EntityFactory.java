package com.lukelavin.leveleditor;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.lukelavin.leveleditor.Config.*;

/**
 * Created by lukel on 7/7/2017.
 */
public class EntityFactory
{
    private static final int BLOCK_SIZE = 40;
    private static final Point2D PACMAN_OFFSET = new Point2D(2, 2);

    public static GameEntity newBackground()
    {
        EntityView entityView = new EntityView(new Rectangle(1000, 1000, Color.BLACK));
        entityView.setRenderLayer(background);

        return Entities.builder()
                .at(0,0)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newGridLine(double x, double y, double width, double height)
    {
        EntityView entityView = new EntityView(new Rectangle(width, height, Color.GRAY));
        entityView.setRenderLayer(top);

        return Entities.builder()
                .at(x,y)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newEraser(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//" + "eraser.png")));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(new Point2D(x * BLOCK_SIZE, y * BLOCK_SIZE).add(PACMAN_OFFSET))
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newSelectionMarker(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//" + "select.png")));
        entityView.setRenderLayer(top);

        return Entities.builder()
                .at(x * BLOCK_SIZE, y * BLOCK_SIZE)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newPlayer(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//" + "pacman.png")));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(new Point2D(x * BLOCK_SIZE, y * BLOCK_SIZE).add(PACMAN_OFFSET))
                .type(TileType.PACMAN)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newBlinky(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//" + "blinky.png")));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE + 2, y * BLOCK_SIZE + 2)
                .type(TileType.BLINKY)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newPinky(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//" + "pinky.png")));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE + 2, y * BLOCK_SIZE + 2)
                .type(TileType.PINKY)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newInky(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//" + "inky.png")));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE + 2, y * BLOCK_SIZE + 2)
                .type(TileType.INKY)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newClyde(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//" + "clyde.png")));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE + 2, y * BLOCK_SIZE + 2)
                .type(TileType.CLYDE)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity makeBlock(double x, double y)
    {
        EntityView entityView = new EntityView(new Rectangle(BLOCK_SIZE, BLOCK_SIZE, Color.BLUE));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE, y * BLOCK_SIZE)
                .type(TileType.BLOCK)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newPellet(double x, double y)
    {
        EntityView entityView = new EntityView(new Circle(BLOCK_SIZE / 8, Color.YELLOW));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE + BLOCK_SIZE / 2 - BLOCK_SIZE / 8, y * BLOCK_SIZE + BLOCK_SIZE / 2 - BLOCK_SIZE / 8)
                .type(TileType.PELLET)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newTeleporter(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//" + "teleporter.png")));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE, y * BLOCK_SIZE)
                .type(TileType.TELEPORTER)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newPowerPellet(double x, double y)
    {
        EntityView entityView = new EntityView(new Circle(BLOCK_SIZE / 4, Color.YELLOW));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE + BLOCK_SIZE / 4, y * BLOCK_SIZE + BLOCK_SIZE / 4)
                .type(TileType.POWERPELLET)
                .viewFromNode(entityView)
                .build();
    }

    public static GameEntity newPointBoost(double x, double y)
    {
        EntityView entityView = new EntityView(new ImageView(new Image("assets//textures//cherry.png")));
        entityView.setRenderLayer(bottom);

        return Entities.builder()
                .at(x * BLOCK_SIZE, y * BLOCK_SIZE)
                .type(TileType.BOOST)
                .viewFromNode(entityView)
                .build();
    }
}
