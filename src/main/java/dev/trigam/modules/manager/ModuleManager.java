package dev.trigam.modules.manager;

import dev.trigam.modules.BlockByBlock;
import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleInfo;
import net.minecraft.util.Identifier;


import java.util.Map;
import java.util.HashMap;

public class ModuleManager {
	
	private final ModuleDiscovery moduleDiscovery;
	private final Map<Identifier, Module> loadedModules = new HashMap<>();
	
	public ModuleManager () {
		this.moduleDiscovery = new ModuleDiscovery();
	}
	
	public void initializeModules () throws Exception {
		this.moduleDiscovery.scan( "dev.trigam.modules.test.modules" );
		
		Map< Identifier, ModuleInfo > discoveredModules = this.moduleDiscovery.getDiscoveredModules();
		Map< Identifier, Class<? extends Module>> moduleClasses = this.moduleDiscovery.getModuleClasses();
		
		StringBuilder moduleList = new StringBuilder();
		for ( Identifier moduleIdentifier : discoveredModules.keySet() ) {
			if ( !moduleList.isEmpty() ) moduleList.append( "\n" );
			moduleList.append( String.format( "\t- %s", moduleIdentifier ) );
		}
		BlockByBlock.MANAGER_LOGGER.info( "Loading {} module{}:\n{}",
			discoveredModules.size(),
			discoveredModules.size() != 1 ? "s" : "",
			moduleList
		);
		
		for ( Map.Entry< Identifier, ModuleInfo > discoveredModule : discoveredModules.entrySet() ) {
			Identifier moduleIdentifier = discoveredModule.getKey();
			ModuleInfo moduleAnnotation = discoveredModule.getValue();
			Class<? extends Module> moduleClass = moduleClasses.get( moduleIdentifier );
			
			try {
				Module module = moduleClass.getDeclaredConstructor().newInstance();
				module.init();
				this.loadedModules.put( moduleIdentifier, module );
				
				BlockByBlock.MANAGER_LOGGER.info( "Loaded module {}", moduleIdentifier );
			} catch ( Exception exception ) {
				throw new Exception( String.format(
					"Failed to initialize module %s %s", moduleIdentifier.toString(), exception
				) );
			}
		}
	}
}
