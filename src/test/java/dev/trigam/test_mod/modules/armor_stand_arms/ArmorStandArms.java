package dev.trigam.test_mod.modules.armor_stand_arms;

import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleEnvironment;
import dev.trigam.modules.module.ModuleIdentifier;
import dev.trigam.modules.module.ModuleInfo;
import dev.trigam.test_mod.TestMod;

@ModuleInfo(
	moduleId = @ModuleIdentifier(
		modId = TestMod.MOD_ID,
		modulePath = "building/armor_stand/armor_stand_arms"
	),
	
	environment = ModuleEnvironment.SERVER
)
public class ArmorStandArms extends Module {
	@Override
	public void init () {
		this.getLogger().info( "Hey! I'm loaded!" );
	}
}