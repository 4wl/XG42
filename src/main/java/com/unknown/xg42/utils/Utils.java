package com.unknown.xg42.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Utils {
    public static boolean nullCheck() {
        return (Wrapper.getPlayer() == null || Wrapper.getWorld() == null);
    }

    public static ByteBuffer readImageToBuffer(InputStream p_readImageToBuffer_1_) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(p_readImageToBuffer_1_);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[])null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        int[] var5 = aint;
        int var6 = aint.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            int i = var5[var7];
            bytebuffer.putInt(i << 8 | i >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
    }
}
