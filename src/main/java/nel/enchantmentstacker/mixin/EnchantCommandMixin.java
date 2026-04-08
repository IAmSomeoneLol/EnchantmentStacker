package nel.enchantmentstacker.mixin;

import nel.enchantmentstacker.EnchantmentStacker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {

    // Redirects the specific check inside the command to bypass the limit cleanly
    @Redirect(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I"))
    private static int bypassCommandMaxLevel(Enchantment instance) {
        return 255;
    }

    // CAPTURE THE ITEM HELD BY THE TARGET
    @Inject(method = "enchant", at = @At("HEAD"))
    private static void captureContext(CommandSourceStack source, Collection<? extends Entity> targets, Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Integer> cir) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity le) {
                EnchantmentStacker.CURRENT_ITEM.set(le.getMainHandItem());
                break; // Grab the first valid item
            }
        }
    }

    // CLEAR THE TRACKER
    @Inject(method = "enchant", at = @At("RETURN"))
    private static void clearContext(CommandSourceStack source, Collection<? extends Entity> targets, Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Integer> cir) {
        EnchantmentStacker.CURRENT_ITEM.remove();
    }
}