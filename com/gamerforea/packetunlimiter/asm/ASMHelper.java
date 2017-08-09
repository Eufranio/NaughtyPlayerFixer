package com.gamerforea.packetunlimiter.asm;

import com.gamerforea.packetunlimiter.CoreMod;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public final class ASMHelper {
   private static final ImmutableMap methods;

   public static String getMethod(String method) {
      return CoreMod.isObfuscated?(String)methods.get(method):method.substring(method.lastIndexOf(46) + 1);
   }

   static {
      Builder builderMethods = ImmutableMap.builder();
      builderMethods.put("net.minecraft.network.PacketBuffer.writeNBTTagCompoundToBuffer", "func_150786_a");
      builderMethods.put("net.minecraft.network.PacketBuffer.readNBTTagCompoundFromBuffer", "func_150793_b");
      methods = builderMethods.build();
   }
}
