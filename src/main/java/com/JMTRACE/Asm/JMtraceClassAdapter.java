package com.JMTRACE.Asm;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.awt.*;

public class JMtraceClassAdapter extends ClassVisitor {

    public JMtraceClassAdapter(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        //cv is writer
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        if(name.equals("mtraceStatic") || name.equals("mtraceField") || name.equals("isALOAD")
            || name.equals("isASTORE") || name.equals("mtraceALOAD") || name.equals("mtraceASTORE")
            || name.equals("outputType"))
            return methodVisitor;
        else
            return new JMtraceMethodAdapter(api, methodVisitor, access, name, desc);

    }
}
