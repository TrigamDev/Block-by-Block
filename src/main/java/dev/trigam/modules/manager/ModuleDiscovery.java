package dev.trigam.modules.manager;

import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleInfo;
import net.minecraft.util.Identifier;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModuleDiscovery {

	private final Map< Identifier, ModuleInfo> discoveredModules = new HashMap<>();
	private final Map< Identifier, Class<? extends Module>> moduleClasses = new HashMap<>();
	
	public void scan ( String packageName ) throws Exception {
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
					throw new Exception( String.format( "%s is annotated with @ModuleInfo, but does not extend Module",
						moduleClass.getName()
					) );
				}
				
				ModuleInfo annotation = moduleClass.getAnnotation( ModuleInfo.class );
				discoveredModules.put( Module.getModuleIdentifier( annotation ), annotation );
				moduleClasses.put( Module.getModuleIdentifier( annotation ), moduleClass.asSubclass( Module.class ) );
			}
			
		} catch ( Exception exception ) {
			throw new Exception( "Failed to scan for modules", exception );
		}
	}
	
	public Map< Identifier, ModuleInfo> getDiscoveredModules () {
		return this.discoveredModules;
	}
	
	public Map< Identifier, Class<? extends Module>> getModuleClasses () {
		return this.moduleClasses;
	}

}
