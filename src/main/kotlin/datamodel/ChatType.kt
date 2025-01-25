package moe.nea.ultranotifier.datamodel

import jdk.jfr.Category
import moe.nea.ultranotifier.event.SubscriptionTarget
import moe.nea.ultranotifier.event.TickEvent
import moe.nea.ultranotifier.event.UltraSubscribe
import moe.nea.ultranotifier.event.VisibleChatMessageAddedEvent
import moe.nea.ultranotifier.util.minecrat.MC
import moe.nea.ultranotifier.util.minecrat.category
import moe.nea.ultranotifier.util.minecrat.getFormattedTextCompat
import moe.nea.ultranotifier.util.minecrat.removeFormattingCodes
import net.minecraft.text.Text
import java.util.function.Predicate
import java.util.regex.Pattern

data class ChatTypeId(
	val id: String
)

data class ChatType(
	val id: ChatTypeId,
	val name: String,
	val patterns: List<ChatPattern>,
)

data class ChatPattern(
	val text: String
) {
	val pattern = Pattern.compile(text)
	val predicate: Predicate<String> =
//#if JAVA > 11
		pattern.asMatchPredicate()
//#else
//$$		Predicate { it: String -> pattern.matcher(it).matches() }
//#endif
}

data class CategoryId(val id: String)
data class ChatCategory(
	val id: CategoryId,
	val label: String,
	val chatTypes: Set<ChatTypeId>,
)

data class ChatUniverse(
	val name: String,
	val types: List<ChatType>,
	val categories: List<ChatCategory>,
) {
	fun categorize(
		text: String
	): CategorizedChatLine {
		val types = this.types
			.asSequence()
			.filter {
				it.patterns.any {
					it.predicate.test(text)
				}
			}
			.map {
				it.id
			}
			.toSet()
		// TODO: potentially allow recalculating categories on the fly
		val categories = categories.filterTo(mutableSetOf()) { it.chatTypes.any { it in types } }
		return CategorizedChatLine(
			text, types, categories
		)
	}
}

data class CategorizedChatLine(
	val text: String,
	val types: Set<ChatTypeId>,
	val categories: Set<ChatCategory>,
)

interface HasCategorizedChatLine {
	val categorizedChatLine_ultraNotifier: CategorizedChatLine
}

object ChatCategoryArbiter : SubscriptionTarget {
	val specialAll = CategoryId("special-all")
	val universe = ChatUniverse(
		"Hypixel SkyBlock",
		listOf(
			ChatType(
				ChatTypeId("bazaar"),
				"Bazaar",
				listOf(
					ChatPattern("(?i).*Bazaar.*")
				)
			),
			ChatType(
				ChatTypeId("auction-house"),
				"Auction House",
				listOf(
					ChatPattern("(?i).*Auction House.*")
				)
			),
		),
		listOf(
			ChatCategory(
				specialAll,
				"All",
				setOf()
			),
			ChatCategory(
				CategoryId("economy"),
				"Economy",
				setOf(ChatTypeId("bazaar"), ChatTypeId("auction-house"))
			)
		)
	)

	val categories get() = universe.categories
	var selectedCategoryId = specialAll
		set(value) {
			field = value
			selectedCategory = findCategory(value)
		}
	var lastSelectedId = selectedCategoryId

	@UltraSubscribe
	fun onTick(event: TickEvent) {
		if (lastSelectedId != selectedCategoryId) {
			MC.chatHud.reset()
			lastSelectedId = selectedCategoryId
		}
	}

	var selectedCategory: ChatCategory = findCategory(selectedCategoryId)
		private set

	@UltraSubscribe
	fun onVisibleChatMessage(event: VisibleChatMessageAddedEvent) {
		val cl = event.chatLine.category
		if (selectedCategory.id == specialAll)return
		if (selectedCategory !in cl.categories)
			event.cancel()
	}

	fun findCategory(id: CategoryId) = categories.find { it.id == id }!!

	fun categorize(content: Text): CategorizedChatLine {
		val stringContent = content.getFormattedTextCompat().removeFormattingCodes()
		return universe.categorize(stringContent)
	}
}

