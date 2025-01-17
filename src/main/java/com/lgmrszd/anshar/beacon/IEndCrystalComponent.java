package com.lgmrszd.anshar.beacon;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

import static com.lgmrszd.anshar.Anshar.MOD_ID;

public interface IEndCrystalComponent extends ServerTickingComponent, ClientTickingComponent, AutoSyncedComponent {
    ComponentKey<IEndCrystalComponent> KEY = ComponentRegistry.getOrCreate(
            Identifier.of(MOD_ID, "end_crystal"), IEndCrystalComponent.class
    );

    Vec3d getPos();

    boolean onCrystalDamage(DamageSource source);

    void setBeacon(BlockPos pos);

    Optional<BlockPos> getConnectedBeacon();

    ActionResult onUse(PlayerEntity player, Hand hand);
}
