package bigjango.redflags.mixins;

import bigjango.redflags.IRedFlag;

import net.minecraft.client.render.FlagRenderer;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.OpenGLHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FlagRenderer.class, remap = false)
public class FlagRendererMixin {
    @Shadow
    private int texture;
    @Shadow
    private RenderEngine renderEngine;
    int newTexture = -1;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderEngine;createTexture"))
    public int init_createTex(RenderEngine e, int w, int h) {
        return -1;
    }

    @Inject(method = "updateTexture", at = @At("HEAD"), cancellable = true)
    public void updateTexture(TileEntityFlag tileEntity, CallbackInfo ci) {
        int text = ((IRedFlag) tileEntity).getTexture();
        if (text == -1) {
            newTexture = renderEngine.createTexture(24, 16);;
            ((IRedFlag) tileEntity).setTexture(newTexture);
        } else {
            newTexture = text;
            ci.cancel();
        }
    }

    @Redirect(method = "updateTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderEngine;updateTextureData"))
    private void newText(RenderEngine e, int[] data, int w, int h, int _textureId) {
        e.updateTextureData(data, w, h, newTexture);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    public void getTexture(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(newTexture);
    }
}
