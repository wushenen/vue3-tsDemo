package com.qtec.unicom.component.jni;

public class ProbationJNI {
    public native int nativeProbation();
    public native void nativeInfoWrite(int year, int month, int day);

    static {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary("probation");
    }

    public int getN(){
        int ret = new ProbationJNI().nativeProbation();

        System.out.println(ret);
        return ret;
    }

    public static void main(String[] args){
        int ret = new ProbationJNI().nativeProbation();

        if(ret == 1){
            System.out.println("In date\n");
        }else{
            System.out.println("Out of date\n");
        }
    }
}
