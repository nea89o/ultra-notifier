package moe.nea.ultranotifier.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import moe.nea.ultranotifier.UltraNotifier
import moe.nea.ultranotifier.event.CommandRegistrationEvent
import moe.nea.ultranotifier.event.SubscriptionTarget
import moe.nea.ultranotifier.event.UltraNotifierEvents
import moe.nea.ultranotifier.event.UltraSubscribe
import net.minecraft.text.Text

interface CustomSource {
	fun sendFeedback(text: Text)
}


typealias UltraCommandSource =
//#if FORGE
//$$	CustomSource
//#elseif MC > 1.18
	net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
//#else
//$$	net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource
//#endif


fun translatableText(key: String, vararg args: String) =
//#if MC > 1.17
	Text.translatable(key, *args)
//#else
//$$	net.minecraft.text.TranslatableText(key, *args)
//#endif

fun literalText(string: String): Text =
//#if MC > 1.17
	Text.literal(string)
//#else
//$$	net.minecraft.text.LiteralText(string)
//#endif

object Commands : SubscriptionTarget {
	@UltraSubscribe
	fun registerTestCommand(event: CommandRegistrationEvent) {
		event.dispatcher.register(LiteralArgumentBuilder.literal<UltraCommandSource>("hello")
			                          .executes {
				                          it.source.sendFeedback(literalText("Hello World"))
				                          0
			                          })
	}

//#if MC <= 1.18 && FABRIC
//$$	@UltraSubscribe
//$$	fun registerEverythingOnce(event: moe.nea.ultranotifier.event.RegistrationFinishedEvent) {
//$$		CommandRegistrationEvent(net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.DISPATCHER).post()
//$$	}
//#endif

	override fun init() {
//#if FORGE
//$$		UltraNotifierEvents.register(BrigadierPatchbay)
//#elseif MC > 1.18
		net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
			UltraNotifierEvents.post(CommandRegistrationEvent(dispatcher))
		}
//#endif
		UltraNotifier.logger.info("Initialized command subsystem")
	}
}
