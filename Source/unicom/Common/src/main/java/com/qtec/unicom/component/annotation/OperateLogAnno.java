package com.qtec.unicom.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by duhc on 2017/10/18.
 * 声明注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLogAnno {
     String operateDesc() default "默认操作";
     String operateModel() default "操作模块";
}
