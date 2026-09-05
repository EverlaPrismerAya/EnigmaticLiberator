package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.EnigmaticAmulet;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class)
public abstract class MixinPlayer {
    @Inject(
        method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At("HEAD"), remap = false, require = 0
    )
    private void rerollAmuletOnSneakDropOfficial(ItemStack stack, boolean randomThrow, boolean retainOwnership,
                                                 CallbackInfoReturnable<ItemEntity> cir) {
        rerollAmuletOnSneakDrop(stack);
    }

    @Inject(method = "m_7197_", at = @At("HEAD"), remap = false, require = 0)
    private void rerollAmuletOnSneakDropSrg(ItemStack stack, boolean randomThrow,
                                            CallbackInfoReturnable<ItemEntity> cir) {
        rerollAmuletOnSneakDrop(stack);
    }

    private void rerollAmuletOnSneakDrop(ItemStack stack) {
        Player player = (Player) (Object) this;
        if (ExtraConfig.AMULET_REROLL_ON_SNEAK_DROP.get()
                && !player.level().isClientSide
                && player.isCrouching()
                && stack.is(EnigmaticItems.ENIGMATIC_AMULET)) {
            ((EnigmaticAmulet) EnigmaticItems.ENIGMATIC_AMULET).setRandomColor(stack);
        }
    }
}
