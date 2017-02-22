package net.d4rkfly3r.projects.virtualdesktop.parts;

import net.d4rkfly3r.projects.virtualdesktop.rendering.Framebuffer;

import java.util.ArrayList;
import java.util.List;

public abstract class WindowPart<T extends WindowPart> extends BasePart<T> {

    protected final List<BasePart> partList;
    protected String title;
    private Framebuffer framebuffer;

    protected WindowPart() {
        this.partList = new ArrayList<>();
    }

    public T addComponent(final ComponentPart iComponentPart) {
        this.partList.add(iComponentPart);
        return (T) this;
    }

    public T addModule(final ModulePart modulePart) {
        this.partList.add(modulePart);
        return (T) this;
    }

    @Override
    public void render() {
        this.partList.forEach(BasePart::render);
    }

    public String getTitle() {
        return this.title;
    }

    public T setTitle(final String title) {
        this.title = title;
        return (T) this;
    }


    @Override
    public T revalidate() {
        System.out.println("Creating... : " + this.getWidth() + " | " + this.getHeight());
        if (framebuffer != null) {
            framebuffer.dispose();
        }
        System.out.println(framebuffer);
        framebuffer = new Framebuffer(((int) getWidth()), ((int) getHeight()));
        return (T) this;
    }

    @Override
    public T setWidth(int width) {
        super.setWidth(width);
        return (T) this;
    }

    @Override
    public T setHeight(int height) {
        super.setHeight(height);
        return (T) this;
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public boolean isNotMinimized() {
        return windowed;
    }
}
