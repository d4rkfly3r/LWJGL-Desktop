package net.d4rkfly3r.projects.virtualdesktop.rendering;

import net.d4rkfly3r.projects.virtualdesktop.MainClass;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Fox on 2/4/2017.
 */
public class Framebuffer implements ITexture {

    /**
     * The ID of the FBO in use
     */
    protected int id;
    protected Texture texture;
    protected boolean ownsTexture;
    Framebuffer(Texture texture, boolean ownsTexture) {
        this.texture = texture;
        this.ownsTexture = ownsTexture;
        if (!isSupported()) {
            throw new RuntimeException("FBO extension not supported in hardware");
        }
        texture.bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        id = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER, id);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                texture.getTarget(), texture.getID(), 0);
        int result = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER);
        if (result != GL_FRAMEBUFFER_COMPLETE) {
            glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
            glDeleteFramebuffers(id);
            throw new RuntimeException("Exception " + result + " when checking FBO status");
        }
        glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
    }

    /**
     * Advanced constructor which creates a frame buffer from a texture; the framebuffer
     * does not "own" the texture and thus calling dispose() on this framebuffer will not
     * destroy the texture.
     *
     * @param texture the texture to use
     */
    public Framebuffer(Texture texture) {
        this(texture, false);
    }

    /**
     * @param width
     * @param height
     * @param filter
     * @param wrap
     */
    public Framebuffer(int width, int height, int filter, int wrap) {
        this(new Texture(width, height, filter, wrap), true);
    }

    public Framebuffer(int width, int height, int filter) {
        this(width, height, filter, GL_NEAREST);
    }

    public Framebuffer(int width, int height) {
        this(width, height, GL_NEAREST, GL_REPEAT);
    }

    public static boolean isSupported() {
        return GL.getCapabilities().GL_EXT_framebuffer_object;
    }

    public int getID() {
        return id;
    }

    public int getWidth() {
        return texture.getWidth();
    }

    public int getHeight() {
        return texture.getHeight();
    }

    public Texture getTexture() {
        return texture;
    }

    /**
     * Binds the FBO and sets glViewport to the texture region width/height.
     */
    public void begin() {
        if (id == 0)
            throw new IllegalStateException("can't use FBO as it has been destroyed..");
        glViewport(0, 0, getWidth(), getHeight());
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
        //glReadBuffer(GL_COLOR_ATTACHMENT0);
    }

    /**
     * Unbinds the FBO and resets glViewport to the display size.
     */
    public void end() {
        if (id == 0)
            return;
        glViewport(0, 0, MainClass.getDisplayWidth(), MainClass.getDisplayHeight());
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    /**
     * Disposes this FBO without destroying the texture.
     */
    public void dispose() {
        if (id == 0)
            return;
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glDeleteFramebuffersEXT(id);
        if (ownsTexture)
            texture.dispose();
        id = 0;
        //glReadBuffer(GL_BACK);
    }

    @Override
    public float getU() {
        return 0;
    }

    @Override
    public float getV() {
        return 1f;
    }

    @Override
    public float getU2() {
        return 1f;
    }

    @Override
    public float getV2() {
        return 0;
    }

    @Override
    public String toString() {
        return "Framebuffer{" +
                "id=" + id +
                ", texture=" + texture +
                ", ownsTexture=" + ownsTexture +
                '}';
    }
}
