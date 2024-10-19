package moe.nea.ultranotifier.util.render

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import net.minecraft.util.Identifier
import java.awt.Color

object ScreenRenderUtils {
	fun fillRect(
		matrixStack: UMatrixStack,
		left: Double, top: Double,
		right: Double, bottom: Double,
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

	fun renderTexture(
		identifier: Identifier,
		matrixStack: UMatrixStack,
		left: Double, top: Double,
		right: Double, bottom: Double,
	) {
		UGraphics.bindTexture(0, identifier)
		val graphics = UGraphics.getFromTessellator()
		graphics.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_TEXTURE)
		graphics.pos(matrixStack, left, top, 0.0).tex(0.0, 0.0).endVertex()
		graphics.pos(matrixStack, left, bottom, 0.0).tex(0.0, 1.0).endVertex()
		graphics.pos(matrixStack, right, bottom, 0.0).tex(1.0, 1.0).endVertex()
		graphics.pos(matrixStack, right, top, 0.0).tex(1.0, 0.0).endVertex()
		graphics.drawDirect()
	}
}
