package nel.enchantmentstacker.mixin;

import nel.enchantmentstacker.EnchantmentStacker;
import nel.enchantmentstacker.config.ModConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilRepairMixin {

    // ITEM CAPTURE
    @Inject(method = "createResult", at = @At("HEAD"))
    private void captureContext(CallbackInfo ci) {
        AnvilMenu menu = (AnvilMenu) (Object) this;
        EnchantmentStacker.CURRENT_ITEM.set(menu.getSlot(0).getItem());
    }

    // CLEAR THE TRACKER SO IT DOESN'T LEAK LOL
    @Inject(method = "createResult", at = @At("RETURN"))
    private void clearContext(CallbackInfo ci) {
        EnchantmentStacker.CURRENT_ITEM.remove();
    }

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.BEFORE))
    private void bypassTooExpensive(CallbackInfo ci) {
        AnvilMenu menu = (AnvilMenu) (Object) this;
        int currentCost = ((ItemCombinerMenuAccessor) menu).getLevelCost().get();
        if (currentCost >= 40) {
            ((ItemCombinerMenuAccessor) menu).getLevelCost().set(39);
        }
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void applyFixedRepairCost(CallbackInfo ci) {
        if (!ModConfig.get().enableFixedAnvilCost) return;

        AnvilMenu menu = (AnvilMenu) (Object) this;
        ItemStack leftStack = menu.getSlot(0).getItem();
        ItemStack rightStack = menu.getSlot(1).getItem();
        ItemStack outputStack = menu.getSlot(2).getItem();

        if (leftStack.isEmpty() || rightStack.isEmpty() || outputStack.isEmpty()) return;

        outputStack.set(DataComponents.REPAIR_COST, 0);

        if (!rightStack.is(Items.ENCHANTED_BOOK) && !leftStack.is(rightStack.getItem())) {
            if (leftStack.isDamageableItem() && outputStack.getDamageValue() < leftStack.getDamageValue()) {

                int levelCost = ModConfig.get().repairCostLevelAmount;
                int materialCost = ModConfig.get().repairCostMaterialAmount;

                if (materialCost >= 1) {
                    if (materialCost > rightStack.getCount()) {
                        menu.getSlot(2).set(ItemStack.EMPTY);
                        return;
                    }
                    ((ItemCombinerMenuAccessor) menu).setCost(materialCost);
                }

                if (levelCost >= 1) {
                    ((ItemCombinerMenuAccessor) menu).getLevelCost().set(levelCost);
                }

                int maxDamage = leftStack.getMaxDamage();
                int repairAmount = (int) (maxDamage * ModConfig.get().percentRepairedPerAction);

                int currentDamage = leftStack.getDamageValue() - repairAmount;
                if (currentDamage < 0) {
                    currentDamage = 0;
                }

                outputStack.setDamageValue(currentDamage);
            }
        }
    }
}