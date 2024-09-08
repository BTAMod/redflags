package bigjango.redflags.mixins;

import bigjango.redflags.IRedFlag;

import net.minecraft.client.render.FlagRenderer;
import net.minecraft.core.block.entity.TileEntityFlag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FlagRenderer.class, remap = false)
public class FlagRendererMixin {
    @Inject(method = "updateTexture", at = @At("HEAD"), cancellable = true)
    public void updateTexture(TileEntityFlag tileEntity, CallbackInfo ci) {
        if (!((IRedFlag) tileEntity).shouldRedraw()) {
            ci.cancel();
        }
        ((IRedFlag) tileEntity).setRedraw(false);
    }
}
