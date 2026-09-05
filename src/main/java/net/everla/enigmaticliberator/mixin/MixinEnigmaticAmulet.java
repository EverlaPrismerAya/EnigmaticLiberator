package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.EnigmaticAmulet;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.network.chat.Component;

import java.util.UUID;
import java.util.List;

@Mixin(value = EnigmaticAmulet.class, remap = false)
public abstract class MixinEnigmaticAmulet {
    private static final UUID GRAVITY_MODIFIER = UUID.fromString("d1a07f6f-1079-4b17-8dbd-c74dc5e9094d");

    @Inject(method = "getCurrentModifiers", at = @At("RETURN"), cancellable = true, remap = false)
    private void replaceCurrentGravityWithReach(ItemStack stack, Player player,
                                                 CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        cir.setReturnValue(replaceGravityWithReach(cir.getReturnValue()));
    }

    @Inject(method = "getAllModifiers", at = @At("RETURN"), cancellable = true, remap = false)
    private void replaceAllGravityWithReach(Player player,
                                             CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        cir.setReturnValue(replaceGravityWithReach(cir.getReturnValue()));
    }

    private Multimap<Attribute, AttributeModifier> replaceGravityWithReach(
            Multimap<Attribute, AttributeModifier> original) {
        if (!ExtraConfig.AMULET_REPLACE_GRAVITY.get()) {
            return original;
        }

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(original);
        boolean replaced = modifiers.get(ForgeMod.ENTITY_GRAVITY.get()).removeIf(
            modifier -> modifier.getId().equals(GRAVITY_MODIFIER));
        if (replaced) {
            modifiers.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(
                GRAVITY_MODIFIER,
                "enigmaticlegacy:reach_bonus",
                0.25,
                AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        return modifiers;
    }

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
        com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper.addLocalizedString(list, key);
    }
}
