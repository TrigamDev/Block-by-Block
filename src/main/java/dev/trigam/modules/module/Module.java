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
		
		this.logger = LoggerFactory.getLogger( this.getModuleIdentifier().toString() );
	}
	
	public abstract void init();
	
	public ModuleInfo getModuleInfo () {
		return moduleInfo;
	}
	
	public Identifier getModuleIdentifier () {
		return Module.getModuleIdentifier( this.moduleInfo );
	}
	public static Identifier getModuleIdentifier ( ModuleInfo moduleInfo ) {
		return Identifier.of( moduleInfo.moduleId().modId(), moduleInfo.moduleId().modulePath() );
	}
	
	public Logger getLogger () {
		return this.logger;
	}
	
	public String toString () {
		return String.format( "Module{%s}", this.getModuleIdentifier() );
	}
}
