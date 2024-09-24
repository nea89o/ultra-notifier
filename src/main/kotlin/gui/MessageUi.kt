package moe.nea.ultranotifier.gui

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UScreen
import java.awt.Color

class MessageUi : UScreen() {
	override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
		super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)
		fillRect(matrixStack,
		         0.0, 0.0, width.toDouble(), height.toDouble(), Color.RED)
	}

	fun fillRect(
		matrixStack: UMatrixStack,
		left: Double, top: Double, right: Double, bottom: Double,
		color: Color,
	) {
		val buffer = UGraphics.getFromTessellator()
		buffer.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR)
		buffer.pos(matrixStack, left, top, 0.0).color(color).endVertex()
		buffer.pos(matrixStack, left, bottom, 0.0).color(color).endVertex()
		buffer.pos(matrixStack, right, bottom, 0.0).color(color).endVertex()
		buffer.pos(matrixStack, right, top, 0.0).color(color).endVertex()
		buffer.drawDirect()
	}

}
