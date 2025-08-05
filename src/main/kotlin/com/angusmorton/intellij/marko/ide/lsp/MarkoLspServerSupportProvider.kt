package com.angusmorton.intellij.marko.ide.lsp

import com.angusmorton.intellij.marko.MarkoIcons
import com.intellij.javascript.nodejs.util.NodePackageRef
import com.intellij.lang.typescript.lsp.LspServerLoader
import com.intellij.lang.typescript.lsp.LspServerPackageDescriptor
import com.intellij.lang.typescript.lsp.PackageVersion
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.registry.Registry
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem

private object MarkoLspServerPackageDescriptor : LspServerPackageDescriptor(
    name ="@marko/language-server",
    defaultVersion = PackageVersion.downloadable("2.1.0"),
    defaultPackageRelativePath = "/bin.js") {

    override val registryVersion: String get() = Registry.stringValue("marko.language.server.default.version")
}

class MarkoLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerSupportProvider.LspServerStarter) {
        if (MarkoLspServerActivationRule.isLspServerEnabledAndAvailable(project, file)) {
            serverStarter.ensureServerStarted(MarkoLspServerDescriptor(project))
        }
    }

    override fun createLspServerWidgetItem(lspServer: LspServer, currentFile: VirtualFile?): LspServerWidgetItem =
        LspServerWidgetItem(lspServer, currentFile, MarkoIcons.MARKO)
}

fun restartMarkoServerAsync(project: Project) {
    ApplicationManager.getApplication().invokeLater(Runnable {
        LspServerManager.getInstance(project).stopAndRestartIfNeeded(MarkoLspServerSupportProvider::class.java)
    }, project.disposed)
}

object MarkoLspServerLoader : LspServerLoader(MarkoLspServerPackageDescriptor) {
    override fun getSelectedPackageRef(project: Project): NodePackageRef =
        MarkoServiceSettings.getInstance(project).lspServerPackageRef

    override fun restartService(project: Project) {
        restartMarkoServerAsync(project)
    }
}