package net.d4rkfly3r.projects.virtualdesktop;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Util {
    public static int loadTexture(final String path) {
        try (final FileInputStream fileInputStream = new FileInputStream(path)) {
            final PNGDecoder decoder = new PNGDecoder(fileInputStream);
            final int imageWidth = decoder.getWidth();
            final int imageHeight = decoder.getHeight();
            final ByteBuffer imageByteBuffer = ByteBuffer.allocateDirect(4 * imageWidth * imageHeight);
            decoder.decode(imageByteBuffer, imageWidth * 4, PNGDecoder.Format.RGBA);
            imageByteBuffer.flip();
            final int textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID
            //Setup wrap mode
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            //Setup texture scaling filtering
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, imageWidth, imageHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageByteBuffer);
            return textureID;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int createFBO(final int width, final int height) {
        final int framebufferObjID = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferObjID);
        final int depthbufferID = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthbufferID);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthbufferID);
        return framebufferObjID;
    }

    public static int createFBOTexture(final int width, final int height) {
        final int framebufferTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, framebufferTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, framebufferTexture, 0);
        return framebufferTexture;
    }


}
