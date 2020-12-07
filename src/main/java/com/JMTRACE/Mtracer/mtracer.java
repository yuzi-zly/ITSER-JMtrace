package com.JMTRACE.Mtracer;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class mtracer {
    public static boolean isALOAD(int opcode){
        return opcode == AALOAD || opcode == BALOAD || opcode == CALOAD || opcode == DALOAD
                || opcode == FALOAD || opcode == IALOAD || opcode == LALOAD || opcode == SALOAD;
    }

    public static boolean isOtherASTORE(int opcode){
        return opcode == AASTORE || opcode == BASTORE || opcode == CASTORE
                || opcode == FASTORE || opcode == IASTORE || opcode == SASTORE;
    }

    private static synchronized void outputType(String typestr){
        if(typestr.length() == 1){
            switch (typestr){
                case "Z" :     System.out.print("boolean");    break;
                case "C" :     System.out.print("char");       break;
                case "B" :     System.out.print("byte");       break;
                case "S" :     System.out.print("short");      break;
                case "I" :     System.out.print("int");        break;
                case "F" :     System.out.print("float");      break;
                case "J" :     System.out.print("long");       break;
                case "D" :     System.out.print("double");     break;
                default  :                                     break;
            }
        }
        else {
            System.out.print(typestr.substring(1, typestr.length() - 1));
        }
    }

    public static synchronized void mtraceStatic(String R_W, String name){
        System.out.printf("%-5s", R_W);
        String idstr = String.format("%019d",Thread.currentThread().getId());
        System.out.printf("%-23s", idstr);
        System.out.printf("%-12s", Integer.toHexString(name.hashCode()));
        System.out.printf("%s\n", name);
    }

    public static synchronized void mtraceField(Object obj, String R_W, String name){
        System.out.printf("%-5s", R_W);
        String idstr = String.format("%019d",Thread.currentThread().getId());
        System.out.printf("%-23s", idstr);
        System.out.printf("%-12s", Integer.toHexString(System.identityHashCode(obj)));
        System.out.printf("%s\n", name);
    }

    public static synchronized void mtraceALOAD(Object obj, int index, String R_W){
        System.out.printf("%-5s", R_W);
        String idstr = String.format("%019d",Thread.currentThread().getId());
        System.out.printf("%-23s", idstr);
        System.out.printf("%-12s", Integer.toHexString(System.identityHashCode(obj)));

        String info = obj.toString();
        int dimension = info.lastIndexOf('[');
        int hash_header = info.indexOf('@');
        String typestr = info.substring(dimension + 1, hash_header);
        outputType(typestr);
        System.out.print("[" + index + "]");
        for(int i = 0; i < dimension; ++i)
            System.out.print("[]");
        System.out.println();
    }

    public static synchronized void mtraceASTORE(Object obj, int index, String R_W){
        System.out.printf("%-5s", R_W);
        String idstr = String.format("%019d",Thread.currentThread().getId());
        System.out.printf("%-23s", idstr);
        System.out.printf("%-12s", Integer.toHexString(System.identityHashCode(obj)));

        String info = obj.toString();
        int dimension = info.lastIndexOf('[');
        int hash_header = info.indexOf('@');
        String typestr = info.substring(dimension + 1, hash_header);
        outputType(typestr);
        System.out.print("[" + index + "]");
        for(int i = 0; i < dimension; ++i)
            System.out.print("[]");
        System.out.println();
    }
}
