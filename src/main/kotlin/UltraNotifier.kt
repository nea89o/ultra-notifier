package moe.nea.ultranotifier

import moe.nea.ultranotifier.event.RegistrationFinishedEvent
import moe.nea.ultranotifier.event.UltraEvent
import moe.nea.ultranotifier.event.UltraNotifierEvents
import moe.nea.ultranotifier.init.NeaMixinConfig
import java.io.File

object UltraNotifier {
	val logger =
//#if MC <= 11404
//$$		org.apache.logging.log4j.LogManager.getLogger("UltraNotifier")!!
//#else
		org.slf4j.LoggerFactory.getLogger("UltraNotifier")!!
//#endif

	fun onStartup() {
		logger.info("Started on ${Constants.MINECRAFT_VERSION} ${Constants.PLATFORM} with version ${Constants.VERSION}!")
		for (mixinPlugin in NeaMixinConfig.getMixinPlugins()) {
			logger.info("Loaded ${mixinPlugin.mixins.size} mixins for ${mixinPlugin.mixinPackage}.")
		}
		AllModules.allModules.forEach {
			UltraNotifierEvents.register(it)
			it.init()
		}

		RegistrationFinishedEvent().post()

	}

	val configFolder = File("config/ultra-notifier").also {
		it.mkdirs()
	}
}
