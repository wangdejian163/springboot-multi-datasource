package com.example.demo.policestation.config.annotion;

import java.lang.annotation.*;

/**
 * <p>
 *     动态配置多数据源元注解
 * </p>
 *
 * @author wangdejian
 * @since 2018/3/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface DynamicSource {

    String value() default "";
}
