package com.JMTRACE.Mtracer;

import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class mtracer {

    private static void getObjectTypeHashCode(MethodVisitor mv){
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "identityHashCode", "(Ljava/lang/Object;)I", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "toHexString", "(I)Ljava/lang/String;", false);
        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;",
                new Handle(H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), "\u0001 ");
    }

    private static void getBaseTypeHashcode(MethodVisitor mv, String descriptor){
        switch (descriptor){
            case "Z":
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                break;
            case "C":
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Char", "valueOf", "(C)Ljava/lang/Char;", false);
                break;
            case "B":
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                break;
            case "S":
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                break;
            case "I":
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                break;
            case "F":
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                break;
            case "J":
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                break;
            case "D":
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                break;
            default: System.err.println("jmtrace: Error 'Unknow Base descriptor " + descriptor + "'."); break;
        }
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "identityHashCode", "(Ljava/lang/Object;)I", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "toHexString", "(I)Ljava/lang/String;", false);
        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;",
                new Handle(H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), "\u0001 ");
    }

    public static void traceStatic(MethodVisitor mv, String R_W, String ownername, String descriptor){
        /*  打印W，ThreadID */
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(J)Ljava/lang/String;",
                new Handle(H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), R_W+" \u0001 ");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

        /* 打印 hashcode */
        mv.visitInsn(DUP);
        if(descriptor.startsWith("[") || descriptor.startsWith("L")){
            /* Arrays or Object */
            getObjectTypeHashCode(mv);
        }
        else{
            /* Base Type */
            getBaseTypeHashcode(mv, descriptor);
        }
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitInsn(DUP_X1);
        mv.visitInsn(POP);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

        /* 打印 ownername */
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(ownername);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }


    public static void traceField(MethodVisitor mv, String R_W, String ownername){
        /*  打印R_W，ThreadID */
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(J)Ljava/lang/String;",
                new Handle(H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), R_W+" \u0001 ");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

        /* 打印 hashcode */
        mv.visitInsn(DUP);
        getObjectTypeHashCode(mv);
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitInsn(DUP_X1);
        mv.visitInsn(POP);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

        /* 打印 ownername */
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(ownername);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
}
