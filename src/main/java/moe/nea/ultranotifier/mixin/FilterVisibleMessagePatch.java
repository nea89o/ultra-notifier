package moe.nea.ultranotifier.mixin;

import moe.nea.ultranotifier.event.UltraNotifierEvents;
import moe.nea.ultranotifier.event.VisibleChatMessageAddedEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;

@Mixin(ChatHud.class)
public class FilterVisibleMessagePatch {
//#if MC > 1.18
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
//#else
//$$	@Unique
//$$	ChatHudLine lastAddedChatLine;
//$$
//$$	@Inject(method = "reset()V",
//$$		at = @At(value = "INVOKE",
//$$			target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;IIZ)V"),
//$$		locals = LocalCapture.CAPTURE_FAILHARD)
//$$	private void saveMessageAboutToBeRefreshed(CallbackInfo ci, int i, ChatHudLine chatLine) {
//$$		lastAddedChatLine = chatLine;
//$$	}
//$$
//$$	@Inject(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At(value = "INVOKE",
//#if MC > 1.16
//$$			target = "Lnet/minecraft/client/util/ChatMessages;breakRenderedChatMessageLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;"
//#else
//$$			target = "Lnet/minecraft/client/gui/GuiUtilRenderComponents;splitText(Lnet/minecraft/util/text/ITextComponent;ILnet/minecraft/client/gui/FontRenderer;ZZ)Ljava/util/List;"
//#endif
//$$	))
//$$	private void saveMessage(Text chatComponent, int chatLineId, int updateCounter, boolean displayOnly, CallbackInfo ci) {
//$$		if (lastAddedChatLine != null && lastAddedChatLine.getText() != chatComponent) {
//$$			throw new RuntimeException("Out of order message received");
//$$		}
//$$		if (lastAddedChatLine == null)
//$$			lastAddedChatLine = new ChatHudLine(chatLineId, chatComponent, updateCounter);
//$$	}
//$$
//$$	@ModifyVariable(
//$$		method = "addMessage(Lnet/minecraft/text/Text;IIZ)V",
//$$		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;isChatFocused()Z")
//$$	)
//$$	private List onAddVisibleMessage(List value) {
//$$		VisibleChatMessageAddedEvent event = new VisibleChatMessageAddedEvent(lastAddedChatLine);
//$$		lastAddedChatLine = null;
//$$		if (UltraNotifierEvents.post(event).isCancelled()) {
//$$			return Collections.emptyList();
//$$		}
//$$		return value;
//$$	}
//#endif
}
