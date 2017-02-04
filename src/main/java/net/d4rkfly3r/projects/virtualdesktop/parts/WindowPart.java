package net.d4rkfly3r.projects.virtualdesktop.parts;

import net.d4rkfly3r.projects.virtualdesktop.Util;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public abstract class WindowPart extends BasePart<WindowPart> {

    protected final List<BasePart> partList;
    protected String title;
    private int framebufferID;
    private int framebufferTexID;

    protected WindowPart() {
        this.partList = new ArrayList<>();
    }

    public WindowPart addComponent(final ComponentPart iComponentPart) {
        this.partList.add(iComponentPart);
        return this;
    }

    public WindowPart addModule(final ModulePart modulePart) {
        this.partList.add(modulePart);
        return this;
    }

    @Override
    public void render() {
        this.partList.forEach(BasePart::render);
    }

    public String getTitle() {
        return this.title;
    }

    public WindowPart setTitle(final String title) {
        this.title = title;
        return this;
    }


    @Override
    public WindowPart revalidate() {
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
        return this;
    }

    @Override
    public WindowPart setWidth(int width) {
        super.setWidth(width);
        return this;
    }

    @Override
    public WindowPart setHeight(int height) {
        super.setHeight(height);
        return this;
    }

    public int getFramebufferID() {
        return framebufferID;
    }

    public int getFramebufferTexID() {
        return framebufferTexID;
    }

    public boolean isNotMinimized() {
        return minimized;
    }
}
