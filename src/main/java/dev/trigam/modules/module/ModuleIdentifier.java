package dev.trigam.modules.module;

import net.minecraft.util.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target( ElementType.ANNOTATION_TYPE )
public @interface ModuleIdentifier {
	String modId() default Identifier.DEFAULT_NAMESPACE;
	String modulePath() default "module";
}
