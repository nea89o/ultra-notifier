package moe.nea.ultranotifier.mixin;

import moe.nea.ultranotifier.event.UltraNotifierEvents;
import moe.nea.ultranotifier.event.VisibleChatMessageAddedEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class FilterVisibleMessagePatch {
	@Inject(
		method = "addVisibleMessage",
		at = @At("HEAD"),
		cancellable = true
	)
	private void onAddVisibleMessage(ChatHudLine message, CallbackInfo ci) {
		if (UltraNotifierEvents.post(new VisibleChatMessageAddedEvent(message)).isCancelled()) {
			ci.cancel();
		}
	}
}
