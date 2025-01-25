package moe.nea.ultranotifier.mixin;

import moe.nea.ultranotifier.datamodel.CategorizedChatLine;
import moe.nea.ultranotifier.datamodel.ChatCategoryArbiter;
import moe.nea.ultranotifier.datamodel.HasCategorizedChatLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC> 1.20
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
//#endif

@Mixin(ChatHudLine.class)
public class ChatHudCategoryTracker<T> implements HasCategorizedChatLine {
	@Unique
	CategorizedChatLine categorizedSelf;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(
		int creationTick,
		//#if MC>1.20
		Text content,
		//#elseif MC<1.16
		//$$net.minecraft.util.text.ITextComponent content,
		//$$int weird,
		//#else
		//$$T content,
		//#endif
		//#if MC>1.20
		MessageSignatureData signature, MessageIndicator tag,
		//#endif
		CallbackInfo ci
	) {
		categorizedSelf = ChatCategoryArbiter.INSTANCE.categorize(content);
	}

	@Override
	public @NotNull CategorizedChatLine getCategorizedChatLine_ultraNotifier() {
		return categorizedSelf;
	}
}
