package dev.trigam.modules;

import dev.trigam.modules.test.modules.armor_stand_arms.ArmorStandArms;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockByBlock implements ModInitializer {
	
	public static final String MOD_ID = "block-by-block";
	public static final Logger LOGGER = LoggerFactory.getLogger( MOD_ID );

	@Override
	public void onInitialize() {
		
		new ArmorStandArms().init();
	
	}
}