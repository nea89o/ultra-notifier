package moe.nea.ultranotifier.commands

//#if FORGE
//$$import com.mojang.brigadier.CommandDispatcher
//$$import com.mojang.brigadier.builder.LiteralArgumentBuilder
//$$import com.mojang.brigadier.tree.CommandNode
//$$import moe.nea.ultranotifier.event.CommandRegistrationEvent
//$$import moe.nea.ultranotifier.event.RegistrationFinishedEvent
//$$import moe.nea.ultranotifier.event.UltraNotifierEvents
//$$import moe.nea.ultranotifier.event.UltraSubscribe
//$$import moe.nea.ultranotifier.mixin.AccessorCommandHandler
//$$import net.minecraft.command.CommandBase
//$$import net.minecraft.command.CommandHandler
//$$import net.minecraft.command.ICommandSender
//$$import net.minecraft.util.BlockPos
//$$import net.minecraft.util.ChatComponentText
//$$import net.minecraft.util.text.ITextComponent
//$$import net.minecraftforge.client.ClientCommandHandler
//$$
//$$fun CommandHandler.getCommandSet() = (this as AccessorCommandHandler).commandSet_ultraNotifier
//$$
//$$class BridgedCommandSource(
//$$	val sender: ICommandSender
//$$) : UltraCommandSource {
//$$	override fun sendFeedback(text: ITextComponent) {
//$$		sender.addChatMessage(text)
//$$	}
//$$}
//$$
//$$class BrigadierCommand(
//$$	val dispatcher: CommandDispatcher<UltraCommandSource>,
//$$	val node: CommandNode<UltraCommandSource>
//$$) : CommandBase() {
//$$	override fun addTabCompletionOptions(
//$$		sender: ICommandSender?,
//$$		args: Array<out String>?,
//$$		pos: BlockPos?
//$$	): MutableList<String> {
//$$		return super.addTabCompletionOptions(sender, args, pos)
//$$	}
//$$
//$$	override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
//$$		// TODO: proper check here maybe?
//$$		return true
//$$	}
//$$
//$$	override fun getCommandName(): String {
//$$		return node.name
//$$	}
//$$
//$$	override fun getCommandUsage(sender: ICommandSender?): String {
//$$		return ""
//$$	}
//$$
//$$	private fun getCommandLineText(args: Array<out String>) = "${node.name} ${args.joinToString(" ")}".trim()
//$$
//$$	override fun processCommand(sender: ICommandSender, args: Array<out String>) {
//$$		val source = BridgedCommandSource(sender)
//$$		val results = dispatcher.parse(getCommandLineText(args), source)
//$$		kotlin.runCatching {
//$$			dispatcher.execute(results)
//$$			Unit
//$$		}.recoverCatching {
//$$			source.sendFeedback(ChatComponentText("Could not execute ultra command: ${it.message}"))
//$$		}
//$$	}
//$$}
//$$
//$$object BrigadierPatchbay {
//$$
//$$	@UltraSubscribe
//$$	fun onAfterRegistration(event: RegistrationFinishedEvent) {
//$$		fullReload()
//$$	}
//$$
//$$	@UltraSubscribe
//$$	fun onCommands(event: CommandRegistrationEvent) {
//$$		event.dispatcher
//$$			.register(LiteralArgumentBuilder.literal<UltraCommandSource>("reloadcommands")
//$$				          .executes {
//$$					          it.source.sendFeedback(literalText("Reloading commands"))
//$$					          fullReload()
//$$					          it.source.sendFeedback(literalText("Reload completed"))
//$$					          0
//$$				          })
//$$	}
//$$
//$$	fun fullReload() {
//$$		val handler = ClientCommandHandler.instance
//$$		unpatch(handler)
//$$		val dispatcher = createDispatcher()
//$$		UltraNotifierEvents.post(CommandRegistrationEvent(dispatcher))
//$$		patch(handler, dispatcher)
//$$	}
//$$
//$$	fun createDispatcher() = CommandDispatcher<UltraCommandSource>()
//$$
//$$	fun unpatch(handler: CommandHandler) {
//$$		handler.getCommandSet()
//$$			.removeIf {
//$$				it is BrigadierCommand
//$$			}
//$$		handler.commands.entries
//$$			.removeIf {
//$$				it.value is BrigadierCommand
//$$			}
//$$	}
//$$
//$$	fun patch(handler: CommandHandler, dispatcher: CommandDispatcher<UltraCommandSource>) {
//$$		dispatcher.root.children
//$$			.map { BrigadierCommand(dispatcher, it) }
//$$			.forEach(handler::registerCommand)
//$$	}
//$$}
//#endif


