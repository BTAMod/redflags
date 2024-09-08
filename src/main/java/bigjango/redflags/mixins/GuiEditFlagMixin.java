package bigjango.redflags.mixins;

import bigjango.redflags.IRedFlag;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiEditFlag;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiTexturedButton;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.block.entity.TileEntityFlag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiEditFlag.class, remap = false)
public abstract class GuiEditFlagMixin extends GuiContainer {
    @Shadow
    private final TileEntityFlag tileEntity;

    GuiTexturedButton reverso;

    public GuiEditFlagMixin(Container i) {
        super(i);
        tileEntity = null;
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init_(CallbackInfo ci) {
        reverso = new GuiTexturedButton(
            7, "/assets/redflags/buttons.png", (this.width - this.xSize) / 2 + 12, (this.height - this.ySize) / 2 + 61, 0, 0, 12, 12
        );
        controlList.add(reverso);
    }

    @Inject(method = "buttonPressed", at = @At("TAIL"))
    private void buttonPressed_(GuiButton button, CallbackInfo ci) {
        if (button.id == 7) {
            ((IRedFlag) tileEntity).setCorrect(!((IRedFlag) tileEntity).isCorrect());
        }
    }
}
