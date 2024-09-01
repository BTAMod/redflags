package bigjango.redflags.mixins;

import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.Polygon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Cube.class, remap = false)
public interface CubeMixin {
    @Accessor
    public abstract Polygon[] getFaces();
    @Accessor
    public abstract void setFaces(Polygon[] polygoingoingone);
}