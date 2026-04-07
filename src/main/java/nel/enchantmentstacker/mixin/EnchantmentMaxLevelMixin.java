package nel.enchantmentstacker.mixin;

import nel.enchantmentstacker.config.ModConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMaxLevelMixin {

    @Shadow public abstract Component description();

    @Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
    private void applyCustomMaxLevel(CallbackInfoReturnable<Integer> cir) {

        // Unchains the /enchant command limit by reading the server's live stack trace
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().contains("EnchantCommand")) {
                cir.setReturnValue(255);
                return;
            }
        }

        if (this.description().getContents() instanceof TranslatableContents translatable) {
            String key = translatable.getKey();
            int customMax = ModConfig.get().getCustomMaxLevel(key);

            if (customMax > 0) {
                cir.setReturnValue(customMax);
            }
        }
    }
}