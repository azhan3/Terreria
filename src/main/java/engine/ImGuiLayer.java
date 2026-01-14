package engine;

import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;
import imgui.gl3.ImGuiImplGl3AMD;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer {

    private long glfwWindow;

    // Mouse cursors provided by GLFW
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

    // LWJGL3 renderer patched for AMD GPU compatibility (avoids glDrawElementsBaseVertex)
    private final ImGuiImplGl3AMD imGuiGl3 = new ImGuiImplGl3AMD();

    public ImGuiLayer(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    // Initialize Dear ImGui.
    public void initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("imgui.ini"); // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
        io.addBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // ------------------------------------------------------------
        // Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

        // ------------------------------------------------------------
        // GLFW callbacks to handle user input

        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            int imguiKey = mapKey(key);
            if (imguiKey != ImGuiKey.None) {
                io.addKeyEvent(imguiKey, action != GLFW_RELEASE);
            }

            io.addKeyEvent(ImGuiKey.ModCtrl, (mods & GLFW_MOD_CONTROL) != 0);
            io.addKeyEvent(ImGuiKey.ModShift, (mods & GLFW_MOD_SHIFT) != 0);
            io.addKeyEvent(ImGuiKey.ModAlt, (mods & GLFW_MOD_ALT) != 0);
            io.addKeyEvent(ImGuiKey.ModSuper, (mods & GLFW_MOD_SUPER) != 0);

            if (!io.getWantCaptureKeyboard()) {
                KeyListener.keyCallback(w, key, scancode, action, mods);
            }
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            int buttonIndex = mapMouseButton(button);
            if (buttonIndex != -1) {
                io.addMouseButtonEvent(buttonIndex, action != GLFW_RELEASE);
            }

            if (!io.getWantCaptureMouse() && button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE) {
                ImGui.setWindowFocus(null);
            }

            if (!io.getWantCaptureMouse()) {
                MouseListener.mouseButtonCallback(w, button, action, mods);
            }
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) ->
            io.addMouseWheelEvent((float) xOffset, (float) yOffset));

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        // Fonts merge example
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/segoeui.ttf", 32, fontConfig);

        fontConfig.destroy(); // After all fonts were added we don't need this config more

        // ------------------------------------------------------------
        // Build fonts texture using the default stb_truetype backend
        fontAtlas.build();

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGl3.init("#version 330 core");
    }

    public void update(float dt, Scene currentScene) {
        startFrame(dt);

        // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
        ImGui.newFrame();
        currentScene.sceneImgui();
        ImGui.render();

        endFrame();
    }

    private void startFrame(final float deltaTime) {
        // Get window properties and mouse position
        float[] winWidth = {Window.getWidth()};
        float[] winHeight = {Window.getHeight()};
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.addMousePosEvent((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    private void endFrame() {
        // Flush the GPU pipeline before ImGui renders.
        // The AMD Radeon driver (atio6axx.dll) has an async DMA bug that causes
        // a null-pointer crash inside glDraw* if game render commands are still
        // in-flight when ImGui uploads its vertex/index data to the same memory pool.
        // glFinish() ensures all previous GL commands complete before ImGui starts.
        org.lwjgl.opengl.GL11.glFinish();

        // Also unbind game OpenGL objects for a clean state
        org.lwjgl.opengl.GL30.glBindVertexArray(0);
        org.lwjgl.opengl.GL15.glBindBuffer(org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER, 0);
        org.lwjgl.opengl.GL15.glBindBuffer(org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        org.lwjgl.opengl.GL20.glUseProgram(0);

        // Only render when there is actual draw data to avoid a null-deref
        // crash inside the AMD GPU driver (atio6axx.dll) on glDrawElements*.
        imgui.ImDrawData drawData = ImGui.getDrawData();
        if (drawData != null && drawData.getCmdListsCount() > 0) {
            imGuiGl3.renderDrawData(drawData);
        }
    }

    // If you want to clean a room after yourself - do it by yourself
    private void destroyImGui() {
        imGuiGl3.shutdown();
        ImGui.destroyContext();
    }

    private int mapMouseButton(int button) {
        if (button == GLFW_MOUSE_BUTTON_1) {
            return 0;
        }
        if (button == GLFW_MOUSE_BUTTON_2) {
            return 1;
        }
        if (button == GLFW_MOUSE_BUTTON_3) {
            return 2;
        }
        if (button == GLFW_MOUSE_BUTTON_4) {
            return 3;
        }
        if (button == GLFW_MOUSE_BUTTON_5) {
            return 4;
        }
        return -1;
    }

    private int mapKey(int key) {
        switch (key) {
            case GLFW_KEY_TAB:
                return ImGuiKey.Tab;
            case GLFW_KEY_LEFT:
                return ImGuiKey.LeftArrow;
            case GLFW_KEY_RIGHT:
                return ImGuiKey.RightArrow;
            case GLFW_KEY_UP:
                return ImGuiKey.UpArrow;
            case GLFW_KEY_DOWN:
                return ImGuiKey.DownArrow;
            case GLFW_KEY_PAGE_UP:
                return ImGuiKey.PageUp;
            case GLFW_KEY_PAGE_DOWN:
                return ImGuiKey.PageDown;
            case GLFW_KEY_HOME:
                return ImGuiKey.Home;
            case GLFW_KEY_END:
                return ImGuiKey.End;
            case GLFW_KEY_INSERT:
                return ImGuiKey.Insert;
            case GLFW_KEY_DELETE:
                return ImGuiKey.Delete;
            case GLFW_KEY_BACKSPACE:
                return ImGuiKey.Backspace;
            case GLFW_KEY_SPACE:
                return ImGuiKey.Space;
            case GLFW_KEY_ENTER:
                return ImGuiKey.Enter;
            case GLFW_KEY_ESCAPE:
                return ImGuiKey.Escape;
            case GLFW_KEY_KP_ENTER:
                return ImGuiKey.KeypadEnter;
            case GLFW_KEY_A:
                return ImGuiKey.A;
            case GLFW_KEY_C:
                return ImGuiKey.C;
            case GLFW_KEY_V:
                return ImGuiKey.V;
            case GLFW_KEY_X:
                return ImGuiKey.X;
            case GLFW_KEY_Y:
                return ImGuiKey.Y;
            case GLFW_KEY_Z:
                return ImGuiKey.Z;
            default:
                return ImGuiKey.None;
        }
    }
}