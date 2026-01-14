package imgui.gl3;

import imgui.ImDrawData;
import imgui.ImVec4;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * AMD-compatible subclass of ImGuiImplGl3 that avoids glDrawElementsBaseVertex
 * and manages its own independent VBO/EBO buffers using GL_STATIC_DRAW.
 *
 * The AMD Radeon integrated GPU driver (atio6axx.dll) crashes when VBOs
 * created by the parent class are used after the game's own VBOs have been
 * set up. By creating entirely new independent buffers and using GL_STATIC_DRAW
 * (reallocating on size increase), we avoid the AMD driver's broken
 * buffer-orphaning code path entirely.
 */
public class ImGuiImplGl3AMD extends ImGuiImplGl3 {

    // Our own independent VBO and EBO, separate from data.vboHandle/elementsHandle
    private int amdVboHandle      = 0;
    private int amdElementsHandle = 0;
    private int amdCurrentVtxSize = 0;
    private int amdCurrentIdxSize = 0;

    private final int[] lastActiveTexture = new int[1];
    private final int[] lastProgram       = new int[1];
    private final int[] lastTexture       = new int[1];
    private final int[] lastArrayBuffer   = new int[1];
    private final int[] lastVertexArray   = new int[1];
    private final int[] lastBlendSrcRgb   = new int[1];
    private final int[] lastBlendDstRgb   = new int[1];
    private final int[] lastBlendSrcAlpha = new int[1];
    private final int[] lastBlendDstAlpha = new int[1];
    private final int[] lastBlendEqRgb    = new int[1];
    private final int[] lastBlendEqAlpha  = new int[1];
    private final int[] lastViewport      = new int[4];
    private final int[] lastScissor       = new int[4];
    private final float[] ortho           = new float[16];
    private final ImVec4 clipRect         = new ImVec4();

    @Override
    public boolean init(final String glslVersion) {
        final boolean result = super.init(glslVersion);
        // Create our own independent buffers that won't be affected by the
        // game renderer's VBO state
        amdVboHandle = glGenBuffers();
        amdElementsHandle = glGenBuffers();
        return result;
    }

