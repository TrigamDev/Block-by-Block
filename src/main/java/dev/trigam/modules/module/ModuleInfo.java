package dev.trigam.modules.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention( RetentionPolicy.RUNTIME )
public @interface ModuleInfo {
	ModuleIdentifier moduleId();
	
	ModuleEnvironment environment() default ModuleEnvironment.ANY;
	boolean enabled = true;
}