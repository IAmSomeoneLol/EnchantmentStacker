package nel.enchantmentstacker.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilMenu.class)
public interface ItemCombinerMenuAccessor {
    @Accessor("repairItemCountCost")
    void setCost(int cost);

    @Accessor("cost")
    DataSlot getLevelCost();
}