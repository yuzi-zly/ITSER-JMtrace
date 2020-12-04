package com.JMTRACE.Instrument;

import com.JMTRACE.Asm.JMtraceClassAdapter;
import org.objectweb.asm.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;


public class JMtraceAgent{
    private static final String PATH = "../";

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

            File file = new File(PATH + "Output.class");
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
