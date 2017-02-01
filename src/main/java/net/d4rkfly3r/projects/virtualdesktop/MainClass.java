package net.d4rkfly3r.projects.virtualdesktop;

import net.d4rkfly3r.projects.virtualdesktop.parts.BasePart;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Joshua Freedman on 1/26/2017.
 * Project: VirtualDesktop
 */
public class MainClass {
    private GLFWVidMode vidmode;
    // The window handle
    private long window;
    private Desktop desktop;
    private float xAngle, yAngle;
    private BasePart lastBasePart;
    private int width;
    private int height;
    private int framebufferID;
    private int framebufferTexID;

    public static void main(String[] args) {
        new MainClass().run();
    }

    public void run() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        init();
        postInit();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void postInit() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        width = vidmode.width();
        height = vidmode.height();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, width + height, -width - height);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        glShadeModel(GL_SMOOTH);                            //Enables Smooth Color Shading
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               //This Will Clear The Background Color To Black
        glClearDepth(1.0);                                  //Enables Clearing Of The Depth Buffer
        glEnable(GL_DEPTH_TEST);                            //Enables Depth Testing
        glDepthFunc(GL_LEQUAL);                             //The Type Of Depth Test To Do
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);  // Really Nice Perspective Calculations
        glEnable(GL_TEXTURE_2D);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        this.desktop = new Desktop(this);

        framebufferID = createFBO();
        framebufferTexID = creatFBOTexture();

    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        // Create the window
        window = glfwCreateWindow(800, 600, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//            if (action == GLFW_RELEASE) {
            double dAngle = 5;
            switch (key) {
                case GLFW_KEY_ESCAPE:
                    if (action == GLFW_RELEASE) {
                        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                    }
                    break;
                case GLFW_KEY_LEFT:
                    yAngle -= dAngle;
                    break;
                case GLFW_KEY_RIGHT:
                    yAngle += dAngle;
                    break;
                case GLFW_KEY_UP:
                    xAngle += dAngle;
                    break;
                case GLFW_KEY_DOWN:
                    xAngle -= dAngle;
                    break;
            }
//            }
        });


        glfwSetCursorPosCallback(window, (window1, xpos, ypos) -> {
            final BasePart basePart = this.desktop.getPartUnder((int) xpos, (int) ypos);
            if (basePart != null) {
                this.lastBasePart = basePart;
            }

        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glBindTexture(GL_TEXTURE_2D, 0); // unlink textures because if we dont it all is gonna fail

            glPushMatrix();

            glPushAttrib(GL_CURRENT_BIT);

            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
            {
                glClearColor(0, 0, 0, 0); //transparent black
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
                glPushMatrix();
                this.desktop.render();
                glPopMatrix();
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            }
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

            glPopAttrib();

            glPushAttrib(GL_CURRENT_BIT);
            glPushMatrix();
            glTranslatef(0, 0, width + height - 1);
            this.desktop.renderBase();
            glPopMatrix();
            glPopAttrib();


            glTranslatef(width / 2, height / 2, 0);
            glRotatef(xAngle, 1, 0, 0);
            glRotatef(yAngle, 0, 1, 0);
            glTranslatef(-width / 2, -height / 2, 0);


            glLineWidth(5f);
            glColor3f(1, 0, 0);
            glBegin(GL_LINE_STRIP);
            glVertex3f(0, 0, 0);
            glVertex3f(100, 0, 0);
            glEnd();
            glColor3f(0, 1, 0);
            glBegin(GL_LINE_STRIP);
            glVertex3f(0, 0, 0);
            glVertex3f(0, 100, 0);
            glEnd();
            glColor3f(0, 0, 1);
            glBegin(GL_LINE_STRIP);
            glVertex3f(0, 0, 0);
            glVertex3f(0, 0, 100);
            glEnd();

            glColor4f(1, 1, 1, 1);
            glBindTexture(GL_TEXTURE_2D, framebufferTexID);
            {
                glBegin(GL_QUADS);
                glTexCoord2f(0, 1);
                glVertex3f(0, 0, 0);
                glTexCoord2f(1, 1);
                glVertex3f(width, 0, 0);
                glTexCoord2f(1, 0);
                glVertex3f(width, height, 0);
                glTexCoord2f(0, 0);
                glVertex3f(0, height, 0);
                glEnd();
            }
            glBindTexture(GL_TEXTURE_2D, 0);
            glEnable(GL_BLEND);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            glPopMatrix();
            glPopAttrib();

        }
    }

    private int createFBO() {

        //frame buffer
        final int framebufferObjID = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferObjID);

        //depth buffer
        final int depthbufferID = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthbufferID);

        //allocate space for the renderbuffer
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, width, height);

        //attach depth buffer to framebufferObjID
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthbufferID);
        return framebufferObjID;
    }

    private int creatFBOTexture() {
        //create texture to render to
        final int framebufferTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, framebufferTexture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);

        //attach texture to the framebufferObjID
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, framebufferTexture, 0);

        //check completeness
        if (glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) == GL_FRAMEBUFFER_COMPLETE_EXT) {
            System.out.println("Frame buffer created successfully.");
        } else {
            System.out.println("An error occurred creating the frame buffer.");
        }
        return framebufferTexture;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
