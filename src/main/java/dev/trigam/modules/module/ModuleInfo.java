package dev.trigam.modules.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention( RetentionPolicy.RUNTIME )
public @interface ModuleInfo {
	String modId();
	String moduleId();
	String categoryId() default "";
	
	ModuleEnvironment environment() default ModuleEnvironment.ANY;
	boolean enabled = true;
}