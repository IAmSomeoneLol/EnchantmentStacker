package nel.enchantmentstacker;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import nel.enchantmentstacker.config.ModConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantable;

public class EnchantmentStacker implements ModInitializer {

	// global encht toggle
	public static final ThreadLocal<ItemStack> CURRENT_ITEM = ThreadLocal.withInitial(() -> null);

	@Override
	public void onInitialize() {
		ModConfig.load();
		System.out.println("Enchantment Stacker Loaded - Server Configs active.");

		DefaultItemComponentEvents.MODIFY.register(context -> {
			if (ModConfig.get().allowTheUnEnchantable) {
				context.modify(item ->
								item == Items.SHEARS ||
										item == Items.FLINT_AND_STEEL ||
										item == Items.SHIELD ||
										item == Items.ELYTRA ||
										item == Items.BRUSH,
						(builder, item) -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(10))
				);
			}
		});
	}
}