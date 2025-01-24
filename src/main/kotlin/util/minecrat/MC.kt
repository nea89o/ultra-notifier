package moe.nea.ultranotifier.util.minecrat

import net.minecraft.client.MinecraftClient

object MC {
	val font get() = instance.textRenderer
	val instance get() = MinecraftClient.getInstance()
	val inGameHud get() = instance.inGameHud!!
	val chatHud get() = inGameHud.chatHud!!

}
