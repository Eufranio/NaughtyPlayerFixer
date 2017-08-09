package com.gamerforea.packetunlimiter.asm;

import com.gamerforea.packetunlimiter.CoreMod;
import com.gamerforea.packetunlimiter.asm.ASMHelper;
import java.util.Iterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public final class ASMTransformer implements IClassTransformer, Opcodes {
   public byte[] transform(String name, String transformedName, byte[] basicClass) {
      return transformedName.equals("net.minecraft.network.PacketBuffer")?patchPacketBuffer(basicClass):basicClass;
   }

   private static byte[] patchPacketBuffer(byte[] basicClass) {
      ClassNode cNode = new ClassNode();
      (new ClassReader(basicClass)).accept(cNode, 0);
      String writeNbt = ASMHelper.getMethod("net.minecraft.network.PacketBuffer.writeNBTTagCompoundToBuffer");
      String readNbt = ASMHelper.getMethod("net.minecraft.network.PacketBuffer.readNBTTagCompoundFromBuffer");
      Iterator cWriter = cNode.methods.iterator();

      while(true) {
         while(cWriter.hasNext()) {
            MethodNode mNode = (MethodNode)cWriter.next();
            InsnList insn;
            if(mNode.name.equals(writeNbt) && mNode.desc.equals("(Lnet/minecraft/nbt/NBTTagCompound;)V")) {
               if(!CoreMod.writeNbtLimit) {
                  insn = new InsnList();
                  insn.add(new VarInsnNode(25, 0));
                  insn.add(new VarInsnNode(25, 1));
                  insn.add(new MethodInsnNode(184, "com/gamerforea/packetunlimiter/asm/PacketBufferPatch", "writeNBTTagCompoundToBuffer", "(Lnet/minecraft/network/PacketBuffer;Lnet/minecraft/nbt/NBTTagCompound;)V", false));
                  insn.add(new InsnNode(177));
                  mNode.instructions = insn;
               }
            } else if(mNode.name.equals(readNbt) && mNode.desc.equals("()Lnet/minecraft/nbt/NBTTagCompound;") && !CoreMod.readNbtLimit) {
               insn = new InsnList();
               insn.add(new VarInsnNode(25, 0));
               insn.add(new MethodInsnNode(184, "com/gamerforea/packetunlimiter/asm/PacketBufferPatch", "readNBTTagCompoundFromBuffer", "(Lnet/minecraft/network/PacketBuffer;)Lnet/minecraft/nbt/NBTTagCompound;", false));
               insn.add(new InsnNode(176));
               mNode.instructions = insn;
            }
         }

         ClassWriter cWriter1 = new ClassWriter(1);
         cNode.accept(cWriter1);
         return cWriter1.toByteArray();
      }
   }
}
