package moe.nea.ultranotifier.gui

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import moe.nea.ultranotifier.datamodel.ChatCategory
import moe.nea.ultranotifier.datamodel.ChatCategoryArbiter
import moe.nea.ultranotifier.util.minecrat.MC
import moe.nea.ultranotifier.util.minecrat.accessor
import moe.nea.ultranotifier.util.render.ScreenRenderUtils
import net.minecraft.client.gui.screen.ChatScreen
import java.awt.Color

class ChatUi(val chatScreen: ChatScreen) {

	val Double.value get() = this
	val Float.value get() = this
	fun getChatBgOpacity(opacityMultiplier: Double = 1.0): Color {
		return Color((MC.instance.options.textBackgroundOpacity.value * opacityMultiplier * 255).toInt() shl 24, true)
	}

	fun calculateChatTop(): Double {
		val ch = MC.chatHud
		ch.accessor()
		val chatOffset =
			40
		val chatTop =
			(chatScreen.height - chatOffset) / ch.chatScale - ch.visibleLineCount * ch.lineHeight_ultranotifier
		return chatTop.toDouble()
	}

	fun iterateButtons(
		onEach: (
			label: ChatCategory,
			isSelected: Boolean,
			pos: Rect,
		) -> Unit
	) {
		val chatTop = calculateChatTop()
		var xOffset = 5
		val top = chatTop - 16.0
		for (button in ChatCategoryArbiter.categories) {
			val w = ScreenRenderUtils.getTextWidth(button.label) + 3
			val isSelected = button == ChatCategoryArbiter.selectedCategory
			onEach(button, isSelected, Rect(xOffset.toDouble(), top, w.toDouble(), 16.0))
			xOffset += w + 5
		}
	}

	data class Rect(
		val x: Double, val y: Double,
		val w: Double, val h: Double,
	) {
		fun contains(mouseX: Double, mouseY: Double): Boolean {
			return mouseX in (l..<r) && mouseY in (t..<b)
		}

		val l get() = x
		val t get() = y
		val r get() = x + w
		val b get() = y + h
		val cy get() = y + h / 2
		val cx get() = x + w / 2
	}

	fun renderButtons(
		matrixStack: UMatrixStack,
		mouseX: Double, mouseY: Double,
	) {
		iterateButtons { button, isSelected, pos ->
			UGraphics.enableBlend()
			ScreenRenderUtils.fillRect(matrixStack,
			                           pos.l, pos.t, pos.r, pos.b,
			                           if (isSelected) getChatBgOpacity() else getChatBgOpacity(0.75))
			UGraphics.disableBlend()
			ScreenRenderUtils.renderText(matrixStack,
			                             pos.l + 2, pos.cy - 9 / 2,
			                             if (isSelected) "§a${button.label}" else "§f${button.label}")
		}
	}

	fun clickMouse(mouseX: Double, mouseY: Double, button: Int) {
		iterateButtons { label, isSelected, pos ->
			if (pos.contains(mouseX, mouseY)) {
				if (button == 0) {
					ChatCategoryArbiter.selectedCategoryId = label.id
				}
				// TODO: right click options or something
			}
		}
	}
}
