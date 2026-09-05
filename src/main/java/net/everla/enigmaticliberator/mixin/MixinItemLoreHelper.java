package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Locale;

/** Uses the configured suffering threshold in the worthy-only tooltip. */
@Mixin(value = ItemLoreHelper.class, remap = false)
public abstract class MixinItemLoreHelper {

    @Overwrite(remap = false)
    public static void indicateWorthyOnesOnly(List<Component> tooltip) {
        ChatFormatting color = ChatFormatting.DARK_RED;
        Player player = Minecraft.getInstance().player;
        if (player != null && SuperpositionHandler.isTheWorthyOne(player)) {
            color = ChatFormatting.GOLD;
        }

        String requiredPercent = String.format(
                Locale.ROOT,
                "%.1f%%",
                ExtraConfig.SUPER_CURSED_TIME.get() * 100.0
        );

        tooltip.add(Component.translatable("tooltip.enigmaticlegacy.worthyOnesOnly1").withStyle(color));
        tooltip.add(Component.translatable("tooltip.enigmatic_liberator.worthy_ones_only2", requiredPercent)
                .withStyle(color));
        tooltip.add(Component.translatable("tooltip.enigmaticlegacy.worthyOnesOnly3").withStyle(color));
        tooltip.add(Component.translatable("tooltip.enigmaticlegacy.void"));
        tooltip.add(Component.translatable("tooltip.enigmaticlegacy.worthyOnesOnly4")
                .withStyle(color)
                .append(Component.literal(player == null ? "" : SuperpositionHandler.getSufferingTime(player))
                        .withStyle(ChatFormatting.LIGHT_PURPLE)));
    }
}
