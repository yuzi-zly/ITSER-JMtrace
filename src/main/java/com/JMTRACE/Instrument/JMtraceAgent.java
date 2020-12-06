package com.JMTRACE.Instrument;

import com.JMTRACE.Asm.JMtraceClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;


public class JMtraceAgent{
    //for idea
    private static final String PATH = "./output/";

    public static void premain(String agentArgs, Instrumentation inst)  {
        Class<?>[] cLasses = inst.getAllLoadedClasses();
        inst.addTransformer(new DefineTransformer(), true);
    }

    static class DefineTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            if(className.length() >= 5){
                if (className.startsWith("java/") || className.startsWith("jdk/") || className.startsWith("sun/")) {
                    return classfileBuffer;
                }
            }

            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            ClassVisitor classVisitor = new JMtraceClassAdapter(Opcodes.ASM9, classWriter);
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            byte[] result = classWriter.toByteArray();

            File file = new File(PATH + className + ".class");
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(result);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return classWriter.toByteArray();
        }
    }
}
