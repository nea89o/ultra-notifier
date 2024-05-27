package moe.nea.ultranotifier.event

import com.mojang.brigadier.CommandDispatcher
import moe.nea.ultranotifier.commands.UltraCommandSource

/**
 * Fired whenever commands need to be registered. This may be multiple times during each launch. Old commands will be
 * automatically unregistered first.
 */
class CommandRegistrationEvent(val dispatcher: CommandDispatcher<UltraCommandSource>) : UltraEvent()
