package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.AbyssalHeart;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Mixin to disable Abyssal Heart when configured
 * Note: Abyssal Heart is primarily a curio that provides passive effects
 * We can't directly disable its curio effects without mixining into the curio handler,
 * but we can at least mark it as disabled in tooltip
 */
@Mixin(value = AbyssalHeart.class, remap = false)
public abstract class MixinAbyssalHeart {

    /**
     * Add a warning tooltip when Abyssal Heart is disabled
     */
    @Inject(method = "appendHoverText", at = @At("TAIL"), remap = false)
    private void addDisabledTooltip(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
        if (!BlessingConfig.ABYSSAL_HEART_ENABLED.get()) {
            tooltip.add(Component.literal("§c§lDISABLED BY CONFIG"));
            tooltip.add(Component.literal("§7This item's effects are disabled"));
        }
    }
}
