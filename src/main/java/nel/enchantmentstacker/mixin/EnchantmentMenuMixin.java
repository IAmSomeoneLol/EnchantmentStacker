package nel.enchantmentstacker.mixin;

import nel.enchantmentstacker.config.ModConfig;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {

    @Inject(method = "slotsChanged", at = @At("HEAD"), cancellable = true)
    private void blockUnenchantable(Container container, CallbackInfo ci) {
        EnchantmentMenu menu = (EnchantmentMenu) (Object) this;
        ItemStack stack = menu.getSlot(0).getItem();

        if (!ModConfig.get().allowTheUnEnchantable) {
            if (stack.is(Items.SHEARS) || stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.SHIELD) || stack.is(Items.ELYTRA) || stack.is(Items.BRUSH)) {
                menu.costs[0] = 0;
                menu.costs[1] = 0;
                menu.costs[2] = 0;
                menu.broadcastChanges();
                ci.cancel();
            }
        }
    }
}