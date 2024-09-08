package bigjango.redflags.mixins;

import bigjango.redflags.IRedFlag;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.GLAllocation;
import com.mojang.nbt.CompoundTag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityFlag.class, remap = false)
public class TileEntityFlagMixin extends TileEntity implements IRedFlag {
    private boolean correct = false;
    @Override public boolean isCorrect() { return correct; }
    @Override public void setCorrect(boolean correct) { this.correct = correct; }

    private int texture = -1;
    @Override public int getTexture() { return texture; }
    @Override public void setTexture(int texture) {
        if (this.texture != -1) GLAllocation.deleteTexture(this.texture);
        this.texture = texture;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (texture != -1) {
            GLAllocation.deleteTexture(texture);
        }
    }

    @Inject(method = "writeFlagNBT", at = @At("TAIL"))
    public void writeFlagNBT(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("Flip", correct);
    }

    @Inject(method = "readFlagNBT", at = @At("TAIL"))
    public void readFlagNBT(CompoundTag tag, CallbackInfo ci) {
        correct = tag.getBooleanOrDefault("Flip", false);
    }

    @Inject(method = "copyFlagNBT", at = @At("TAIL"))
    public void copyFlagNBT(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("Flip", correct);
    }
}
