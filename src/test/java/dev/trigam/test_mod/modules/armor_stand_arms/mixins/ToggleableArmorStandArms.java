package dev.trigam.test_mod.modules.armor_stand_arms.mixins;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ArmorStandEntity.class )
public abstract class ToggleableArmorStandArms extends LivingEntity {
	
	// (In place of config, will be added later)
	@Unique private static final boolean TOGGLEABLE_ARMS = true;
	@Unique private static final int STICK_COST = 2;

	public ToggleableArmorStandArms( EntityType<? extends ArmorStandEntity> type, World world ) {
		super( type, world );
	}
	
	@Shadow public abstract void setShowArms ( boolean showArms );
	@Shadow public abstract boolean isMarker ();
	@Shadow public abstract boolean shouldShowArms ();
	
	@Inject(
		method = "interactAt",
		at = @At( value = "HEAD" ),
		cancellable = true
	)
	public void toggleArms ( PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> callback ) {
		if ( isMarker() || !TOGGLEABLE_ARMS ) return;
		if ( player.isSneaking() ) return;
		
		ItemStack heldStack = player.getStackInHand( hand );
		
		// Add arms
		if ( heldStack.isOf( Items.STICK ) && heldStack.getCount() > STICK_COST && !shouldShowArms() ) {
			equipArms( player, heldStack );
			callback.setReturnValue( ActionResult.SUCCESS );
		}
		// Remove arms
		else if ( heldStack.isOf( Items.SHEARS ) && shouldShowArms() ) {
			removeArms( player, heldStack, hand );
			callback.setReturnValue( ActionResult.SUCCESS );
		}
	}
	
	@Unique
	public void equipArms ( PlayerEntity player, ItemStack heldStack ) {
		setShowArms( true );
		heldStack.decrementUnlessCreative( STICK_COST, player );
		
		player.playSound( SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(), 1F, 1F );
	}
	
	@Unique
	public void removeArms ( PlayerEntity player, ItemStack heldStack, Hand hand ) {
		setShowArms( false );
		heldStack.damage( 1, player, LivingEntity.getSlotForHand( hand ) );
		
		ItemStack droppedStick = Items.STICK.getDefaultStack();
		World world = this.getWorld();
		BlockPos up = this.getBlockPos().up();
		
		for ( int i = 0; i < STICK_COST; i++ ) {
			Block.dropStack( world, up, droppedStick );
		}
		
		// Drop hand items
		EquipmentSlot[] handSlots = { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND };
		for ( EquipmentSlot slot : handSlots ) {
			ItemStack handStack = this.equipment.put( slot, ItemStack.EMPTY );
			if ( !handStack.isEmpty() ) {
				Block.dropStack( world, up, handStack );
			}
		}
	}
	

}