    @Override
    public void renderDrawData(final ImDrawData drawData) {
        final int fbWidth  = (int) (drawData.getDisplaySizeX() * drawData.getFramebufferScaleX());
        final int fbHeight = (int) (drawData.getDisplaySizeY() * drawData.getFramebufferScaleY());
        if (fbWidth <= 0 || fbHeight <= 0) return;
        if (drawData.getCmdListsCount() <= 0) return;

        // -- Save GL state -------------------------------------------------------
        glGetIntegerv(GL_ACTIVE_TEXTURE, lastActiveTexture);
        glActiveTexture(GL_TEXTURE0);
        glGetIntegerv(GL_CURRENT_PROGRAM, lastProgram);
        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture);
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer);
        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, lastVertexArray);
        glGetIntegerv(GL_VIEWPORT, lastViewport);
        glGetIntegerv(GL_SCISSOR_BOX, lastScissor);
        glGetIntegerv(GL_BLEND_SRC_RGB, lastBlendSrcRgb);
        glGetIntegerv(GL_BLEND_DST_RGB, lastBlendDstRgb);
        glGetIntegerv(GL_BLEND_SRC_ALPHA, lastBlendSrcAlpha);
        glGetIntegerv(GL_BLEND_DST_ALPHA, lastBlendDstAlpha);
        glGetIntegerv(GL_BLEND_EQUATION_RGB, lastBlendEqRgb);
        glGetIntegerv(GL_BLEND_EQUATION_ALPHA, lastBlendEqAlpha);
        final boolean lastBlend     = glIsEnabled(GL_BLEND);
        final boolean lastCullFace  = glIsEnabled(GL_CULL_FACE);
        final boolean lastDepth     = glIsEnabled(GL_DEPTH_TEST);
        final boolean lastScissorEn = glIsEnabled(GL_SCISSOR_TEST);

        // -- Setup render state --------------------------------------------------
        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_SCISSOR_TEST);
        glViewport(0, 0, fbWidth, fbHeight);

        // Build orthographic projection
        final float L = drawData.getDisplayPosX();
        final float R = drawData.getDisplayPosX() + drawData.getDisplaySizeX();
        final float T = drawData.getDisplayPosY();
        final float B = drawData.getDisplayPosY() + drawData.getDisplaySizeY();
        Arrays.fill(ortho, 0f);
        ortho[0]  = 2.0f / (R - L);
        ortho[5]  = 2.0f / (T - B);
        ortho[10] = -1.0f;
        ortho[12] = (R + L) / (L - R);
        ortho[13] = (T + B) / (B - T);
        ortho[15] = 1.0f;

        glUseProgram(data.shaderHandle);
        glUniform1i(data.attribLocationTex, 0);
        glUniformMatrix4fv(data.attribLocationProjMtx, false, ortho);

        // Create temporary VAO using OUR independent buffers
        final int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, amdVboHandle);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, amdElementsHandle);

        glEnableVertexAttribArray(data.attribLocationVtxPos);
        glEnableVertexAttribArray(data.attribLocationVtxUV);
        glEnableVertexAttribArray(data.attribLocationVtxColor);

        final int stride = ImDrawData.sizeOfImDrawVert();
        glVertexAttribPointer(data.attribLocationVtxPos,   2, GL_FLOAT,         false, stride, 0);
        glVertexAttribPointer(data.attribLocationVtxUV,    2, GL_FLOAT,         false, stride, 8);
        glVertexAttribPointer(data.attribLocationVtxColor, 4, GL_UNSIGNED_BYTE, true,  stride, 16);

        // -- Render command lists ------------------------------------------------
        final float clipOffX   = drawData.getDisplayPosX();
        final float clipOffY   = drawData.getDisplayPosY();
        final float clipScaleX = drawData.getFramebufferScaleX();
        final float clipScaleY = drawData.getFramebufferScaleY();
        final int idxType = ImDrawData.sizeOfImDrawIdx() == 2 ? GL_UNSIGNED_SHORT : GL_UNSIGNED_INT;

        for (int n = 0; n < drawData.getCmdListsCount(); n++) {
            final java.nio.ByteBuffer vtxData = drawData.getCmdListVtxBufferData(n);
            final java.nio.ByteBuffer idxData = drawData.getCmdListIdxBufferData(n);
            final int vtxSize = vtxData.remaining();
            final int idxSize = idxData.remaining();

            // Upload vertex data. Only reallocate if the buffer is too small.
            if (vtxSize > amdCurrentVtxSize) {
                glBufferData(GL_ARRAY_BUFFER, vtxSize, GL_STATIC_DRAW);
                amdCurrentVtxSize = vtxSize;
            }
            glBufferSubData(GL_ARRAY_BUFFER, 0, vtxData);

            // Upload index data. Only reallocate if the buffer is too small.
            if (idxSize > amdCurrentIdxSize) {
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, idxSize, GL_STATIC_DRAW);
                amdCurrentIdxSize = idxSize;
            }
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, idxData);

            for (int cmd = 0; cmd < drawData.getCmdListCmdBufferSize(n); cmd++) {
                drawData.getCmdListCmdBufferClipRect(clipRect, n, cmd);
                final float clipMinX = (clipRect.x - clipOffX) * clipScaleX;
                final float clipMinY = (clipRect.y - clipOffY) * clipScaleY;
                final float clipMaxX = (clipRect.z - clipOffX) * clipScaleX;
                final float clipMaxY = (clipRect.w - clipOffY) * clipScaleY;
                if (clipMaxX <= clipMinX || clipMaxY <= clipMinY) continue;

                glScissor(
                    (int) clipMinX,
                    (int) (fbHeight - clipMaxY),
                    (int) (clipMaxX - clipMinX),
                    (int) (clipMaxY - clipMinY)
                );

                final long texId     = drawData.getCmdListCmdBufferTextureId(n, cmd);
                final int  elemCount = drawData.getCmdListCmdBufferElemCount(n, cmd);
                final int  idxOffset = drawData.getCmdListCmdBufferIdxOffset(n, cmd);

                glBindTexture(GL_TEXTURE_2D, (int) texId);

                // Use plain glDrawElements, never glDrawElementsBaseVertex.
                // VtxOffset is always 0 since RendererHasVtxOffset is not set.
                final long idxByteOffset = (long) idxOffset * ImDrawData.sizeOfImDrawIdx();
                glDrawElements(GL_TRIANGLES, elemCount, idxType, idxByteOffset);
            }
        }

        // -- Cleanup & restore state --------------------------------------------
        glDeleteVertexArrays(vao);

        glUseProgram(lastProgram[0]);
        glBindTexture(GL_TEXTURE_2D, lastTexture[0]);
        glActiveTexture(lastActiveTexture[0]);
        glBindVertexArray(lastVertexArray[0]);
        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer[0]);
        glBlendEquationSeparate(lastBlendEqRgb[0], lastBlendEqAlpha[0]);
        glBlendFuncSeparate(lastBlendSrcRgb[0], lastBlendDstRgb[0], lastBlendSrcAlpha[0], lastBlendDstAlpha[0]);
        if (lastBlend)     glEnable(GL_BLEND);      else glDisable(GL_BLEND);
        if (lastCullFace)  glEnable(GL_CULL_FACE);  else glDisable(GL_CULL_FACE);
        if (lastDepth)     glEnable(GL_DEPTH_TEST);  else glDisable(GL_DEPTH_TEST);
        if (lastScissorEn) glEnable(GL_SCISSOR_TEST); else glDisable(GL_SCISSOR_TEST);
        glViewport(lastViewport[0], lastViewport[1], lastViewport[2], lastViewport[3]);
        glScissor(lastScissor[0], lastScissor[1], lastScissor[2], lastScissor[3]);
    }
}
