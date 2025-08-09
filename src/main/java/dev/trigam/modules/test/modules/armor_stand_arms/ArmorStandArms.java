package dev.trigam.modules.test.modules.armor_stand_arms;

import dev.trigam.modules.BlockByBlock;
import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleEnvironment;
import dev.trigam.modules.module.ModuleInfo;

@ModuleInfo(
	modId = BlockByBlock.MOD_ID,
	moduleId = "armor_stand_arms",
	categoryId = "building",
	
	environment = ModuleEnvironment.SERVER
)
public class ArmorStandArms extends Module {
	@Override
	public void init () {
		this.getLogger().info( "Hey! I'm loaded!" );
	}
}

