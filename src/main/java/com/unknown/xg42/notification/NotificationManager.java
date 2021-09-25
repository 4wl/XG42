package com.unknown.xg42.notification;

import net.minecraft.client.Minecraft;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {
    private static LinkedBlockingQueue<Notification> pendingNotifications = new LinkedBlockingQueue<>();
    private static Notification currentNotification = null;

    public static void show(Notification notification) {
        pendingNotifications.add(notification);
    }

    public static void update() {
        if (currentNotification != null && !currentNotification.isShown()) {
            currentNotification = null;
        }

        if (currentNotification == null && !pendingNotifications.isEmpty()) {
            currentNotification = pendingNotifications.poll();
            currentNotification.show();
        }

    }

    public static void render() {
        int divider = Minecraft.getMinecraft().gameSettings.guiScale;
        int Dwidth = Minecraft.getMinecraft().displayWidth / divider;
        int Dheight = Minecraft.getMinecraft().displayHeight / divider;
        update();
        if (currentNotification != null)
            currentNotification.render(Dwidth, Dheight);
    }
}
