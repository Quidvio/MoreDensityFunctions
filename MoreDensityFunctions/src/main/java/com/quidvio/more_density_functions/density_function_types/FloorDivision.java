package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record FloorDivision(DensityFunction dividend, DensityFunction divisor, double maxOutput, double minOutput,
    DensityFunction errorDf) implements DensityFunction {

  private static final MapCodec<FloorDivision> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
      .group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(FloorDivision::dividend),
          DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(FloorDivision::divisor),
          Codec
              .doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max_output")
              .forGetter(FloorDivision::maxOutput),
          Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min_output")
              .forGetter(FloorDivision::minOutput),
          DensityFunction.FUNCTION_CODEC.fieldOf("error_output").forGetter(FloorDivision::errorDf))
      .apply(instance, (FloorDivision::new)));
  public static final CodecHolder<FloorDivision> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

  @Override
  public double sample(NoisePos pos) {
    int divisorValue = (int) this.divisor.sample(pos);
    int dividendValue = (int) this.dividend.sample(pos);

    if (divisorValue == 0) {
      return this.errorDf.sample(pos);
    }

    double result = Math.floorDiv(dividendValue, divisorValue);

    if (result > this.maxOutput) {
      return this.maxOutput;
    }

    if (result < this.minOutput) {
      return this.minOutput;
    }

    return result;
  }

  @Override
  public void method_40470(double[] densities, class_6911 applier) {
    applier.method_40478(densities, this);
  }

  @Override
  public DensityFunction apply(DensityFunctionVisitor visitor) {
    return visitor.apply(new FloorDivision(this.dividend.apply(visitor), this.divisor.apply(visitor), this.maxOutput,
        this.minOutput, this.errorDf.apply(visitor)));
  }

  @Override
  public DensityFunction dividend() {
    return dividend;
  }

  @Override
  public DensityFunction divisor() {
    return divisor;
  }

  @Override
  public DensityFunction errorDf() {
    return this.errorDf;
  }

  @Override
  public double minValue() {
    return Math.min(this.errorDf.minValue(), this.minOutput);
  }

  @Override
  public double maxValue() {
    return Math.max(this.errorDf.maxValue(), this.maxOutput);
  }

  @Override
  public CodecHolder<? extends DensityFunction> getCodec() {
    return CODEC;
  }
}
