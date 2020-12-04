package com.JMTRACE.Asm;

import com.JMTRACE.Mtracer.mtracer;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import javax.swing.*;
import java.io.PrintStream;

import static org.objectweb.asm.Opcodes.*;

public class JMtraceMethodAdapter extends MethodVisitor{
    private final String methodName;

    public JMtraceMethodAdapter(int api, MethodVisitor methodVisitor, int access, String name, String desc, String className) {
        super(api, methodVisitor);
        this.methodName = name;
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
                mtracer.traceStatic(mv, "R", owner + "." + name, descriptor);
                break;
            }
            case PUTSTATIC:  {
                /*
                    ...,value -->
                    ...,-->
                 */
                mtracer.traceStatic(mv, "W", owner + "." + name, descriptor);
                super.visitFieldInsn(opcode, owner, name, descriptor);
                break;
            }
            case GETFIELD:  {
                /*
                    ...,objectref -->
                    ...,value -->
                 */
                mtracer.traceField(mv, "R", owner + "." + name);
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
                mtracer.traceField(mv, "W", owner + "." + name);
                mv.visitInsn(POP);
                super.visitFieldInsn(opcode, owner, name, descriptor);
                break;
            }
            default: super.visitFieldInsn(opcode, owner, name, descriptor);  break;
        }

    }

    @Override
    public void visitInsn(int opcode) {
        switch (opcode){
//            case Opcodes.AALOAD:     System.out.println("visit AALOAD");     break;
//            case Opcodes.BALOAD:     System.out.println("visit BALOAD");     break;
//            case Opcodes.CALOAD:     System.out.println("visit CALOAD");     break;
//            case Opcodes.DALOAD:     System.out.println("visit DALOAD");     break;
//            case Opcodes.FALOAD:     System.out.println("visit FALOAD");     break;
//            case Opcodes.IALOAD:     System.out.println("visit IALOAD");     break;
//            case Opcodes.LALOAD:     System.out.println("visit LALOAD");     break;
//            case Opcodes.SALOAD:     System.out.println("visit SALOAD");     break;
//
//            case Opcodes.AASTORE:    System.out.println("visit AASTORE");    break;
//            case Opcodes.BASTORE:    System.out.println("visit BASTORE");    break;
//            case Opcodes.CASTORE:    System.out.println("visit CASTORE");    break;
//            case Opcodes.DASTORE:    System.out.println("visit DASTORE");    break;
//            case Opcodes.FASTORE:    System.out.println("visit FASTORE");    break;
//            case Opcodes.IASTORE:    System.out.println("visit IASTORE");    break;
//            case Opcodes.LASTORE:    System.out.println("visit LASTORE");    break;
//            case Opcodes.SASTORE:    System.out.println("visit SASTORE");    break;
            default:                                                         break;
        }
        super.visitInsn(opcode);
    }


    /*
        Opcode:
            new
            anewarray
     */
    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
        if(opcode == Opcodes.NEW){
            //System.out.println(type);
        }
        else if(opcode == Opcodes.ANEWARRAY){
            //System.out.println(type);
        }
    }


    /*
            Opcode:
                newarray
    */
    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
        if(opcode == Opcodes.NEWARRAY){
            switch (operand){
//                case Opcodes.T_BOOLEAN:     System.out.println("New Boolean array");        break;
//                case Opcodes.T_FLOAT:       System.out.println("New FLOAT array");          break;
//                case Opcodes.T_DOUBLE:      System.out.println("New DOUBLE array");         break;
//                case Opcodes.T_BYTE:        System.out.println("New BYTE array");           break;
//                case Opcodes.T_SHORT:       System.out.println("New SHORT array");          break;
//                case Opcodes.T_INT:         System.out.println("New INT array");            break;
//                case Opcodes.T_LONG:        System.out.println("New LONG array");           break;
                default:                                                                    break;
            }
        }
    }



    private static void hack(MethodVisitor mv, String msg) {
        mv.visitFieldInsn(
                GETSTATIC,
                Type.getInternalName(System.class),
                "out",
                Type.getDescriptor(PrintStream.class)
        );
        mv.visitLdcInsn(msg);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                Type.getInternalName(PrintStream.class),
                "println",
                "(Ljava/lang/String;)V",
                false
        );
    }
}
