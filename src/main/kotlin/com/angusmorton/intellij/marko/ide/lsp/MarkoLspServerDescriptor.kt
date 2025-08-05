package com.angusmorton.intellij.marko.ide.lsp

import com.intellij.lang.typescript.lsp.JSLspServerDescriptor
import com.intellij.openapi.project.Project

class MarkoLspServerDescriptor(project: Project)
    : JSLspServerDescriptor(project, MarkoLspServerActivationRule, "Marko") {
}