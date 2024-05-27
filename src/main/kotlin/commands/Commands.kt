package moe.nea.ultranotifier.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import moe.nea.ultranotifier.UltraNotifier
import moe.nea.ultranotifier.event.CommandRegistrationEvent
import moe.nea.ultranotifier.event.UltraNotifierEvents
import moe.nea.ultranotifier.event.UltraSubscribe
import net.minecraft.text.Text

interface CustomSource {
	fun sendFeedback(text: Text)
}


typealias UltraCommandSource =
//#if FORGE
//$$	CustomSource
//#else
	net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

//#endif


fun translatableText(key: String, vararg args: String) =
//#if MC > 11400
	Text.translatable(key, *args)
//#else
//$$	net.minecraft.util.ChatComponentTranslation(key, *args)
//#endif

fun literalText(string: String): Text =
//#if MC > 11400
	Text.literal(string)
//#else
//$$	net.minecraft.util.ChatComponentText(string)
//#endif

object Commands {
	@UltraSubscribe
	fun registerTestCommand(event: CommandRegistrationEvent) {
		event.dispatcher.register(LiteralArgumentBuilder.literal<UltraCommandSource>("hello")
			                          .executes {
				                          it.source.sendFeedback(literalText("Hello World"))
				                          0
			                          })
	}

	fun init() {
		UltraNotifierEvents.register(this)
//#if FORGE
//$$		UltraNotifierEvents.register(BrigadierPatchbay)
//#else
		net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
			UltraNotifierEvents.post(CommandRegistrationEvent(dispatcher))
		}
//#endif
		UltraNotifier.logger.info("Initialized command subsystem")
	}
}
