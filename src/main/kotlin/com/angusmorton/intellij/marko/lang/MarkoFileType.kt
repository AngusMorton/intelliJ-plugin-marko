package com.angusmorton.intellij.marko.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.angusmorton.intellij.marko.MarkoBundle
import com.angusmorton.intellij.marko.MarkoIcons
import org.jetbrains.annotations.Nls
import javax.swing.Icon

object MarkoFileType : LanguageFileType(MarkoLanguage) {
    override fun getName(): String = "marko"

    override fun getDisplayName(): String = "Marko"

    override fun getDescription(): String = MarkoBundle.message("filetype.marko.description")

    override fun getDefaultExtension(): String = "marko"

    override fun getIcon(): Icon = MarkoIcons.MARKO
}
