package com.unknown.xg42.utils.particle;

import com.unknown.xg42.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem {

    private static final float SPEED = 0.1f;
    private List<Particle> particleList = new ArrayList<>();
    private int dist;

    public ParticleSystem(int initAmount, int dist) {
        addParticles(initAmount);
        this.dist = dist;

    }

    public void addParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            particleList.add(Particle.generateParticle());
        }
    }


    public void tick(int delta) {
        for (Particle particle : particleList) {
            particle.tick(delta, SPEED);
        }
    }

    public void render() {
        for (Particle particle : particleList) {
            {
                RenderUtils.drawArc((float) particle.getX(),(float) particle.getY(), particle.getSize() / 1.5f, 0, 360, 16);
            }

            {
                double nearestDistance = 0;
                Particle nearestParticle = null;

                for (Particle particle1 : particleList) {
                    double distance = particle.getDistanceTo(particle1);

                    if (distance <= dist && (nearestDistance <= 0 || distance <= nearestDistance)) {
                        nearestDistance = distance;
                        nearestParticle = particle1;
                    }
                }

                if (nearestParticle != null) {
                    double alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - nearestDistance / dist));
                    drawLine(particle.getX(),
                            particle.getY(),
                            nearestParticle.getX(),
                            nearestParticle.getY(),
                            1,
                            1,
                            1,
                            alpha);
                }
            }
        }
    }

    private void drawLine(double x, double y, double x1, double y1, float r, float g, float b, double alpha) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(r, g, b, (float) alpha);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        {
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x1, y1);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

}
