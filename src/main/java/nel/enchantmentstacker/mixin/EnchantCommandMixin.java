package nel.enchantmentstacker.mixin;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {

    // Keeps our level 255 bypass active
    @Redirect(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I"))
    private static int bypassCommandMaxLevel(Enchantment instance) {
        return 255;
    }

    // THE ULTIMATE OVERRIDE: Forces the enchantment but dresses it in the exact Vanilla chat text!
    @Inject(method = "enchant", at = @At("HEAD"), cancellable = true)
    private static void forceAdminEnchant(CommandSourceStack source, Collection<? extends Entity> targets, Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Integer> cir) {
        int successCount = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity le) {
                ItemStack handItem = le.getMainHandItem();

                if (!handItem.isEmpty()) {
                    // Forcibly applies the enchantment directly to the item's data
                    handItem.enchant(enchantment, level);
                    successCount++;

                    // Replicate Vanilla text perfectly: "Applied enchantment [Name] [Level] to [Player]'s item"
                    // Manually creates the Roman Numerals for seamless immersion
                    String lvlStr = level == 1 ? "I" : level == 2 ? "II" : level == 3 ? "III" : level == 4 ? "IV" : level == 5 ? "V" : level == 6 ? "VI" : level == 7 ? "VII" : level == 8 ? "VIII" : level == 9 ? "IX" : level == 10 ? "X" : String.valueOf(level);
                    Component enchName = Component.literal("").append(enchantment.value().description()).append(" ").append(lvlStr);

                    // Fetches the exact vanilla translation string
                    Component msg = Component.translatable("commands.enchant.success.single", enchName, le.getDisplayName());

                    if (targets.size() == 1) {
                        source.sendSuccess(() -> msg, true);
                    }
                }
            }
        }

        // If we successfully enchanted at least one item, report success and cancel the rest of the command!
        if (successCount > 0) {
            if (targets.size() > 1) {
                Component multiMsg = Component.translatable("commands.enchant.success.multiple", enchantment.value().description(), targets.size());
                source.sendSuccess(() -> multiMsg, true);
            }
            cir.setReturnValue(successCount);
        }

        // Note: If the player isn't holding an item (successCount == 0), we let the vanilla
        // command continue naturally so it correctly outputs the standard "Not holding any item" error.
    }
}