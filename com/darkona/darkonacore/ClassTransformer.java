package com.darkona.darkonacore;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClassTransformer implements net.minecraft.launchwrapper.IClassTransformer {

	@Override
	public byte[] transform(String className, String whatever, byte[] bytes) {
		if(className.equals("nm")){
			return patchClassASM(className, bytes, true);
		}
		
		if(className.equals("net.minecraft.entity.Entity")){
			return patchClassASM(className, bytes, false);
		}
		
		return bytes;
	}


	private byte[] patchClassASM(String className, byte[] bytes, boolean obf) {
		try{
		String target;
		if(obf){
			target = "a";
			System.out.println("[DarkonaCore] Injecting poison in obfuscated environment >"+className+"<");
		}else{
			target = "onStruckByLightning";
			System.out.println("[DarkonaCore] Injecting poison in unobfuscated environment >"+className+"<");
		}
		
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		InsnList toInject = new InsnList();
		toInject.add(new LabelNode());
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/darkona/darkonacore/LightningStrikeEvent", "postMe", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/effect/EntityLightningBolt;)V"));
			
		for(MethodNode method : classNode.methods){
			if(method.name.equals(target)){
				System.out.println("[DarkonaCore] Method: " + method.name + " found. Injecting poison.");
				method.instructions.insert(toInject);
				break;
			}
		}
		

		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		System.out.println("[DarkonaCore] Poison injected. You've seen nothing.");
		
		return writer.toByteArray();
		
		}catch(Exception oops){
			oops.printStackTrace();
		}
		return bytes;
		
	}

}
