package net.d4rkfly3r.projects.virtualdesktop;

import net.d4rkfly3r.projects.virtualdesktop.components.BasicWindow;
import net.d4rkfly3r.projects.virtualdesktop.parts.BasePart;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

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
    private final HashMap<Integer, List<BiConsumer<Integer, Integer>>> keyBindings = new HashMap<>();
    private GLFWVidMode vidmode;
    // The window handle
    private long window;
    private Desktop desktop;
    private float xAngle, yAngle;
    private final GLFWKeyCallbackI glfwKeyCallbackI = (window, key, scancode, action, mods) -> {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            return;
        }
        if (this.keyBindings.containsKey(key)) {
            this.keyBindings.get(key).iterator().forEachRemaining(con -> con.accept(key, action));
        }
        double dAngle = 5;
        switch (key) {
            case GLFW_KEY_ESCAPE:
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
    };
    private BasePart lastBasePart;
    private int width;
    private int height;
    private int framebufferID;
    private int framebufferTexID;
    private double lastMouseX, lastMouseY;
    private int lastMouseButtonAction = -1;
    private int lastMouseButton = -1;
    private final GLFWCursorPosCallbackI glfwCursorPosCallbackI = (window1, xPos, yPos) -> {
        this.lastMouseX = xPos;
        this.lastMouseY = yPos;
        if (this.lastMouseButton == GLFW_MOUSE_BUTTON_LEFT && this.lastMouseButtonAction == GLFW_PRESS) {
            if (this.lastBasePart != null) {
                this.lastBasePart.mouseDrag((int) this.lastMouseX - this.lastBasePart.getPositionX(), (int) this.lastMouseY - this.lastBasePart.getPositionY(), this.lastMouseButton);
            }
        }
    };
    private final GLFWMouseButtonCallbackI glfwMouseButtonCallbackI = (window1, button, action, mods) -> {
        this.lastMouseButtonAction = action;
        this.lastMouseButton = button;
        this.lastBasePart = this.desktop.getPartUnder(((int) this.lastMouseX), ((int) this.lastMouseY));
        if (action == GLFW_RELEASE) {
            if (!this.desktop.overrideMouseRelease(((int) this.lastMouseX), ((int) this.lastMouseY), button)) {
                if (this.lastBasePart != null) {
                    this.desktop.use(this.lastBasePart);
                    this.lastBasePart.mouseReleased((int) this.lastMouseX - this.lastBasePart.getPositionX(), (int) this.lastMouseY - this.lastBasePart.getPositionY(), button);
                }
            }
        }
        if (action == GLFW_PRESS) {
            if (!this.desktop.overrideMousePress(((int) this.lastMouseX), ((int) this.lastMouseY), button)) {
                if (this.lastBasePart != null) {
                    this.desktop.use(this.lastBasePart);
                    this.lastBasePart.mouseClicked((int) this.lastMouseX - this.lastBasePart.getPositionX(), (int) this.lastMouseY - this.lastBasePart.getPositionY(), button);
                }
            }
        }
    };

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

        glfwSetKeyCallback(window, glfwKeyCallbackI);

        glfwSetMouseButtonCallback(window, glfwMouseButtonCallbackI);

        glfwSetCursorPosCallback(window, glfwCursorPosCallbackI);

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            width = vidmode.width();
            height = vidmode.height();

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

    private void postInit() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

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
        framebufferTexID = createFBOTexture();

        registerKeyBinds();

    }

    private void loop() {

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glBindTexture(GL_TEXTURE_2D, 0); // unlink textures because if we dont it all is gonna fail

            glPushMatrix();

            startFBORender();
            glPushMatrix();
            this.desktop.render();
            glPopMatrix();
            finishFBORender();

            glPushAttrib(GL_CURRENT_BIT);
            glPushMatrix();
            glTranslatef(0, 0, width + height - 1); // Sets render depth to one below the farthest depth.
            this.desktop.renderBase();
            glPopMatrix();
            glPopAttrib();

            // Rotate... currently incorrect and for testing only
//            glTranslatef(width / 2, height / 2, 0);
//            glRotatef(xAngle, 1, 0, 0);
//            glRotatef(yAngle, 0, 1, 0);
//            glTranslatef(-width / 2, -height / 2, 0);
//            drawAxis();

            // Draw the FBO to the screen.
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

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            glPopMatrix();
            glPopAttrib();

        }
    }

    private void finishFBORender() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glPopAttrib();
    }

    private void startFBORender() {
        glPushAttrib(GL_CURRENT_BIT);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
        glClearColor(0, 0, 0, 0); //transparent black
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
    }

    private void drawAxis() {
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

    private int createFBOTexture() {
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

    private void addKeyBinding(int key, BiConsumer<Integer, Integer> consumer) {
        if (!this.keyBindings.containsKey(key)) {
            this.keyBindings.put(key, new ArrayList<>());
        }
        this.keyBindings.get(key).add(consumer);
    }

    private void registerKeyBinds() {
        System.out.println("(Re)Registering Key Bindings!");
        this.keyBindings.clear();
        addKeyBinding(GLFW_KEY_E, (integer, integer2) -> System.out.println(integer + " | " + integer2));
        addKeyBinding(GLFW_KEY_S, (integer, integer2) -> System.err.println(integer + " | " + integer2));
        addKeyBinding(GLFW_KEY_K, (key, action) -> {
            if (action == GLFW_RELEASE) {
                final BasicWindow basicWindow = new BasicWindow(this.desktop);
                basicWindow.setPositionX(25).setPositionY(25).setWidth(150).setHeight(150);
                basicWindow.setTitle("FoxEdit Info").revalidate();
                System.out.println("Adding window: " + basicWindow);
                desktop.use(basicWindow);
            }
        });
    }
}
