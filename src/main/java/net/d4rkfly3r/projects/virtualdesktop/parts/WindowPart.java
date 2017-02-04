package net.d4rkfly3r.projects.virtualdesktop.parts;

import net.d4rkfly3r.projects.virtualdesktop.Util;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public abstract class WindowPart<T extends WindowPart> extends BasePart<T> {

    protected final List<BasePart> partList;
    protected String title;
    private int framebufferID;
    private int framebufferTexID;

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
        if (framebufferID >= 0) {
            GL30.glDeleteFramebuffers(framebufferID);
        }
        framebufferID = Util.createFBO(((int) this.getWidth()), ((int) this.getHeight()));
        if (framebufferTexID >= 0) {
            GL11.glDeleteTextures(framebufferTexID);
        }
        framebufferTexID = Util.createFBOTexture(((int) this.getWidth()), ((int) this.getHeight()));
        glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
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

    public int getFramebufferID() {
        return framebufferID;
    }

    public int getFramebufferTexID() {
        return framebufferTexID;
    }

    public boolean isNotMinimized() {
        return windowed;
    }
}
