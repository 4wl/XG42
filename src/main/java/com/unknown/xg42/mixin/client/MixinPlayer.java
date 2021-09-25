package com.unknown.xg42.mixin.client;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityPlayer.class)
public class MixinPlayer extends MixinEntity {

}
