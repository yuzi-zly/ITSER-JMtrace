package com.JMTRACE.Asm;

import com.JMTRACE.Mtracer.mtracer;
import org.objectweb.asm.*;

import javax.swing.*;
import java.awt.*;
import java.io.LineNumberReader;
import java.io.PrintStream;

import static org.objectweb.asm.Opcodes.*;

public class JMtraceMethodAdapter extends MethodVisitor{
    public String methodname;

    public JMtraceMethodAdapter(int api, MethodVisitor methodVisitor, int access, String name, String desc) {
        super(api, methodVisitor);
        this.methodname = name;
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        switch (opcode){
            case GETSTATIC:  {
                /*
                    ...,-->
                    ...,value -->
                 */
                super.visitFieldInsn(opcode, owner, name, descriptor);
                mv.visitInsn(DUP);
                mv.visitLdcInsn("R");
                mv.visitLdcInsn(owner+"."+name);
                mv.visitMethodInsn(INVOKESTATIC, "com/JMTRACE/Mtracer/mtracer", "mtraceStatic", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", false);
                break;
            }
            case PUTSTATIC:  {
                /*
                    ...,value -->
                    ...,-->
                 */
                mv.visitInsn(DUP);
                mv.visitLdcInsn("W");
                mv.visitLdcInsn(owner+"."+name);
                mv.visitMethodInsn(INVOKESTATIC, "com/JMTRACE/Mtracer/mtracer", "mtraceStatic", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", false);
                super.visitFieldInsn(opcode, owner, name, descriptor);
                break;
            }
            case GETFIELD:  {
                /*
                    ...,objectref -->
                    ...,value -->
                */
                mv.visitInsn(DUP);
                mv.visitLdcInsn("R");
                mv.visitLdcInsn(owner+"."+name);
                mv.visitMethodInsn(INVOKESTATIC, "com/JMTRACE/Mtracer/mtracer", "mtraceField", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", false);
                super.visitFieldInsn(opcode, owner, name, descriptor);
                break;
            }
            case PUTFIELD: {
                /*
                    ...,objectref, value -->
                    ...,-->
                 */
                mv.visitInsn(DUP2);
                mv.visitInsn(POP);
                mv.visitLdcInsn("W");
                mv.visitLdcInsn(owner+"."+name);
                mv.visitMethodInsn(INVOKESTATIC, "com/JMTRACE/Mtracer/mtracer", "mtraceField", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", false);
                super.visitFieldInsn(opcode, owner, name, descriptor);
                break;
            }
            default: super.visitFieldInsn(opcode, owner, name, descriptor);  break;
        }

    }

    @Override
    public void visitInsn(int opcode) {
        if(mtracer.isALOAD(opcode)){
            /*
                ...,arrayref, index -->
                ...,value
             */
            mv.visitInsn(DUP2);
            mv.visitLdcInsn("R");
            mv.visitMethodInsn(INVOKESTATIC, "com/JMTRACE/Mtracer/mtracer", "mtraceALOAD", "(Ljava/lang/Object;ILjava/lang/String;)V", false);
        }
        else if(mtracer.isASTORE(opcode)){
            /*
                ...,arrayref, index, value -->
                ...,-->
             */
            mv.visitInsn(DUP_X2);
            mv.visitInsn(POP);
            mv.visitInsn(DUP2_X1);
            mv.visitLdcInsn("W");
            mv.visitMethodInsn(INVOKESTATIC, "com/JMTRACE/Mtracer/mtracer", "mtraceASTORE", "(Ljava/lang/Object;ILjava/lang/String;)V", false);
        }
        super.visitInsn(opcode);
    }

}
