package net.d4rkfly3r.projects.virtualdesktop.rendering;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.glGenerateMipmapEXT;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Fox on 2/4/2017.
 */
public class Texture implements ITexture {

    protected int id;
    protected int width;
    protected int height;

    protected Texture() {
        //does nothing... for subclasses
    }

    /**
     * Creates an empty OpenGL texture with the given width and height, where
     * each pixel is transparent black (0, 0, 0, 0) and the wrap mode is
     * CLAMP_TO_EDGE and the filter is NEAREST.
     *
     * @param width  the width of the texture
     * @param height the height of the texture
     */
    public Texture(int width, int height) {
        this(width, height, GL_NEAREST);
    }

    /**
     * Creates an empty OpenGL texture with the given width and height, where
     * each pixel is transparent black (0, 0, 0, 0) and the wrap mode is
     * CLAMP_TO_EDGE.
     *
     * @param width  the width of the texture
     * @param height the height of the texture
     * @param filter the filter to use
     */
    public Texture(int width, int height, int filter) {
        this(width, height, filter, GL_REPEAT);
    }

    public Texture(int width, int height, int filter, int wrap) {
        glEnable(getTarget());
        id = glGenTextures();
        this.width = width;
        this.height = height;
        bind();

        setFilter(filter);
        setWrap(wrap);

        ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
        upload(GL_RGBA, buf);
    }

    public Texture(URL pngRef) throws IOException {
        this(pngRef, GL_NEAREST);
    }

    public Texture(URL pngRef, int filter) throws IOException {
        this(pngRef, filter, GL_REPEAT);
    }

    public Texture(URL pngRef, int filter, int wrap) throws IOException {
        this(pngRef, filter, filter, wrap, false);
    }

    public Texture(URL pngRef, int filter, boolean genMipmap) throws IOException {
        this(pngRef, filter, filter, GL_REPEAT, genMipmap);
    }

    public Texture(URL pngRef, int minFilter, int magFilter, int wrap,
                   boolean genMipmap) throws IOException {
        //TODO: npot check
        InputStream input = null;
        try {
            input = pngRef.openStream();
            PNGDecoder dec = new PNGDecoder(input);

            width = dec.getWidth();
            height = dec.getHeight();
            ByteBuffer buf = BufferUtils.createByteBuffer(4 * width * height);
            dec.decode(buf, width * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            glEnable(getTarget());
            id = glGenTextures();

            bind();
            setFilter(minFilter, magFilter);
            setWrap(wrap);
            upload(GL_RGBA, buf);

            //use EXT since we are targeting 2.0+
            if (genMipmap) {
                glGenerateMipmapEXT(getTarget());
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public int getTarget() {
        return GL_TEXTURE_2D;
    }

    public int getID() {
        return id;
    }

    protected void setUnpackAlignment() {
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glPixelStorei(GL_PACK_ALIGNMENT, 1);
    }

    /**
     * Uploads image data with the dimensions of this Texture.
     *
     * @param dataFormat the format, e.g. GL_RGBA
     * @param data       the byte data
     */
    public void upload(int dataFormat, ByteBuffer data) {
        bind();
        setUnpackAlignment();
        glTexImage2D(getTarget(), 0, GL_RGBA, width, height, 0, dataFormat, GL_UNSIGNED_BYTE, data);
    }

    /**
     * Uploads a sub-image within this texture.
     *
     * @param x          the destination x offset
     * @param y          the destination y offset, with lower-left origin
     * @param width      the width of the sub image data
     * @param height     the height of the sub image data
     * @param dataFormat the format of the sub image data, e.g. GL_RGBA
     * @param data       the sub image data
     */
    public void upload(int x, int y, int width, int height, int dataFormat, ByteBuffer data) {
        bind();
        setUnpackAlignment();
        glTexSubImage2D(getTarget(), 0, x, y, width, height, dataFormat, GL_UNSIGNED_BYTE, data);
    }

    public void setFilter(int filter) {
        setFilter(filter, filter);
    }

    public void setFilter(int minFilter, int magFilter) {
        bind();
        glTexParameteri(getTarget(), GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(getTarget(), GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void setWrap(int wrap) {
        bind();
        glTexParameteri(getTarget(), GL_TEXTURE_WRAP_S, wrap);
        glTexParameteri(getTarget(), GL_TEXTURE_WRAP_T, wrap);
    }

    public void bind() {
        if (!valid())
            throw new IllegalStateException("trying to bind a texture that was disposed");
        glBindTexture(getTarget(), id);
    }

    public void dispose() {
        if (valid()) {
            glDeleteTextures(id);
            id = 0;
        }
    }

    /**
     * Returns true if this texture is valid, aka it has not been disposed.
     *
     * @return true if id!=0
     */
    public boolean valid() {
        return id != 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Returns this object; used for abstraction with SpriteBatch.
     *
     * @return this texture object
     */
    public Texture getTexture() {
        return this;
    }

    @Override
    public float getU() {
        return 0f;
    }

    @Override
    public float getV() {
        return 0f;
    }

    @Override
    public float getU2() {
        return 1f;
    }

    @Override
    public float getV2() {
        return 1f;
    }

    @Override
    public String toString() {
        return "Texture{" +
                "id=" + id +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}