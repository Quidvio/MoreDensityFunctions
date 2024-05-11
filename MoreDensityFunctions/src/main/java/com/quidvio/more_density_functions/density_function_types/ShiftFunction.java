package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record ShiftFunction(DensityFunction df, DensityFunction shiftx, DensityFunction shifty, DensityFunction shiftz) implements DensityFunction {

    private static final MapCodec<ShiftFunction> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(ShiftFunction::df), DensityFunction.FUNCTION_CODEC.fieldOf("shift_x").forGetter(ShiftFunction::shiftx), DensityFunction.FUNCTION_CODEC.fieldOf("shift_y").forGetter(ShiftFunction::shifty), DensityFunction.FUNCTION_CODEC.fieldOf("shift_z").forGetter(ShiftFunction::shiftz)).apply(instance, (ShiftFunction::new)));
    public static final CodecHolder<ShiftFunction> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        return this.df.sample(new UnblendedNoisePos(pos.blockX()+(int)this.shiftx.sample(pos),pos.blockY()+(int)this.shifty.sample(pos),pos.blockZ()+(int)this.shiftz.sample(pos)));
    }

    @Override
    public void method_40470(double[] ds, class_6911 arg) {
        arg.method_40478(ds, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new ShiftFunction(this.df,this.shiftx,this.shifty,this.shiftz));
    }

    @Override
    public DensityFunction shiftx() {
        return shiftx;
    }

    @Override
    public DensityFunction shifty() {
        return shifty;
    }

    @Override
    public DensityFunction shiftz() {
        return shiftz;
    }

    @Override
    public DensityFunction df() {
        return df;
    }

    @Override
    public double minValue() {
        return df.minValue();
    }

    @Override
    public double maxValue() {
        return df.maxValue();
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC;
    }
}
