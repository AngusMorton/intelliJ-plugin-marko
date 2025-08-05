package com.angusmorton.intellij.marko.ide.highlighting

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider.PluginBundle
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * Provides the Marko TextMate bundle for syntax highlighting.
 * This replaces the custom registration approach with the proper
 * IntelliJ Platform TextMate extension point.
 */
class MarkoTextMateBundleProvider : TextMateBundleProvider {
    val LOG: Logger = Logger.getInstance(MarkoTextMateBundleProvider::class.java)

    override fun getBundles(): List<PluginBundle> {
        val tmp = Files.createTempDirectory(
            Paths.get(PathManager.getTempPath()), "textmate"
        )

        val filesToCopy = listOf(
            "package.json",
            "marko.configuration.json",
            "syntaxes/marko.tmLanguage.json",
        )

        val classLoader = MarkoTextMateBundleProvider::class.java.getClassLoader()

        for (fileToCopy in filesToCopy) {
            val resource: URL? = classLoader.getResource("textmate/$fileToCopy")
            if (resource == null) {
                LOG.warn("Resource not found: $fileToCopy")
                throw IOException("Resource not found: $fileToCopy")
            }

            resource.openStream().use { resourceStream ->
                val target: Path = tmp.resolve(fileToCopy)
                Files.createDirectories(target.parent)
                Files.copy(resourceStream, target, StandardCopyOption.REPLACE_EXISTING)
            }
        }

        return listOf(PluginBundle("marko", tmp))
    }
}