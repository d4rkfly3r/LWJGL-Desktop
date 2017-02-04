package net.d4rkfly3r.projects.virtualdesktop;


import net.d4rkfly3r.projects.virtualdesktop.components.BasicWindow;
import net.d4rkfly3r.projects.virtualdesktop.geometries.GeometrySquare;
import net.d4rkfly3r.projects.virtualdesktop.parts.WindowPart;
import org.joml.Vector3d;
import org.joml.Vector4f;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Joshua Freedman on 1/26/2017.
 * Project: VirtualDesktop
 */
public class Desktop {

    private final double heightModifier = 100;
    final double tabViewWidth = heightModifier * (16.0 / 9.0);
    private final double toolbarHeight = 5;
    private final double displayHeight;
    private final double displayWidth;
    private final UseOrderList<WindowPart> windowPartList;
    private int backgroundImageTextureID;
    private GeometrySquare toolbar;
    private boolean toolbarOpen = false;
    private HashMap<WindowPart, GeometrySquare> minimizedGeometries;

    public Desktop(final MainClass mainClass) {
        displayHeight = mainClass.getHeight();
        displayWidth = mainClass.getWidth();
        windowPartList = new UseOrderList<>();
        minimizedGeometries = new HashMap<>();
        backgroundImageTextureID = Util.loadTexture("assets/background.png");

        toolbar = new GeometrySquare(new Vector3d(0, displayHeight - toolbarHeight, 0), new Vector3d(displayWidth, displayHeight, 0), new Vector4f(88.000f / 255.000f, 0, 0, .85f));
        toolbar.setPostRender(() -> {
            minimizedGeometries.values().forEach(GeometrySquare::render);
//            for (int i = 0; i < windowPartList.getItems().size(); i++) {
//                final double tabViewWidth = heightModifier * (16.0 / 9.0);
//                final double xOff = tabViewWidth * i + 2 * (i + 1);
//                final double yOff = toolbar.start.y() + 2;
//                glColor4f(10.000f / 255.000f, 0, 0, 1);
//                glBegin(GL_QUADS);
//                 FIXME: Don't think that 'Z' works correctly!
//                glVertex3d(xOff, yOff, 0);
//                glVertex3d(xOff + tabViewWidth, yOff, 0);
//                glVertex3d(xOff + tabViewWidth, displayHeight, 0);
//                glVertex3d(xOff, displayHeight, 0);
//                glEnd();
//            }
        });

        add(new BasicWindow(this)
                .setTitle("FoxCore Window Test")
                .setPositionX(800)
                .setPositionY(150)
                .setHeight(500)
                .setWidth(350)
                .setMinimized(true)
                .revalidate()
        );
    }

    public WindowPart add(final WindowPart item) {
        final double xOff = tabViewWidth * minimizedGeometries.size() + 2 * (minimizedGeometries.size() + 1);
        final double yOff = toolbar.start.y() + 2;

        minimizedGeometries.put(item, new GeometrySquare(new Vector3d(xOff, yOff, 0), new Vector3d(xOff + tabViewWidth, displayHeight, 0), new Vector4f(10.000f / 255.000f, 0, 0, 1)));
        return windowPartList.use(item);
    }

    public WindowPart getPartUnder(double x, double y) {
        for (final WindowPart windowPart : this.windowPartList.itemList) {
            if (x >= windowPart.getPositionX() && x <= windowPart.getPositionX() + windowPart.getWidth() && y >= windowPart.getPositionY() && y <= windowPart.getPositionY() + windowPart.getHeight()) {
                return windowPart;
            }
        }
        return null;
    }


    public void renderBase() {
        glBindTexture(GL_TEXTURE_2D, backgroundImageTextureID);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3d(0, 0, 0);
        glTexCoord2f(1, 0);
        glVertex3d(displayWidth, 0, 0);
        glTexCoord2f(1, 1);
        glVertex3d(displayWidth, displayHeight, 0);
        glTexCoord2f(0, 1);
        glVertex3d(0, displayHeight, 0);
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);

