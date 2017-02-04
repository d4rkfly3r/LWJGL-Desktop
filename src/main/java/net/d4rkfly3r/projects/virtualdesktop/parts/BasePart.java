package net.d4rkfly3r.projects.virtualdesktop.parts;

public abstract class BasePart<T extends BasePart> {
    protected double height;
    protected double positionX;
    protected double positionY;
    protected double width;
    protected boolean windowed;

    public abstract void render();

    public double getPositionX() {
        return this.positionX;
    }

    public T setPositionX(int positionX) {
        this.positionX = positionX;
        return (T) this;
    }

    public double getPositionY() {
        return this.positionY;
    }

    public T setPositionY(int positionY) {
        this.positionY = positionY;
        return (T) this;
    }

    public double getWidth() {
        return this.width;
    }

    public T setWidth(int width) {
        this.width = width;
        return (T) this;
    }

    public double getHeight() {
        return this.height;
    }

    public T setHeight(int height) {
        this.height = height;
        return (T) this;
    }

    public boolean isWindowed() {
        return this.windowed;
    }

    public T setWindowed(boolean minimized) {
        this.windowed = minimized;
        return (T) this;
    }

    public abstract void mouseClicked(double x, double y, int buttonCode);

    public abstract void mouseReleased(double x, double y, int buttonCode);

    public abstract void mouseDrag(double x, double y, int buttonCode);

    public abstract T revalidate();
}
