package walksy.optimizer.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.IntegerArgumentType;

public class EnableOptimizerCommand {

    public static boolean fastCrystal = true;
    public static int fastCrystalChance = 100; // Default to 100% chance

    public void initializeToggleCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            // Toggle command
            dispatcher.register(ClientCommandManager.literal("walksyfastcrystal")
                    .executes(context -> {
                        if (fastCrystal) {
                            fastCrystal = false;
                            displayMessage("Walksy's Fast crystals disabled!");
                        } else {
                            fastCrystal = true;
                            displayMessage("Walksy's Fast crystals enabled");
                        }
                        return 1;
                    })
            );

            dispatcher.register(ClientCommandManager.literal("walksyfastcrystalchance")
                    .then(ClientCommandManager.argument("chance", IntegerArgumentType.integer(0, 100))
                            .executes(context -> {
                                int chance = IntegerArgumentType.getInteger(context, "chance");
                                fastCrystalChance = chance;
                                displayMessage("Walksy's Fast crystals chance set to " + chance + "%");
                                return 1;
                            })
                    )
                    .executes(context -> {
                        displayMessage("Current fast crystal chance: " + fastCrystalChance + "%");
                        return 1;
                    })
            );
        });
    }

    public static void displayMessage(String message) {
        // Make sure that they are in game.
        if (!inGame()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        ChatHud chatHud = client.inGameHud.getChatHud();

        chatHud.addMessage(Text.of(message));
    }

    public static Boolean inGame() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null && client.getNetworkHandler() != null;
    }
}