package net.d4rkfly3r.projects.virtualdesktop;


import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Joshua Freedman on 1/26/2017.
 * Project: VirtualDesktop
 */
public class Desktop {

    private float rotationTriangle;
    private float rotationQuad;
    private float scale = 1;

    public void render() {
        int xOff = MainClass.vidmode.width() / 2;
        int yOff = MainClass.vidmode.height() / 2;
        glTranslated(xOff, yOff, 0);
        glScalef(scale, scale, 1);
        glTranslated(-xOff, -yOff, 1);

        glPushMatrix();
        glTranslatef(500, 500, -8.0f);                        // Move Left 1.5 Units And Into The Screen 6.0
        glRotatef(rotationTriangle, 1.0f, 1.0f, 0.0f);                        // Rotate The Triangle On The Y axis ( NEW )
        glBegin(GL_TRIANGLES);                                // Start Drawing A Triangle
        glColor3f(100, 0.0f, 0.0f);                        // Red
        glVertex3f(0.0f, 100, 0.0f);                    // Top Of Triangle (Front)
        glColor3f(0.0f, 100, 0.0f);                        // Green
        glVertex3f(-100, -100, 100);                    // Left Of Triangle (Front)
        glColor3f(0.0f, 0.0f, 100);                        // Blue
        glVertex3f(100, -100, 100);                    // Right Of Triangle (Front)
        glColor3f(100, 0.0f, 0.0f);                        // Red
        glVertex3f(0.0f, 100, 0.0f);                    // Top Of Triangle (Right)
        glColor3f(0.0f, 0.0f, 100);                        // Blue
        glVertex3f(100, -100, 100);                    // Left Of Triangle (Right)
        glColor3f(0.0f, 100, 0.0f);                        // Green
        glVertex3f(100, -100, -100);                    // Right Of Triangle (Right)
        glColor3f(100, 0.0f, 0.0f);                        // Red
        glVertex3f(0.0f, 100, 0.0f);                    // Top Of Triangle (Back)
        glColor3f(0.0f, 100, 0.0f);                        // Green
        glVertex3f(100, -100, -100);                    // Left Of Triangle (Back)
        glColor3f(0.0f, 0.0f, 100);                        // Blue
        glVertex3f(-100, -100, -100);                    // Right Of Triangle (Back)
        glColor3f(100, 0.0f, 0.0f);                        // Red
        glVertex3f(0.0f, 100, 0.0f);                    // Top Of Triangle (Left)
        glColor3f(0.0f, 0.0f, 100);                        // Blue
        glVertex3f(-100, -100, -100);                    // Left Of Triangle (Left)
        glColor3f(0.0f, 100, 0.0f);                        // Green
        glVertex3f(-100, -100, 100);                    // Right Of Triangle (Left)
        glEnd();                                            // Done Drawing The Pyramid
        glPopMatrix();

        glPushMatrix();
        glTranslatef(700, 700, 100.0f);                        // Move Right 1.5 Units And Into The Screen 7.0
        glRotatef(rotationQuad, 1.0f, 1.0f, 1.0f);                    // Rotate The Quad On The X axis ( NEW )
        glBegin(GL_QUADS);                                    // Draw A Quad
        glColor3f(0.0f, 100f, 0.0f);                        // Set The Color To Blue
        glVertex3f(100f, 100f, -100f);                    // Top Right Of The Quad (Top)
        glVertex3f(-100f, 100f, -100f);                    // Top Left Of The Quad (Top)
        glVertex3f(-100f, 100f, 100f);                    // Bottom Left Of The Quad (Top)
        glVertex3f(100f, 100f, 100f);                    // Bottom Right Of The Quad (Top)
        glColor3f(100f, 0.5f, 0.0f);                        // Set The Color To Orange
        glVertex3f(100f, -100f, 100f);                    // Top Right Of The Quad (Bottom)
        glVertex3f(-100f, -100f, 100f);                    // Top Left Of The Quad (Bottom)
        glVertex3f(-100f, -100f, -100f);                    // Bottom Left Of The Quad (Bottom)
        glVertex3f(100f, -100f, -100f);                    // Bottom Right Of The Quad (Bottom)
        glColor3f(100f, 0.0f, 0.0f);                        // Set The Color To Red
        glVertex3f(100f, 100f, 100f);                    // Top Right Of The Quad (Front)
        glVertex3f(-100f, 100f, 100f);                    // Top Left Of The Quad (Front)
        glVertex3f(-100f, -100f, 100f);                    // Bottom Left Of The Quad (Front)
        glVertex3f(100f, -100f, 100f);                    // Bottom Right Of The Quad (Front)
        glColor3f(100f, 100f, 0.0f);                        // Set The Color To Yellow
        glVertex3f(100f, -100f, -100f);                    // Top Right Of The Quad (Back)
        glVertex3f(-100f, -100f, -100f);                    // Top Left Of The Quad (Back)
        glVertex3f(-100f, 100f, -100f);                    // Bottom Left Of The Quad (Back)
        glVertex3f(100f, 100f, -100f);                    // Bottom Right Of The Quad (Back)
        glColor3f(0.0f, 0.0f, 100f);                        // Set The Color To Blue
        glVertex3f(-100f, 100f, 100f);                    // Top Right Of The Quad (Left)
        glVertex3f(-100f, 100f, -100f);                    // Top Left Of The Quad (Left)
        glVertex3f(-100f, -100f, -100f);                    // Bottom Left Of The Quad (Left)
        glVertex3f(-100f, -100f, 100f);                    // Bottom Right Of The Quad (Left)
        glColor3f(100f, 0.0f, 100f);                        // Set The Color To Violet
        glVertex3f(100f, 100f, -100f);                    // Top Right Of The Quad (Right)
        glVertex3f(100f, 100f, 100f);                    // Top Left Of The Quad (Right)
        glVertex3f(100f, -100f, 100f);                    // Bottom Left Of The Quad (Right)
        glVertex3f(100f, -100f, -100f);                    // Bottom Right Of The Quad (Right)
        glEnd();                                            // Done Drawing The Quad
        glPopMatrix();

//        rotationTriangle += .2f;
//        rotationQuad -= .2f;
        scale += .0005f;
    }
}