        glTranslated(0, 0, -displayWidth - displayHeight - 2);
        toolbar.render();
    }

    public void render() {
//        windowPartList.reverseStream().filter(WindowPart::isNotMinimized).forEachOrdered(this::protectedRender);
        windowPartList.reverseStream().forEachOrdered(this::protectedRender);
    }

    private void protectedRender(final WindowPart windowPart) {
        glPushMatrix();
        glTranslated(windowPart.getPositionX(), windowPart.getPositionY(), 0);

//        glPushAttrib(GL_CURRENT_BIT | GL_VIEWPORT_BIT);
//        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, windowPart.getFramebufferID());
//        final double windowPartWidth = windowPart.getWidth();
//        final double windowPartHeight = windowPart.getHeight();
//        System.out.println(windowPartWidth + " | " + windowPartHeight);
//        glViewport(0, 0, ((int) windowPartWidth), ((int) windowPartHeight));

//        glClearColor(0, 0, 0, 0); //transparent black
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
//        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);

//        startGlScissor(windowPart.getPositionX(), windowPart.getPositionY(), windowPart.getWidth(), windowPart.getHeight());

//        glPushAttrib(GL_CURRENT_BIT);
//        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, MainClass.framebufferTexID);
//        glClearColor(0, 0, 0, 0); //transparent black
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
//        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
//

        glPushMatrix();
        windowPart.render();
        glPopMatrix();

//        glColor4f(1, 0, 0, 1);
//        glBegin(GL_QUADS);
//         FIXME: Don't think that 'Z' works correctly!
//        glVertex3d(5, 5, 0);
//        glVertex3d(3000 / 4, 5, 0);
//        glVertex3d(3000 / 4, 3500 / 3, 0);
//        glVertex3d(5, 3500 / 3, 0);
//        glEnd();

//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
//        glPopAttrib();

//        glColor4f(1, 1, 1, 1);
//        System.err.println(windowPart.getFramebufferTexID());
//        glBindTexture(GL_TEXTURE_2D, windowPart.getFramebufferTexID());
//        {
//            glBegin(GL_QUADS);
//            glTexCoord2f(0, 1);
//            glVertex3d(0, 0, 0);
//            glTexCoord2f(1, 1);
//            glVertex3d(windowPartWidth, 0, 0);
//            glTexCoord2f(1, 0);
//            glVertex3d(windowPartWidth, windowPartHeight, 0);
//            glTexCoord2f(0, 0);
//            glVertex3d(0, windowPartHeight, 0);
//            glEnd();
//        }
//        glBindTexture(GL_TEXTURE_2D, 0);

//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
//        glPopAttrib();
//
//
//        glColor4f(1, 1, 1, 1);
//        glBindTexture(GL_TEXTURE_2D, MainClass.framebufferTexID);
//        {
//            glBegin(GL_QUADS);
//            glTexCoord2f(0, 1);
//            glVertex3d(0, 0, 0);
//            glTexCoord2f(1, 1);
//            glVertex3d(windowPart.getWidth(), 0, 0);
//            glTexCoord2f(1, 0);
//            glVertex3d(windowPart.getWidth(), windowPart.getHeight(), 0);
//            glTexCoord2f(0, 0);
//            glVertex3d(0, windowPart.getHeight(), 0);
//            glEnd();
//        }
//        glBindTexture(GL_TEXTURE_2D, 0);

//        endGlScissor();
        glPopMatrix();
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

    public void removePart(final WindowPart basePart) {
        final GeometrySquare oldGeometry = minimizedGeometries.get(basePart);
        minimizedGeometries.remove(basePart);
        minimizedGeometries.forEach((windowPart, geometrySquare) -> {
            if (oldGeometry.start.x <= geometrySquare.start.x) {
                geometrySquare.start.sub(tabViewWidth + 2, 0, 0);
                geometrySquare.end.sub(tabViewWidth + 2, 0, 0);
            }
        });
        windowPartList.remove(basePart);
    }

    public boolean overrideMouseRelease(double lastMouseX, double lastMouseY, double button) {
//        if (this.toolbar.pointLiesWithin(lastMouseX, lastMouseY, 0)) {
//            System.out.println("Start Clicked!");
//            startOpen = !startOpen;
//            return true;
//        }
        return false;
    }

    public double getDisplayHeight() {
        return displayHeight;
    }

    public double getDisplayWidth() {
        return displayWidth;
    }

    public boolean overrideMousePress(double lastMouseX, double lastMouseY, int button) {
        return false;
    }

    public void mouseMove(double xPos, double yPos) {
        if (this.toolbar.pointLiesWithin(xPos, yPos, 0)) {
            if (!this.toolbarOpen) {
                toolbarOpen = true;
                this.toolbar.start.sub(0, heightModifier, 0);
                this.minimizedGeometries.values().forEach(geometrySquare -> {
                    geometrySquare.start.sub(0, heightModifier, 0);
                });
            }
        } else if (this.toolbarOpen) {
            toolbarOpen = false;
            this.toolbar.start.add(0, heightModifier, 0);
            this.minimizedGeometries.values().forEach(geometrySquare -> {
                geometrySquare.start.add(0, heightModifier, 0);
            });
        }
    }

    public void use(final WindowPart windowPart) {
        windowPartList.use(windowPart);
    }
}
