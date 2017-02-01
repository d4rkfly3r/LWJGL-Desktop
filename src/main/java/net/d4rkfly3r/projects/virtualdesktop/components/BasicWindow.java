package net.d4rkfly3r.projects.virtualdesktop.components;

import net.d4rkfly3r.projects.virtualdesktop.Desktop;
import net.d4rkfly3r.projects.virtualdesktop.geometries.GeometrySquare;
import net.d4rkfly3r.projects.virtualdesktop.parts.WindowPart;
import org.joml.Vector3d;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Joshua Freedman on 1/31/2017.
 * Project: VirtualDesktop
 */
public class BasicWindow extends WindowPart {

    private final Vector4f frameColor = new Vector4f(.15f, .15f, .15f, 1);
    private final Vector4f backgroundColor = new Vector4f(.25f, .25f, .25f, 1);
    private final int headButtonSize = 28;
    private int headSize = 30;
    private int frameWidth = 4;
    private GeometrySquare closeButton, minimizeButton;
    private int lastClickLocationX;
    private int lastClickLocationY;
    private boolean currentlyDraggable = false;
    private Desktop desktop;

    public BasicWindow(final Desktop desktop) {
        this.desktop = desktop;
        layoutHeadButtons();
    }

    public boolean isCurrentlyDraggable() {
        return currentlyDraggable;
    }

    public void setCurrentlyDraggable(boolean currentlyDraggable) {
        this.currentlyDraggable = currentlyDraggable;
    }

    public Desktop getDesktop() {
        return desktop;
    }

    public void setDesktop(Desktop desktop) {
        this.desktop = desktop;
    }

    private void layoutHeadButtons() {
        final int startY = (this.headSize - this.headButtonSize) / 2;
        this.closeButton = new GeometrySquare(
                new Vector3d(getWidth() - this.headButtonSize - this.frameWidth, startY, 0),
                new Vector3d(getWidth() - this.frameWidth, startY + this.headButtonSize, 0),
                new Vector4f(.8f, 0, 0, 1)
        );
        this.minimizeButton = new GeometrySquare(
                new Vector3d(getWidth() - this.headButtonSize - this.headButtonSize - 1 - this.frameWidth, startY, 0),
                new Vector3d(getWidth() - this.headButtonSize - 1 - this.frameWidth, startY + this.headButtonSize, 0),
                new Vector4f(0, 0, 0, .3575f)
        );

    }

    void renderFrame() {
        glColor3f(.15f, .15f, .15f);
        glBegin(GL_QUADS);
        glVertex3d(0, 0, 0.0D);
        glVertex3d(0, getHeight(), 0.0D);
        glVertex3d(getWidth(), getHeight(), 0.0D);
        glVertex3d(getWidth(), 0, 0.0D);
        glEnd();
    }

    void renderHead() {
        glColor3f(.15f, .15f, .15f);
        glBegin(GL_QUADS);
        glVertex3d(0, 0, 0.0D);
        glVertex3d(0, this.headSize, 0.0D);
        glVertex3d(getWidth(), this.headSize, 0.0D);
        glVertex3d(getWidth(), 0, 0.0D);
        glEnd();

        this.closeButton.render();
        this.minimizeButton.render();

    }

    void renderBody() {
        glColor3f(.25f, .25f, .25f);
        glBegin(GL_QUADS);
        glVertex3d(0, 0, 0.0D);
        glVertex3d(0, getHeight() - this.headSize - this.frameWidth, 0.0D);
        glVertex3d(getWidth() - this.frameWidth * 2, getHeight() - this.headSize - this.frameWidth, 0.0D);
        glVertex3d(getWidth() - this.frameWidth * 2, 0, 0.0D);
        glEnd();

    }

    @Override
    public void render() {
        renderFrame();
        renderHead();
        glTranslated(this.frameWidth, this.headSize, 0);
        renderBody();
        super.render();
    }


    public Vector4f getFrameColor() {
        return this.frameColor;
    }

    public Vector4f getBackgroundColor() {
        return this.backgroundColor;
    }

    public int getHeadSize() {
        return this.headSize;
    }

    public BasicWindow setHeadSize(int headSize) {
        this.headSize = headSize;
        layoutHeadButtons();
        return this;
    }

    public int getFrameWidth() {
        return this.frameWidth;
    }

    public BasicWindow setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
        return this;
    }

    @Override
    public net.d4rkfly3r.projects.virtualdesktop.parts.BasePart setWidth(int width) {
        layoutHeadButtons();
        return super.setWidth(width);
    }

    @Override
    public void mouseClicked(int x, int y, int buttonCode) {
        this.lastClickLocationX = x;
        this.lastClickLocationY = y;
        if (buttonCode == 0) {
            final boolean inHead = x >= 0 && x <= this.width && y >= 0 && y <= this.headSize;
            this.currentlyDraggable = inHead && !(this.closeButton.pointLiesWithin(x, y, 0) || this.minimizeButton.pointLiesWithin(x, y, 0));
        }
    }

    @Override
    public void mouseReleased(int x, int y, int buttonCode) {
        if (buttonCode == 0) {
            if (!this.currentlyDraggable) {
                if (this.closeButton.pointLiesWithin(x, y, 0)) {
                    this.close();
                } else if (this.minimizeButton.pointLiesWithin(x, y, 0)) {
                    this.setPinned(!this.isPinned());
                }
                y -= this.headSize;
            }
            this.currentlyDraggable = false;
        }
        this.lastClickLocationX = -1;
        this.lastClickLocationY = -1;
    }

    @Override
    public void mouseDrag(int x, int y, int buttonCode) {
        if (buttonCode == 0) {
            if (this.currentlyDraggable) {
                this.positionX += x - this.lastClickLocationX;
                this.positionY += y - this.lastClickLocationY;
            }
        }
    }

    @Override
    public net.d4rkfly3r.projects.virtualdesktop.parts.BasePart revalidate() {
        layoutHeadButtons();
        return this;
    }

    private void close() {
        System.out.println("Closing window: " + this.toString());
        desktop.removePart(this);
    }


}
