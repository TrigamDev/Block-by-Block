package dev.trigam.modules;

import dev.trigam.modules.manager.ModuleManager;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockByBlock implements ModInitializer {
	
	public static final String MOD_ID = "block-by-block";
	
	public static final Logger LOGGER = LoggerFactory.getLogger( "Block by Block" );
	public static final Logger MANAGER_LOGGER = LoggerFactory.getLogger( "Block by Block/Module Manager" );
	
	private ModuleManager moduleManager;

	@Override
	public void onInitialize() {
		this.moduleManager = new ModuleManager();
		
		// Initialize modules
		try { this.moduleManager.loadModules(); }
		catch ( Exception e ) { throw new RuntimeException( e ); }
	}
}