package dev.trigam.modules.module;

import net.fabricmc.api.EnvType;

// https://github.com/constellation-mc/andromeda/blob/2.0.0-1.20-fabric/src/main/java/me/melontini/andromeda/bootstrap/util/Environment.java
public enum ModuleEnvironment {

	CLIENT,
	SERVER,
	
	ANY {
		@Override
		public boolean allows ( ModuleEnvironment environment ) {
			return true;
		}
	};
	
	public boolean allows ( EnvType environment ) {
		return this.allows( environment == EnvType.CLIENT ? CLIENT : SERVER );
	}
	
	public boolean allows ( ModuleEnvironment environment ) {
		return this == environment;
	}

}
