package com.angusmorton.intellij.marko.ide.lsp

import com.angusmorton.intellij.marko.lang.MarkoFileType
import com.intellij.lang.typescript.compiler.languageService.TypeScriptLanguageServiceUtil
import com.intellij.lang.typescript.lsp.LspServerActivationRule
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object MarkoLspServerActivationRule : LspServerActivationRule(MarkoLspServerLoader) {
    override fun isFileAcceptableForLspServer(file: VirtualFile): Boolean {
        if (!TypeScriptLanguageServiceUtil.IS_VALID_FILE_FOR_SERVICE.value(file)) return false
        return file.fileType == MarkoFileType
    }

    override fun isProjectContext(project: Project, context: VirtualFile): Boolean =
        context.fileType == MarkoFileType

    override fun isEnabledInSettings(project: Project): Boolean =
        MarkoServiceSettings.getInstance(project).serviceMode == MarkoServiceMode.ENABLED
}

