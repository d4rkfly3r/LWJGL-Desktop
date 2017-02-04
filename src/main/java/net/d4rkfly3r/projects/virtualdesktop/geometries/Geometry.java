package net.d4rkfly3r.projects.virtualdesktop.geometries;

import org.joml.Vector3d;
import org.joml.Vector4f;

/**
 * Created by Joshua Freedman on 1/31/2017.
 * Project: VirtualDesktop
 */
public abstract class Geometry {
    public double distance;
    public Vector3d position;
    public Vector4f color;

    abstract void render();

    public abstract void setPostRender(Runnable runnable);

    public abstract void setPreRender(Runnable runnable);
}
