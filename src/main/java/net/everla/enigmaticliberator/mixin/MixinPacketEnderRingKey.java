package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraftforge.network.NetworkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

/**
 * Mixin into PacketEnderRingKey to disable Ender Ring functionality from Cursed Ring
 * when the blessing is disabled in config.
 */
@Mixin(value = PacketEnderRingKey.class, remap = false)
public abstract class MixinPacketEnderRingKey {

    /**
     * Intercept the packet handler to prevent Ender Chest opening
     * when Ender Ring blessing is disabled
     */
    @Inject(method = "handle", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onHandlePacket(PacketEnderRingKey msg, Supplier<NetworkEvent.Context> ctx, CallbackInfo ci) {
        // If Ender Ring blessing is disabled, check if player only has Cursed Ring
        if (!BlessingConfig.ENDER_RING_ENABLED.get()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();

                // Only cancel if player has Cursed Ring but NOT Ender Ring
                // (If they have actual Ender Ring, let it work)
                boolean hasCursedRing = SuperpositionHandler.hasCurio(player, EnigmaticItems.CURSED_RING);
                boolean hasEnderRing = SuperpositionHandler.hasCurio(player, EnigmaticItems.ENDER_RING);

                if (hasCursedRing && !hasEnderRing) {
                    // Cancel the packet - don't open Ender Chest
                    ctx.get().setPacketHandled(true);
                    ci.cancel();
                    return;
                }
            });
        }
        // If blessing is enabled or player has actual Ender Ring, let original code run
    }
}
