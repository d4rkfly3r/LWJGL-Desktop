package net.d4rkfly3r.projects.virtualdesktop.components;

import net.d4rkfly3r.projects.virtualdesktop.geometries.GeometrySquare;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

/**
 * Created by Joshua Freedman on 1/31/2017.
 * Project: VirtualDesktop
 */
public class TexturedButton {
    private final GeometrySquare geometrySquare;
    private final int textureID;

    public TexturedButton(GeometrySquare geometrySquare, int textureID) {
        this.geometrySquare = geometrySquare;
        this.textureID = textureID;
    }

    public void render() {
        geometrySquare.render();
        glColor4f(this.geometrySquare.color.x(), this.geometrySquare.color.y(), this.geometrySquare.color.z(), 1);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glBegin(GL_QUADS);
        // FIXME: Don't think that 'Z' works correctly!
        glTexCoord2f(0, 0);
        glVertex3d(this.geometrySquare.start.x(), this.geometrySquare.start.y(), this.geometrySquare.start.z());
        glTexCoord2f(1, 0);
        glVertex3d(this.geometrySquare.end.x(), this.geometrySquare.start.y(), this.geometrySquare.start.z());
        glTexCoord2f(1, 1);
        glVertex3d(this.geometrySquare.end.x(), this.geometrySquare.end.y(), this.geometrySquare.end.z());
        glTexCoord2f(0, 1);
        glVertex3d(this.geometrySquare.start.x(), this.geometrySquare.end.y(), this.geometrySquare.end.z());
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);

    }
}
