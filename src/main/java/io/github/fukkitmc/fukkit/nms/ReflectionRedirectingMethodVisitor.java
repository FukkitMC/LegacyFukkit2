package io.github.fukkitmc.fukkit.nms;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Remaps reflection and method handles
 */
public class ReflectionRedirectingMethodVisitor extends MethodVisitor {

    private static final Map<Member, Member> REMAPPED = new HashMap<>();

    public ReflectionRedirectingMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM8, methodVisitor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        Member m = REMAPPED.get(new Member(owner, name, descriptor));

        // TODO: Deal with custom ClassLoaders?
        if (m != null && (opcode == Opcodes.INVOKESTATIC || opcode == Opcodes.INVOKEVIRTUAL)) {
            super.visitMethodInsn(Opcodes.INVOKESTATIC, m.owner, m.name, m.descriptor, false);
        } else {
            if (opcode == Opcodes.INVOKESTATIC && "java/lang/Class".equals(owner) && "forName".equals(name) && "(Ljava/lang/String;)Ljava/lang/Class;".equals(descriptor)) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "io/github/fukkitmc/fukkit/nms/ReflectionRemapper", "mapClassName", "(Ljava/lang/String;)Ljava/lang/String;", false);
            }

            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    static {
        register("java/lang/Package", "getName", "()Ljava/lang/String;", "package_getName", "(Ljava/lang/Package;)Ljava/lang/String;");
        register("java/lang/Class", "getName", "()Ljava/lang/String;", "class_getName", "(Ljava/lang/Class;)Ljava/lang/String;");
        register("java/lang/Class", "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;", "class_forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;");
        register("java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", "classloader_loadClass", "(Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/lang/Class;");
        register("java/lang/Class", "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", "class_getField", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;");
        register("java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", "class_getDeclaredField", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;");
        register("java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", "class_getMethod", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
        register("java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", "class_getDeclaredMethod", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
        register("java/lang/invoke/MethodHandles$Lookup", "findGetter", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;", "lookup_findGetter", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;");
        register("java/lang/invoke/MethodHandles$Lookup", "findStaticGetter", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;", "lookup_findStaticGetter", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;");
        register("java/lang/invoke/MethodHandles$Lookup", "findSetter", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;", "lookup_findSetter", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;");
        register("java/lang/invoke/MethodHandles$Lookup", "findStaticSetter", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;", "lookup_findStaticSetter", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;");
        register("java/lang/invoke/MethodHandles$Lookup", "findVirtual", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;", "lookup_findVirtual", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;");
        register("java/lang/invoke/MethodHandles$Lookup", "findStatic", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;", "lookup_findStatic", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;");
        register("java/lang/invoke/MethodHandles$Lookup", "findSpecial", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;", "lookup_findSpecial", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;)Ljava/lang/invoke/MethodHandle;");
    }

    private static void register(String owner, String name, String descriptor, String newName, String newDescriptor) {
        REMAPPED.put(new Member(owner, name, descriptor), new Member("io/github/fukkitmc/fukkit/nms/ReflectionRemapper", newName, newDescriptor));
    }
}
