package dev.trigam.test_mod.modules.redstone.delay.fishy_piston;

import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleEnvironment;
import dev.trigam.modules.module.ModuleIdentifier;
import dev.trigam.modules.module.ModuleInfo;

@ModuleInfo(
	moduleId = @ModuleIdentifier(
		modId = "another-mod",
		modulePath = "redstone/fishy_piston"
	),
	
	environment = ModuleEnvironment.SERVER
)
public class FishyPiston extends Module {
	@Override
	public void init () {
		this.getLogger().info( "Hey! I'm loaded!" );
	}
}