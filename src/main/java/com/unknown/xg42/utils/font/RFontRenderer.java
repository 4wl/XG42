package com.unknown.xg42.utils.font;

import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RFontRenderer extends CFont {
    protected CharData[] boldChars = new CharData[256];
    protected CharData[] italicChars = new CharData[256];
    protected CharData[] boldItalicChars = new CharData[256];
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;
    private final int[] colorCode = new int[32];

    public RFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public RFontRenderer(CustomFont font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }


    public float drawStringWithShadow(String text, double x, double y, float speed, float saturation, float brightness, long add, int alpha) {
        float shadowWidth = drawString(text, x + 1D, y + 1D, speed, saturation, brightness, add, alpha, true);
        return Math.max(shadowWidth, drawString(text, x, y, speed, saturation, brightness, add, alpha, false));
    }

    public float drawString(String text, float x, float y, float speed, float saturation, float brightness, long add, int alpha) {
        return drawString(text, x, y, speed, saturation, brightness, add, alpha, false);
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, float speed, float saturation, float brightness, long add, int alpha) {
        return drawStringWithShadow(text, x - getStringWidth(text) / 2f, y, speed, saturation, brightness, add, alpha);
    }

    public float drawCenteredString(String text, float x, float y, float speed, float saturation, float brightness, long add, int alpha) {
        return drawString(text, x - getStringWidth(text) / 2f, y, speed, saturation, brightness, add, alpha);
    }

    public float drawString(String text, double x, double y, float speed, float saturation, float brightness, long add, int alpha, boolean shadow) {
        x--;
        if (text == null) {
            return 0.0f;
        }

        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;

        x *= 2.0;
        y = (y - 3.0) * 2.0;

        if (render) {

            GL11.glPushMatrix();
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
            int size = text.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(tex.getGlTextureId());
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGlTextureId());
            for (int i = 0; i < size; ++i) {
                char character = text.charAt(i);

                Color rainbow = Rainbow.getRainbowColor(speed, saturation, brightness, i * -add);
                int color = rainbow.getRGB();
                if (color == 0x20FFFFFF) {
                    color = 0xFFFFFF;
                }
                if ((color & 0xFC000000) == 0) {
                    color |= 0xFF000000;
                }
                if (shadow) {
                    color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
                }

                RenderUtils.setColor(new Color(color));

                GL11.glBegin(GL11.GL_LINE_BIT);
                drawChar(currentData, character, (float)x, (float)y);
                GL11.glEnd();

                if (strikethrough) {
                    this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0, y + (double)(currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
                }
                x += (currentData[character].width - 8 + this.charOffset);
            }

            GlStateManager.disableBlend();
            GL11.glScalef(2.0f, 2.0f, 2.0f);

            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
            GL11.glPopMatrix();
        }

        return (float)x / 2.0f;
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7' && i < size) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= currentData.length || character < '\u0000') continue;
            width += currentData[character].width - 8 + this.charOffset;
        }
        return width / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(Font.BOLD), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(Font.BOLD | Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_CURRENT_BIT);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if ((double)this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            for (String word : words) {
                for (int i = 0; i < word.toCharArray().length; ++i) {
                    char c = word.toCharArray()[i];
                    if (c != '\u00a7' || i >= word.toCharArray().length - 1) continue;
                    lastColorCode = word.toCharArray()[i + 1];
                }
                StringBuilder stringBuilder = new StringBuilder();
                if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width) {
                    currentWord = currentWord + word + " ";
                    continue;
                }
                finalWords.add(currentWord);
                currentWord = "\u00a7" + lastColorCode + word + " ";
            }
            if (currentWord.length() > 0) {
                if ((double)this.getStringWidth(currentWord) < width) {
                    finalWords.add("\u00a7" + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        char lastColorCode = '\uffff';
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (c == '\u00a7' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
                currentWord = currentWord + c;
                continue;
            }
            finalWords.add(currentWord);
            currentWord = "\u00a7" + lastColorCode + c;
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index >> 0 & 1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }
}

