package dev.trigam.test_mod.modules.redstone.block_state_textures;

import dev.trigam.modules.module.Module;
import dev.trigam.modules.module.ModuleEnvironment;
import dev.trigam.modules.module.ModuleIdentifier;
import dev.trigam.modules.module.ModuleInfo;
import dev.trigam.test_mod.TestMod;

@ModuleInfo(
	moduleId = @ModuleIdentifier(
		modId = TestMod.MOD_ID,
		modulePath = "redstone/block_state_textures"
	),
	
	environment = ModuleEnvironment.SERVER
)
public class BlockStateTextures extends Module {
	@Override
	public void init () {
		this.getLogger().info( "Hey! I'm loaded!" );
	}
}