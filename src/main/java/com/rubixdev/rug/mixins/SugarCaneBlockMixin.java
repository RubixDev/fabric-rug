package com.rubixdev.rug.mixins;

import com.rubixdev.rug.RugSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin extends Block {
    @Shadow @Final public static IntProperty AGE;

    public SugarCaneBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "scheduledTick", at = @At("TAIL"))
    private void onScheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.canPlaceAt(world, pos) && RugSettings.zeroTickPlants && world.isAir(pos.up())) {
            int i;
            for (i = 1; world.getBlockState(pos.down(i)).getBlock() == this; ++i) {
            }

            if (i < 3) {
                int j = state.get(AGE);
                if (j == 15) {
                    world.setBlockState(pos.up(), this.getDefaultState());
                    world.setBlockState(pos, state.with(AGE, 0), 4);
                } else {
                    world.setBlockState(pos, state.with(AGE, j + 1), 4);
                }
            }
        }
    }
}
