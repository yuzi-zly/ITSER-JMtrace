package com.JMTRACE.Asm;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class JMtraceClassAdapter extends ClassVisitor {
    private String className;

    public JMtraceClassAdapter(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        //cv is writer
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        return new JMtraceMethodAdapter(api, methodVisitor, access, name, desc, this.className);

    }
}
