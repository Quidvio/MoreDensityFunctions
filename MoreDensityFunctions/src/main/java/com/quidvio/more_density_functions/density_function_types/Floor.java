package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Floor(DensityFunction df) implements DensityFunctionTypes.class_6932 {

  private static final MapCodec<Floor> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
      .group(DensityFunction.FUNCTION_CODEC.fieldOf("argument").forGetter(Floor::df)).apply(instance, (Floor::new)));
  public static final CodecHolder<Floor> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);

  @Override
  public DensityFunction input() {
    return this.df;
  }

  @Override
  public double apply(double density) {
    return Math.floor(density);
  }

  @Override
  public DensityFunction apply(DensityFunctionVisitor visitor) {
    return new Floor(this.df.apply(visitor));
  }

  @Override
  public double minValue() {
    return apply(this.df.minValue());
  }

  @Override
  public double maxValue() {
    return apply(this.df.maxValue());
  }

  @Override
  public CodecHolder<? extends DensityFunction> getCodec() {
    return CODEC;
  }
}
