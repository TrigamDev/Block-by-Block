package dev.trigam.modules.manager;

import dev.trigam.modules.exception.DiscoveryException;
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

	private final Map< Identifier, DiscoveredModule> discoveredModules = new HashMap<>();
	
	public void scan ( String packageName ) {
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
			}
		} catch ( Exception exception ) {
			throw new DiscoveryException( "Failed to scan for modules", exception );
		}
	}
	
	public Map< Identifier, DiscoveredModule> getDiscoveredModules () {
		return this.discoveredModules;
	}
	
	public record DiscoveredModule(
		Identifier moduleId,
		ModuleInfo moduleInfo,
		Class< ? extends Module > moduleClass
	) {}
}