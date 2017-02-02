package net.d4rkfly3r.projects.virtualdesktop.components;

import net.d4rkfly3r.projects.virtualdesktop.Desktop;
import net.d4rkfly3r.projects.virtualdesktop.TrueTypeFont;
import net.d4rkfly3r.projects.virtualdesktop.Util;
import net.d4rkfly3r.projects.virtualdesktop.parts.BasePart;
import net.d4rkfly3r.projects.virtualdesktop.parts.ComponentPart;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class StartMenu extends ComponentPart {

    public static final int SIDE_RIGHT = 1, SIDE_LEFT = 2, SIDE_TOP = 4, SIDE_BOTTOM = 8;
    private final Desktop desktop;
    private final TrueTypeFont trueTypeFont = new TrueTypeFont(TrueTypeFont.getFont("Courier New", Font.BOLD, 32), true);
    private int sidesToRender = SIDE_RIGHT | SIDE_TOP;
    private int frameSize = 6;
    private int userLogoTexID;

    public StartMenu(final Desktop desktop, final int yOffset) {
        this.desktop = desktop;
        this.width = 500;
        this.height = 800;
        this.positionX = 0;
        this.positionY = desktop.getDisplayHeight() - this.height - yOffset;
        userLogoTexID = Util.loadTexture("assets/d4rkfly3r.png");
    }

    @Override
    public void render() {
        int modXOff = (((sidesToRender & SIDE_LEFT) > 0) ? frameSize : 0);
        int modYOff = (((sidesToRender & SIDE_TOP) > 0) ? frameSize : 0);
        int modW = (((sidesToRender & SIDE_LEFT) > 0) ? frameSize : 0) + (((sidesToRender & SIDE_RIGHT) > 0) ? frameSize : 0);
        int modH = (((sidesToRender & SIDE_TOP) > 0) ? frameSize : 0) + (((sidesToRender & SIDE_BOTTOM) > 0) ? frameSize : 0);

        glColor3f(.15f, .15f, .15f);
        glBegin(GL_QUADS);
        glVertex3d(0, 0, 0.0D);
        glVertex3d(0, getHeight(), 0.0D);
        glVertex3d(getWidth(), getHeight(), 0.0D);
        glVertex3d(getWidth(), 0, 0.0D);
        glEnd();

        glColor3f(.25f, .25f, .25f);
        glBegin(GL_QUADS);
        glVertex3d(modXOff, modYOff, 0.0D);
        glVertex3d(modXOff, modYOff + (getHeight() - modH), 0.0D);
        glVertex3d(modXOff + (getWidth() - modW), modYOff + (getHeight() - modH), 0.0D);
        glVertex3d(modXOff + (getWidth() - modW), modYOff, 0.0D);
        glEnd();

        glColor4f(.15f, .15f, .15f, 1);
        glBegin(GL_QUADS);
        glVertex3d(modXOff, modYOff, 0.0D);
        glVertex3d(modXOff, modYOff + (getHeight() - modH), 0.0D);
        glVertex3d(modXOff + 40 * 1.6, modYOff + (getHeight() - modH), 0.0D);
        glVertex3d(modXOff + 40 * 1.6, modYOff, 0.0D);
        glEnd();

        glColor4f(1, 1, 1, 1);
        glBindTexture(GL_TEXTURE_2D, userLogoTexID);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3d(4 + modXOff, modYOff, 0);
        glTexCoord2f(1, 0);
        glVertex3d(-4 + modXOff + 40 * 1.6, modYOff, 0);
        glTexCoord2f(1, 1);
        glVertex3d(-4 + modXOff + 40 * 1.6, -8 + modYOff + modXOff + 40 * 1.6, 0);
        glTexCoord2f(0, 1);
        glVertex3d(4 + modXOff, -8 + modYOff + modXOff + 40 * 1.6, 0);
        glEnd();

    }

    @Override
    public BasePart revalidate() {
        return this;
    }
}
