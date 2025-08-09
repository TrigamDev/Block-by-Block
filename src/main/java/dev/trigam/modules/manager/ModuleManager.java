package dev.trigam.modules.manager;

import dev.trigam.modules.BlockByBlock;
import dev.trigam.modules.exception.LoadException;
import dev.trigam.modules.manager.ModuleDiscovery.DiscoveredModule;
import dev.trigam.modules.module.Module;
import net.minecraft.util.Identifier;

import java.util.*;

public class ModuleManager {
	
	private final ModuleDiscovery moduleDiscovery;
	private final HashMap<Identifier, Module> loadedModules = new HashMap<>();
	
	public ModuleManager () {
		this.moduleDiscovery = new ModuleDiscovery();
	}
	
	public void loadModules () {
		this.moduleDiscovery.scanMods();
		Map<Identifier, DiscoveredModule> discoveredModules = this.moduleDiscovery.getDiscoveredModules();
		
		// Status message
		BlockByBlock.MANAGER_LOGGER.info( "Loading {} module{}...",
			discoveredModules.size(),
			discoveredModules.size() != 1 ? "s" : ""
		);
		
		// Loop through each discovered module and load it
		for ( Map.Entry<Identifier, DiscoveredModule> discoveredModule : discoveredModules.entrySet() ) {
			loadModule( discoveredModule.getKey(), discoveredModule.getValue() );
		}
		
		// Status message
		BlockByBlock.MANAGER_LOGGER.info( "Loaded {} module{}:{}{}",
			discoveredModules.size(),
			discoveredModules.size() != 1 ? "s" : "",
			!discoveredModules.isEmpty() ? "\n" : "",
			this.getModulesAsTree()
		);
	}
	
	private void loadModule ( Identifier moduleId, DiscoveredModule discoveredModule ) {
		Class<? extends Module> moduleClass = discoveredModule.moduleClass();
		try {
			// Create instance of module class and initialize
			Module module = moduleClass.getDeclaredConstructor().newInstance();
			module.init();
			
			this.loadedModules.put( moduleId, module );
		} catch ( Exception exception ) {
			throw new LoadException( String.format(
				"Failed to load module %s %s", moduleId.toString(), exception
			) );
		}
	}
	
	private ModuleTree getModulesAsTree () {
		ModuleTree moduleTree = new ModuleTree();
		
		for ( Map.Entry<Identifier, Module> moduleEntry : this.loadedModules.entrySet() ) {
			Identifier moduleId = moduleEntry.getKey();
			Module module = moduleEntry.getValue();
			
			this.addModuleToTree( moduleId, module, moduleTree );
		}
		
		return moduleTree;
	}
	private void addModuleToTree ( Identifier moduleId, Module module, ModuleTree moduleTree ) {
		// Either grab the existing root node, or make one if it doesn't exist
		Optional<ModuleTree.Node> modRootSearch = moduleTree.getRootByName( moduleId.getNamespace() );
		ModuleTree.Node modRoot;
		
		if ( modRootSearch.isPresent() ) modRoot = modRootSearch.get();
		else {
			modRoot = new ModuleTree.Node( moduleId.getNamespace(), Optional.empty() );
			moduleTree.addRoot( modRoot );
		}
		
		// Sequentially add path nodes
		ModuleTree.Node currentNode = modRoot;
		for ( String pathPart : moduleId.getPath().split( "/" ) ) {
			// Either grab the existing path node, or make one if it doesn't exist
			Optional<ModuleTree.Node> foundNode = currentNode.getChildByName( pathPart );
			if ( foundNode.isPresent() ) currentNode = foundNode.get();
			else {
				ModuleTree.Node pathNode = new ModuleTree.Node( pathPart, Optional.empty() );
				currentNode.addChild( pathNode );
				currentNode = pathNode;
			}
		}
		
		// Add the module data to the last path node
		currentNode.setValue( Optional.of( module ) );
	}
}
