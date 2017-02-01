package net.d4rkfly3r.projects.virtualdesktop.parts;

import static org.lwjgl.opengl.GL11.*;

public abstract class BasePart {
    protected int height;
    protected int positionX;
    protected int positionY;
    protected int width;
    protected boolean pinned;

    public abstract void render();

    public int getPositionX() {
        return this.positionX;
    }

    public BasePart setPositionX(int positionX) {
        this.positionX = positionX;
        return this;
    }

    public int getPositionY() {
        return this.positionY;
    }

    public BasePart setPositionY(int positionY) {
        this.positionY = positionY;
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    public BasePart setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return this.height;
    }

    public BasePart setHeight(int height) {
        this.height = height;
        return this;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    public BasePart setPinned(boolean pinned) {
        this.pinned = pinned;
        return this;
    }

    public abstract void mouseClicked(int x, int y, int buttonCode);

    public abstract void mouseReleased(int x, int y, int buttonCode);

    public abstract void mouseDrag(int x, int y, int buttonCode);

    public abstract BasePart revalidate();
}
