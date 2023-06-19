package com.lkd.emq;

import java.lang.annotation.*;

/**
 * 主题
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Topic {
    String value();
}
