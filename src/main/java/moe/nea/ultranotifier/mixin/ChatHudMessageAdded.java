package moe.nea.ultranotifier.mixin;

import moe.nea.ultranotifier.event.ChatGuiLineEvent;
import moe.nea.ultranotifier.event.UltraNotifierEvents;
import net.minecraft.client.gui.hud.ChatHud;
//#if MC > 11404
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
//#endif
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		ChatHud.class
)
public class ChatHudMessageAdded {
	@Inject(
//#if MC <= 11404
//#if FORGE
//$$			method = "printChatMessageWithOptionalDeletion",
//#else
//$$			method = "addMessage(Lnet/minecraft/text/Text;I)V",
//#endif
//#else
			method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
//#endif
			at = @At("HEAD"), cancellable = true)
	private void onAddMessage(
			Text message,
//#if MC <= 11404
//$$			int chatLineId,
//#else
			MessageSignatureData signatureData, MessageIndicator indicator,
//#endif
			CallbackInfo ci
	) {
		if (UltraNotifierEvents.post(new ChatGuiLineEvent(message)).isCancelled()) {
			ci.cancel();
		}
	}

}
