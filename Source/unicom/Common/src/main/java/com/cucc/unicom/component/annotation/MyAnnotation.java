package com.cucc.unicom.component.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
 
    int value() default 0;
 
}
