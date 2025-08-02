package dev.trigam.modules.manager;

import dev.trigam.modules.BlockByBlock;
import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleInfo;
import net.minecraft.util.Identifier;


import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class ModuleManager {
	
	private final ModuleDiscovery moduleDiscovery;
	private final Map<Identifier, Module> loadedModules = new HashMap<>();
	
	public ModuleManager () {
		this.moduleDiscovery = new ModuleDiscovery();
	}
	
	public void loadModules () throws Exception {
		this.scanPackages();
		Map< Identifier, ModuleInfo > discoveredModules = this.moduleDiscovery.getDiscoveredModules();
		
		// Status message
		String moduleList = buildModuleList( discoveredModules.keySet() );
		BlockByBlock.MANAGER_LOGGER.info( "Loading {} module{}:\n{}",
			discoveredModules.size(),
			discoveredModules.size() != 1 ? "s" : "",
			moduleList
		);
		
		// Loop through each discovered module and load it
		for ( Map.Entry< Identifier, ModuleInfo > discoveredModule : discoveredModules.entrySet() ) {
			loadModule( discoveredModule.getKey(), discoveredModule.getValue() );
		}
	}
	
	// Replace with a method of finding each mod's
	// modules package and scan each of them
	private void scanPackages () throws Exception {
		this.moduleDiscovery.scan( "dev.trigam.modules.test.modules" );
	}
	
	private void loadModule ( Identifier moduleId, ModuleInfo moduleInfo ) throws Exception {
		Map< Identifier, Class<? extends Module>> moduleClasses = this.moduleDiscovery.getModuleClasses();
		
		Class<? extends Module> moduleClass = moduleClasses.get( moduleId );
		try {
			// Create instance of module class and initialize
			Module module = moduleClass.getDeclaredConstructor().newInstance();
			module.init();
			
			this.loadedModules.put( moduleId, module );
			BlockByBlock.MANAGER_LOGGER.info( "Loaded module {}", moduleId );
		} catch ( Exception exception ) {
			throw new Exception( String.format(
				"Failed to load module %s %s", moduleId.toString(), exception
			) );
		}
	}
	
	// Create a bulleted list of each module id
	private String buildModuleList ( Set<Identifier> moduleIds ) {
		StringBuilder moduleList = new StringBuilder();
		for ( Identifier moduleId : moduleIds ) {
			if ( !moduleList.isEmpty() ) moduleList.append( "\n" );
			moduleList.append( String.format( "\t- %s", moduleId ) );
		}
		return moduleList.toString();
	}
}
