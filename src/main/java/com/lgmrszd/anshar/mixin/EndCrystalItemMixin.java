package com.lgmrszd.anshar.mixin;

import java.util.List;

import com.lgmrszd.anshar.ModApi;
import com.lgmrszd.anshar.beacon.EndCrystalItemContainer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

@Mixin(EndCrystalItem.class)
public abstract class EndCrystalItemMixin extends Item {

    public EndCrystalItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void anshar$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci){
        // copy from: vanilla
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        BlockPos up = blockPos.up();
        
        if (!context.getPlayer().isSneaking() || !blockState.isOf(Blocks.ENDER_CHEST) || !world.isAir(up)) return;

        double d = (double)up.getX();
        double e = (double)up.getY();
        double f = (double)up.getZ();
        List<Entity> list = world.getOtherEntities((Entity)null, new Box(d, e, f, d + 1.0, e + 2.0, f + 1.0));
        if (!list.isEmpty()) {
            return;
        } else {
            if (world instanceof ServerWorld) {
                EndCrystalEntity endCrystalEntity = new EndCrystalEntity(world, d + 0.5, e, f + 0.5);
                endCrystalEntity.setShowBottom(false);
                world.spawnEntity(endCrystalEntity);
                world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, up);
            }

            context.getStack().decrement(1); 
            ci.setReturnValue(ActionResult.success(world.isClient));
            ci.cancel();
        }
    }

    @Intrinsic
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void anshar$addToTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        EndCrystalItemContainer container = ModApi.END_CRYSTAL_ITEM.find(stack, null);
        // TODO make prettier
        container.getBeaconPos().ifPresent(pos -> {
            tooltip.add(Text.literal(String.format(
                    "Stored position: %s",
                    pos
            )));
        });
    }
}
