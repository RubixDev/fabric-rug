package com.rubixdev.rug.mixins;

import com.rubixdev.rug.RugSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends Entity {
    public EnderDragonEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "updatePostDeath",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;awardExperience(I)V",
                    ordinal = 1))
    private void onUpdatePostDeath(CallbackInfo ci) {
        String rugSetting = RugSettings.dragonDrops;
        if (!rugSetting.equals("none") && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            if (rugSetting.equals("elytra") || rugSetting.equals("both")) {
                this.dropStack(new ItemStack(Items.ELYTRA));
            }
            if (rugSetting.equals("dragon_egg") || rugSetting.equals("both")) {
                this.dropStack(new ItemStack(Items.DRAGON_EGG));
            }
        }
    }

    @ModifyConstant(method = "updatePostDeath", constant = @Constant(intValue = 500))
    private int overwriteXP(final int baseValue) {
        return RugSettings.dragonXpDrop;
    }
}
