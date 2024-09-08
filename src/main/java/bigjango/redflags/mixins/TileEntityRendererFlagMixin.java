package bigjango.redflags.mixins;

import bigjango.redflags.IRedFlag;

import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelFlag;
import net.minecraft.client.render.Polygon;
import net.minecraft.client.render.Vertex;
import net.minecraft.client.render.tileentity.TileEntityRendererFlag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.client.render.tessellator.Tessellator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.lwjgl.opengl.GL11;

@Mixin(value = TileEntityRendererFlag.class, remap = false)
public class TileEntityRendererFlagMixin {
    @Mutable @Shadow @Final private ModelFlag flagModel;
    private ModelFlag flagModelMirrored;
    private boolean correct = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        flagModelMirrored = new ModelFlag();
        // Copy
        Polygon[] faces = ((CubeMixin) (Object) flagModelMirrored.flagOverlay).getFaces();
        Vertex[] newVerts = new Vertex[faces[5].vertexPositions.length];
        for (int i = 0; i < newVerts.length; i++) {
            newVerts[i] = new Vertex(faces[5].vertexPositions[i].vector3D, faces[5].vertexPositions[i].texturePositionX, faces[5].vertexPositions[i].texturePositionY);
        }
        faces[4] = new Polygon(newVerts);
        // Flip-a-de-do-da
        faces[4].flipFace();
    }

    @Inject(method = "doRender(Lnet/minecraft/core/block/entity/TileEntityFlag;DDDFZFF)V", at = @At("HEAD"))
    public void doRenderH(
        TileEntityFlag tileEntity, double x, double y, double z, float partialTick, boolean shortPole, float windDirection, float windIntensity, CallbackInfo ci
    ) {
        correct = ((IRedFlag) tileEntity).isCorrect();
    }

    @Redirect(
        method = "doRender(Lnet/minecraft/core/block/entity/TileEntityFlag;DDDFZFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/model/ModelFlag;renderFlagOverlay"
        )
    )
    public void renderFlagOverlay(ModelFlag self) {
        (correct ? flagModelMirrored : flagModel).renderFlagOverlay();
    }
}
