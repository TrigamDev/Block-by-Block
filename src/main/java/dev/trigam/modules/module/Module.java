package dev.trigam.modules.module;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Module {

	private final ModuleInfo moduleInfo;
	private final Logger logger;
	
	public Module () {
		this.moduleInfo = this.getClass().getAnnotation( ModuleInfo.class );
		assert this.moduleInfo != null;
		
		this.logger = LoggerFactory.getLogger( this.getModulePath() );
	}
	
	public abstract void init();
	
	public ModuleInfo getModuleInfo () {
		return moduleInfo;
	}
	
	public String getModulePath () {
		String path = this.moduleInfo.modId() + "/";
		if ( !this.moduleInfo.categoryId().isEmpty() ) path += this.moduleInfo.categoryId() + "/";
		path += this.moduleInfo.moduleId();
		return path;
	}
	
	public Logger getLogger () {
		return this.logger;
	}
	
	public String toString () {
		return "Module{" + Identifier.of( this.moduleInfo.modId(), this.moduleInfo.moduleId() ) + "}";
	}
}
