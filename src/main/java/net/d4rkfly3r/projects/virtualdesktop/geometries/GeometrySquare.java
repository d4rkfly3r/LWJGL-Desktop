package net.d4rkfly3r.projects.virtualdesktop.geometries;

import org.joml.Vector3d;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Joshua Freedman on 1/31/2017.
 * Project: VirtualDesktop
 */
public class GeometrySquare extends Geometry {
    public Vector3d start, end;

    public GeometrySquare(Vector3d start, Vector3d end, Vector4f color) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.position = new Vector3d(start).add(end).div(2);
    }

    @Override
    public void render() {
        glColor4f(this.color.x(), this.color.y(), this.color.z(), this.color.w());
        glBegin(GL_QUADS);
        // FIXME: Don't think that 'Z' works correctly!
        glVertex3d(this.start.x(), this.start.y(), this.start.z());
        glVertex3d(this.end.x(), this.start.y(), this.start.z());
        glVertex3d(this.end.x(), this.end.y(), this.end.z());
        glVertex3d(this.start.x(), this.end.y(), this.end.z());
        glEnd();
    }

    public boolean pointLiesWithin(double x, double y, double z) {
        final boolean isInX = x >= this.start.x() && x <= this.end.x();
        final boolean isInY = y >= this.start.y() && y <= this.end.y();
        final boolean isInZ = z >= this.start.z() && z <= this.end.z();
        return isInX && isInY && isInZ;
    }

    public void info() {
        System.out.println(this.start.x() + " | " + this.start.y());
        System.out.println(this.end.x() + " | " + this.end.y());
    }

    @Override
    public String toString() {
        return "GeometrySquare{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
