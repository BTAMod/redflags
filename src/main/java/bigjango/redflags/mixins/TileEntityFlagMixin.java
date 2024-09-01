package bigjango.redflags.mixins;

import bigjango.redflags.IReversable;

import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelFlag;
import net.minecraft.client.render.Polygon;
import net.minecraft.client.render.Vertex;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.client.render.tessellator.Tessellator;
import com.mojang.nbt.CompoundTag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityFlag.class, remap = false)
public class TileEntityFlagMixin extends TileEntity implements IReversable {
    private boolean correct = false;

    @Override
    public boolean isCorrect() {
        return correct;
    }

    @Override
    public void setCorrect(boolean correct) {
        this.correct = correct;
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
