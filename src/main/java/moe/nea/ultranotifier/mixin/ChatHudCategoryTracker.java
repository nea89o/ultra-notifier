package moe.nea.ultranotifier.mixin;

import moe.nea.ultranotifier.datamodel.CategorizedChatLine;
import moe.nea.ultranotifier.datamodel.ChatCategoryArbiter;
import moe.nea.ultranotifier.datamodel.HasCategorizedChatLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHudLine.class)
public class ChatHudCategoryTracker implements HasCategorizedChatLine {
	@Shadow
	@Final
	private Text content;

	@Unique
	CategorizedChatLine categorizedSelf;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(
		int creationTick,
		Text content,
		MessageSignatureData signature, MessageIndicator tag,
		CallbackInfo ci
	) {
		categorizedSelf = ChatCategoryArbiter.INSTANCE.categorize(content);
	}

	@Override
	public @NotNull CategorizedChatLine getCategorizedChatLine_ultraNotifier() {
		return categorizedSelf;
	}
}
