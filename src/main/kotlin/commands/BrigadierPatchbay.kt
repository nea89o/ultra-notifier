package moe.nea.ultranotifier.commands

//#if MC < 1.16
//$$import com.mojang.brigadier.CommandDispatcher
//$$import com.mojang.brigadier.builder.LiteralArgumentBuilder
//$$import com.mojang.brigadier.tree.CommandNode
//$$import moe.nea.ultranotifier.event.CommandRegistrationEvent
//$$import moe.nea.ultranotifier.event.RegistrationFinishedEvent
//$$import moe.nea.ultranotifier.event.UltraNotifierEvents
//$$import moe.nea.ultranotifier.event.UltraSubscribe
//$$import moe.nea.ultranotifier.event.SubscriptionTarget
//$$import moe.nea.ultranotifier.mixin.AccessorCommandHandler
//$$import net.minecraft.command.CommandBase
//$$import net.minecraft.command.CommandHandler
//$$import net.minecraft.command.ICommandSender
//$$import net.minecraft.server.MinecraftServer
//$$import net.minecraft.util.text.ITextComponent
//$$import net.minecraftforge.client.ClientCommandHandler
//$$
//$$fun CommandHandler.getCommandSet() = (this as AccessorCommandHandler).commandSet_ultraNotifier
//$$
//$$class BridgedCommandSource(
//$$	val sender: ICommandSender
//$$) : UltraCommandSource {
//$$	override fun sendFeedback(text: ITextComponent) {
//$$		sender.sendMessage(text)
//$$	}
//$$}
//$$
//$$class BrigadierCommand(
//$$	val dispatcher: CommandDispatcher<UltraCommandSource>,
//$$	val node: CommandNode<UltraCommandSource>
//$$) : CommandBase() {
//#if MC >= 1.12
//$$    override fun checkPermission(server: MinecraftServer, sender: ICommandSender): Boolean {
//$$		return true
//$$	}
//#else
//$$ 	override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
//$$		return true
//$$	}
//#endif
//$$
//$$	override fun getName(): String {
//$$		return node.name
//$$	}
//$$
//$$	override fun getUsage(sender: ICommandSender): String {
//$$		return ""
//$$	}
//$$
//$$	private fun getCommandLineText(args: Array<out String>) = "${node.name} ${args.joinToString(" ")}".trim()
//$$
//$$
//#if MC < 1.12
//$$    override fun processCommand(sender: ICommandSender, args: Array<out String>) {
//#else
//$$	override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
//#endif
//$$		val source = BridgedCommandSource(sender)
//$$		val results = dispatcher.parse(getCommandLineText(args), source)
//$$		kotlin.runCatching {
//$$			dispatcher.execute(results)
//$$			Unit
//$$		}.recoverCatching {
//$$			source.sendFeedback(literalText("Could not execute ultra command: ${it.message}"))
//$$		}
//$$	}
//$$}
//$$
//$$object BrigadierPatchbay : SubscriptionTarget {
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


