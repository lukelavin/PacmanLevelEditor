package com.lukelavin.leveleditor;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.time.LocalTimer;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.lukelavin.leveleditor.Config.font;
import static com.lukelavin.leveleditor.Config.uiOffset;

public class TextNotificationHandler
{
    Main app;
    private LocalTimer notifTimer;
    Label notif;

    public TextNotificationHandler()
    {
        app = (Main) FXGL.getApp();
    }

    public void onUpdate()
    {
        //clear notifications after 2 seconds
        if(notifTimer != null && notifTimer.elapsed(Duration.seconds(2)))
        {
            app.getGameScene().removeUINode(notif);
            notifTimer = null;
        }
    }

    public void newNotif(String text)
    {
        if(notif != null) // if there's already a notifcation that hasn't expired
        {
            //properly remove the notification text from the game scene before reusing the variable
            app.getGameScene().removeUINode(notif);
            notif = null;
        }

        //new label using the passed-in string as text and the default font from config
        notif = new Label(text);
        notif.setFont(font);
        notif.setTextFill(Color.WHITE);

        //centered above the "Clear" and "Save" buttons
        notif.setTranslateX(uiOffset() + 15);
        notif.setTranslateY(app.getHeight() - 135);
        notif.setTranslateZ(1);

        app.getGameScene().addUINode(notif);

        notifTimer = FXGL.newLocalTimer();
        notifTimer.capture();
    }
}
