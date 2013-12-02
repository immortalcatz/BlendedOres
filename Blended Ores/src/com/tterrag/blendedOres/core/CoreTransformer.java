package com.tterrag.blendedOres.core;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author fishtaco, tterrag
 * 
 */
@SuppressWarnings("unused")
public class CoreTransformer implements IClassTransformer
{

	private final String ORE_CLASS_NAME = "net.minecraft.block.BlockOre";
	private final String OBF_ORE_CLASS_NAME = "apo";
	private final String RENDER_METHOD_NAME = "getRenderType";
	private final String RENDER_METHOD_NAME_OBF = "func_71857_b";
	private final String RENDER_METHOD_NOTCH = "a";
	private final String RENDER_METHOD_DESC = "(Lnet/minecraft/world/IBlockAccess;III)Z";
	private final String OBF_NEW_RENDER_METHOD_DESC = "(Lbeu;F)V";
//	private final String NEW_FENCE_METHOD_DESC = "(Lnet/minecraft/world/IBlockAccess;III)Z";
//
//	private final String WALL_CLASS_NAME = "net.minecraft.block.BlockWall";
//	private final String OBF_WALL_CLASS_NAME = "arn";
//	private final String WALL_METHOD_NAME = "canConnectWallTo";
//	private final String WALL_METHOD_NAME_OBF = "func_82538_d";
//	private final String WALL_METHOD_NOTCH = "a";
//	private final String WALL_METHOD_DESC = "(Lnet/minecraft/world/IBlockAccess;III)Z";
//	private final String OBF_NEW_WALL_METHOD_DESC = "(Lbeu;F)V";
//	private final String NEW_WALL_METHOD_DESC = "(Lnet/minecraft/world/IBlockAccess;III)Z";

	@Override
	public byte[] transform(String arg0, String arg1, byte[] arg2)
	{
		if (arg1.compareTo(ORE_CLASS_NAME) == 0)
			return patchClassOre(arg1, arg2, true);
		return arg2;
	}

	private byte[] patchClassOre(String name, byte[] bytes, boolean isObfuscated)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		Iterator<MethodNode> methods = classNode.methods.iterator();
		while (methods.hasNext())
		{
			MethodNode m = methods.next();
			if ((m.name.equals(this.RENDER_METHOD_NAME) || m.name.equals(this.RENDER_METHOD_NAME_OBF) || m.name.equals(this.RENDER_METHOD_NOTCH))
					&& (m.desc.equals(this.RENDER_METHOD_DESC) || m.desc.equals(OBF_NEW_RENDER_METHOD_DESC)))
			{
				System.out.println("BEGINNING TRANSFORMATION!");
				for (int index = 0; index < m.instructions.size(); index++)
				{
					if (m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN)
					{

						LabelNode lmm1Node = new LabelNode(new Label());

						LabelNode jumpLabel = new LabelNode(new Label());

						// make new instruction list
						InsnList toInject = new InsnList();

						toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
						toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
						toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
						toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));

						toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "niel/mod/core/CoreMethods", "canConnectFenceTo", this.RENDER_METHOD_DESC));

						// toInject.add(new JumpInsnNode(Opcodes.IFNE,
						// lmm1Node));
						toInject.add(jumpLabel);
						toInject.add(lmm1Node);

						m.instructions.insert(m.instructions.get(index), toInject);
						System.out.println("TRANSFORMATION COMPLETE!");
						break;
					}
				}
			}
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		classNode.accept(cw);

		return cw.toByteArray();
	}
}