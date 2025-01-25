package moe.nea.ultranotifier.datamodel

import moe.nea.ultranotifier.util.minecrat.getDirectlyContainedText
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

data class ChatCategory(
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

object ChatCategoryArbiter {
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
				"Economy",
				setOf(ChatTypeId("bazaar"), ChatTypeId("auction-house"))
			)
		)
	)

	fun categorize(content: Text): CategorizedChatLine {
		val stringContent = content.getFormattedTextCompat().removeFormattingCodes()
		return universe.categorize(stringContent)
	}
}





