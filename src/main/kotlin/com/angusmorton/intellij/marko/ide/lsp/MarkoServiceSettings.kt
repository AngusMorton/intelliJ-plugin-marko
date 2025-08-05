package com.angusmorton.intellij.marko.ide.lsp

import com.intellij.lang.typescript.lsp.createPackageRef
import com.intellij.lang.typescript.lsp.defaultPackageKey
import com.intellij.lang.typescript.lsp.extractRefText
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
@State(name = "MarkoServiceSettings", storages = [Storage(StoragePathMacros.WORKSPACE_FILE)])
class MarkoServiceSettings(val project: Project) : SimplePersistentStateComponent<MarkoServiceState>(MarkoServiceState()) {
    var serviceMode
        get() = state.serviceMode
        set(value) {
            val changed = state.serviceMode != value
            state.serviceMode = value
            if (changed) restartMarkoServerAsync(project)
        }

    var lspServerPackageRef
        get() = createPackageRef(state.lspServerPackageName, MarkoLspServerLoader.packageDescriptor.serverPackage)
        set(value) {
            val refText = extractRefText(value)
            val changed = state.lspServerPackageName != refText
            state.lspServerPackageName = refText
            if (changed) restartMarkoServerAsync(project)
        }

    companion object {
        fun getInstance(project: Project): MarkoServiceSettings = project.service()
    }
}

class MarkoServiceState : BaseState() {
    var serviceMode by enum(MarkoServiceMode.ENABLED)
    var lspServerPackageName by string(defaultPackageKey)
}

enum class MarkoServiceMode {
    ENABLED,
    DISABLED
}