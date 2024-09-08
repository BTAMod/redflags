package bigjango.redflags.mixins;

import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.tessellator.Tessellator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.lwjgl.opengl.GL11;

@Mixin(value = RenderGlobal.class, remap = false)
public abstract class RenderGlobalMixin {
    @Shadow
    private Minecraft mc;

    private void renderName(String name, ICamera camera, int x, int y, int z, float renderPartialTicks) {
        if (name == "") return;

        float fx = (float) x - (float) camera.getX(renderPartialTicks);
        float fy = (float) y - (float) camera.getY(renderPartialTicks);
        float fz = (float) z - (float) camera.getZ(renderPartialTicks);

        FontRenderer fontrenderer = this.mc.fontRenderer;

        GL11.glPushMatrix();
        GL11.glTranslatef((float)fx + 0.5F, (float)fy + 2.3F, (float)fz + 0.5F);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef((float) -camera.getYRot(renderPartialTicks), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef((float) camera.getXRot(renderPartialTicks), 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-0.026666671F, -0.026666671F, 0.026666671F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        byte byte0 = 0;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        // Draw box
        tessellator.startDrawingQuads();
        int j = fontrenderer.getStringWidth(name) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex((double)(-j - 1), (double)(-1 + byte0), 0.0D);
        tessellator.addVertex((double)(-j - 1), (double)(8 + byte0), 0.0D);
        tessellator.addVertex((double)(j + 1), (double)(8 + byte0), 0.0D);
        tessellator.addVertex((double)(j + 1), (double)(-1 + byte0), 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // Draw name
        fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, byte0, 553648127);
        fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, byte0, 0xFFFFFF);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @Inject(method = "drawDebugEntityOutlines", at = @At("HEAD"), cancellable = true)
    void drawDebugEntityOutlines(ICamera camera, float partialTicks, CallbackInfo ci) {
        for (Object te : mc.theWorld.loadedTileEntityList) {
            if (te instanceof TileEntityFlag) {
                TileEntityFlag flag = ((TileEntityFlag) te);
                renderName(flag.owner, camera, (int) flag.x, (int) flag.y, (int) flag.z, partialTicks);
            }
        }
    }
}
