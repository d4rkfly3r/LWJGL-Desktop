package net.d4rkfly3r.projects.virtualdesktop;


import net.d4rkfly3r.projects.virtualdesktop.components.BasicWindow;
import net.d4rkfly3r.projects.virtualdesktop.components.StartMenu;
import net.d4rkfly3r.projects.virtualdesktop.components.TexturedButton;
import net.d4rkfly3r.projects.virtualdesktop.geometries.GeometrySquare;
import net.d4rkfly3r.projects.virtualdesktop.parts.BasePart;
import org.joml.Vector3d;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Joshua Freedman on 1/26/2017.
 * Project: VirtualDesktop
 */
public class Desktop {

    private final int toolbarHeight = 40;
    private final int displayHeight;
    private final int displayWidth;
    private final UseOrderList<BasePart> basePartList;
    private final StartMenu startMenu;
    private int backgroundImageTextureID;
    private TexturedButton startTexturedButton;
    private GeometrySquare toolbar;
    private boolean startOpen;

    public Desktop(final MainClass mainClass) {
        displayHeight = mainClass.getHeight();
        displayWidth = mainClass.getWidth();
        backgroundImageTextureID = Util.loadTexture("assets/background.png");
        final int startButtonTextureID = Util.loadTexture("assets/start.png");
        toolbar = new GeometrySquare(new Vector3d(0, displayHeight - toolbarHeight, 0), new Vector3d(displayWidth, displayHeight, 0), new Vector4f(88.000f / 255.000f, 0, 0, .85f));
        final GeometrySquare startButtonGeometry = new GeometrySquare(new Vector3d(0, displayHeight - toolbarHeight, 0), new Vector3d(toolbarHeight * 1.6f, displayHeight, 0), new Vector4f(0, 0, 0, .5f));
        startTexturedButton = new TexturedButton(startButtonGeometry, startButtonTextureID);
        basePartList = new UseOrderList<>();
        basePartList.use(new BasicWindow(this)
                .setTitle("FoxCore Window Test")
                .setPositionX(800)
                .setPositionY(150)
                .setHeight(500)
                .setWidth(350)
                .setPinned(true)
                .revalidate()
        );
        this.startMenu = new StartMenu(this, toolbarHeight);
    }

    public BasePart use(final BasePart item) {
        return basePartList.use(item);
    }

    public BasePart getPartUnder(int x, int y) {
        for (final BasePart basePart : this.basePartList.itemList) {
            if (x >= basePart.getPositionX() && x <= basePart.getPositionX() + basePart.getWidth() && y >= basePart.getPositionY() && y <= basePart.getPositionY() + basePart.getHeight()) {
                return basePart;
            }
        }
        return null;
    }


    public void renderBase() {
        glBindTexture(GL_TEXTURE_2D, backgroundImageTextureID);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(0, 0, 0);
        glTexCoord2f(1, 0);
        glVertex3f(displayWidth, 0, 0);
        glTexCoord2f(1, 1);
        glVertex3f(displayWidth, displayHeight, 0);
        glTexCoord2f(0, 1);
        glVertex3f(0, displayHeight, 0);
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);

        glTranslatef(0, 0, -displayWidth - displayHeight - 2);
        toolbar.render();
        startTexturedButton.render();
    }

    public void render() {
        basePartList.reverseStream().forEachOrdered(this::protectedRender);
        if (startOpen) {
            glPushMatrix();
            glTranslatef(startMenu.getPositionX(), startMenu.getPositionY(), 0);
            startMenu.render();
            glPopMatrix();
        }
    }

    private void protectedRender(final BasePart basePart) {
        glPushMatrix();
        glTranslated(basePart.getPositionX(), basePart.getPositionY(), 0);
//        startGlScissor(basePart.getPositionX(), basePart.getPositionY(), basePart.getWidth(), basePart.getHeight());
        basePart.render();
//        endGlScissor();
        glPopMatrix();
    }

    private void startGlScissor(int positionX, int positionY, int width, int height) {
        glEnable(GL_SCISSOR_TEST);
//        width -= 4;
//        positionX += 4;
//        height -= 4;
        System.out.println(displayHeight + " | " + positionY + " | " + height + " | " + positionX + " | " + width);
        glScissor(positionX, displayHeight - height - positionY, width, displayHeight - positionY);
    }

    private void endGlScissor() {
        glDisable(GL_SCISSOR_TEST);
    }


    public void renderOld() {

        glPushMatrix();
        glTranslatef(500, 500, -8.0f);                        // Move Left 1.5 Units And Into The Screen 6.0
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
    }

    public void removePart(final BasePart basePart) {
        basePartList.remove(basePart);
    }

    public boolean overrideMouseRelease(int lastMouseX, int lastMouseY, int button) {
        if (this.toolbar.pointLiesWithin(lastMouseX, lastMouseY, 0)) {
            System.out.println("Start Clicked!");
            startOpen = !startOpen;
            return true;
        }
        return false;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }
}
