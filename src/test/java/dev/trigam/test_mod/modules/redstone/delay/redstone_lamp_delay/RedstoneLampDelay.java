package dev.trigam.test_mod.modules.redstone.delay.redstone_lamp_delay;

import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleEnvironment;
import dev.trigam.modules.module.ModuleIdentifier;
import dev.trigam.modules.module.ModuleInfo;
import dev.trigam.test_mod.TestMod;

@ModuleInfo(
	moduleId = @ModuleIdentifier(
		modId = TestMod.MOD_ID,
		modulePath = "redstone/delay/redstone_lamp_delay"
	),
	
	environment = ModuleEnvironment.SERVER
)
public class RedstoneLampDelay extends Module {
	@Override
	public void init () {
		this.getLogger().info( "Hey! I'm loaded!" );
	}
}