package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.AscensionAmulet;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = AscensionAmulet.class, remap = false)
public abstract class MixinAscensionAmulet {
    @Redirect(
        method = "addAttributes",
        at = @At(
            value = "INVOKE",
            target = "Lcom/aizistral/enigmaticlegacy/helpers/ItemLoreHelper;addLocalizedString(Ljava/util/List;Ljava/lang/String;)V"
        ),
        remap = false
    )
    private void replaceMagentaTooltip(List<Component> list, String key) {
        if (ExtraConfig.AMULET_REPLACE_GRAVITY.get()
                && "tooltip.enigmaticlegacy.enigmaticAmuletModifierMAGENTA".equals(key)) {
            key = "tooltip.enigmatic_liberator.enigmaticAmuletModifierMAGENTA";
        }
        ItemLoreHelper.addLocalizedString(list, key);
    }
}
