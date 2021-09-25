package com.unknown.xg42.mixin.client;

import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { AbstractClientPlayer.class }, priority = 2147483646)
public class MixinAbstractClientPlayer extends MixinPlayer
{

}