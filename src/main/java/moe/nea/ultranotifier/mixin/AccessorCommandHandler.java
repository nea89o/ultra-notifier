package moe.nea.ultranotifier.mixin;

//#if FORGE
//$$import net.minecraft.command.CommandHandler;
//$$import net.minecraft.command.ICommand;
//$$import org.jetbrains.annotations.NotNull;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$import org.spongepowered.asm.mixin.gen.Accessor;
//$$
//$$import java.util.Set;
//$$
//$$@Mixin(CommandHandler.class)
//$$public interface AccessorCommandHandler {
//$$	@Accessor("commandSet")
//$$	@NotNull
//$$	Set<@NotNull ICommand> getCommandSet_ultraNotifier();
//$$}
//#endif