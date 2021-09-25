package com.unknown.xg42.module.modules;

import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import com.unknown.xg42.notification.NotificationManager;
import com.unknown.xg42.notification.NotificationType;
import com.unknown.xg42.notification.notifications.TopNotification;
import org.lwjgl.input.Keyboard;

@Module.Info(name = "Test", keyCode = Keyboard.KEY_ADD, category = Category.INFO)
public class QWQ extends Module{
    @Override
    public void onEnable() {
        NotificationManager.show(new TopNotification(NotificationType.INFO, "INFO", "JUST A TEST", 15));
        NotificationManager.show(new TopNotification(NotificationType.WARNING, "WARNING", "JUST A TEST", 15));
        NotificationManager.show(new TopNotification(NotificationType.ERROR, "ERROR", "JUST A TEST", 15));
        NotificationManager.show(new TopNotification(NotificationType.RAINBOW, "RAINBOW", "JUST A TEST", 15));
        disable();
    }
}
