package etu1784.framework.annotation;


import etu1784.framework.type.ScopeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
    ScopeType type() default ScopeType.NON_SINGLETON;
}
