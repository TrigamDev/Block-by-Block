package dev.trigam.modules.manager;

import dev.trigam.modules.BlockByBlock;
import dev.trigam.modules.exception.DiscoveryException;
import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.minecraft.util.Identifier;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.*;

public class ModuleDiscovery {

	private final Map<Identifier, DiscoveredModule> discoveredModules = new HashMap<>();
	public static final Identifier METADATA_MODULES_KEY = Identifier.of( BlockByBlock.MOD_ID, "modules" );
	
	public void scanMods () {
		BlockByBlock.MANAGER_LOGGER.info( "Scanning mods..." );
		Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
		mods.forEach( this::scanMod );
	}
	
	private void scanMod ( ModContainer modContainer ) {
		CustomValue modulesPackageField = modContainer.getMetadata().getCustomValue( METADATA_MODULES_KEY.toString() );
		if ( modulesPackageField == null ) return;
		
		String modulesPackage = modulesPackageField.getAsString();
		this.scanPackage( modulesPackage );
	}
	
	public void scanPackage ( String packageName ) {
		BlockByBlock.MANAGER_LOGGER.info(
			"Scanning for modules in {}...",
			packageName
		);
		
		int discoveredModulesCount = 0;
		
		try {
			Reflections reflections = new Reflections(
				new ConfigurationBuilder()
					.setUrls( ClasspathHelper.forPackage( packageName ) )
					.setScanners(
						new TypeAnnotationsScanner(),
						new SubTypesScanner( false )
					)
			);
			
			// Find annotated modules
			Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith( ModuleInfo.class );
			
			for ( Class<?> moduleClass : annotatedClasses ) {
				if ( !Module.class.isAssignableFrom( moduleClass ) ) {
					throw new DiscoveryException( String.format(
						"%s is annotated with @ModuleInfo, but does not extend Module",
						moduleClass.getName()
					) );
				}
				
				// Package up info and add it to the list of discovered modules
				ModuleInfo moduleInfo = moduleClass.getAnnotation( ModuleInfo.class );
				Identifier moduleId = Module.getModuleIdentifier( moduleInfo );
				DiscoveredModule discoveredModule = new DiscoveredModule(
					moduleId,
					moduleInfo,
					moduleClass.asSubclass( Module.class )
				);
				discoveredModules.put( moduleId, discoveredModule );
				discoveredModulesCount++;
			}
			
			BlockByBlock.MANAGER_LOGGER.info(
				"Discovered {} module{} in {}",
				discoveredModulesCount,
				discoveredModulesCount != 1 ? "s" : "",
				packageName
			);
		} catch ( Exception exception ) {
			throw new DiscoveryException( "Failed to scanPackage for modules", exception );
		}
	}
	
	public Map<Identifier, DiscoveredModule> getDiscoveredModules () {
		return this.discoveredModules;
	}
	
	public record DiscoveredModule(
		Identifier moduleId,
		ModuleInfo moduleInfo,
		Class<? extends Module> moduleClass
	) {}
}