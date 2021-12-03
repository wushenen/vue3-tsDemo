package com.qtec.unicom.component;

import com.qtec.unicom.component.annotation.MyAnnotation;

import java.lang.reflect.Method;


public class MyAnnotationProcessor {
	 
    public int getSequence(Method method) {
        if (method.isAnnotationPresent(MyAnnotation.class)) {
            MyAnnotation myAnnotation = (MyAnnotation) method.getAnnotation(MyAnnotation.class);
            return myAnnotation.value();
        }
        return 0;
    }
}
