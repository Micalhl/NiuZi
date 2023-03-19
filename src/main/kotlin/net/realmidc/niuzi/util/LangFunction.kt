package net.realmidc.niuzi.util

import net.mamoe.mirai.contact.Contact
import net.realmidc.niuzi.ConfigReader
import taboolib.common.util.replaceWithOrder
import taboolib.module.lang.Language
import taboolib.module.lang.LanguageFile
import taboolib.module.lang.TypeText

/**
 * NiuZi
 * net.realmidc.niuzi.util.LangFunction
 *
 * @author mical
 * @since 2023/3/19 11:40 AM
 */
fun sendLang(node: String, vararg args: Any) {
    val file = getLocaleFile(ConfigReader.language)
    if (file == null) {
        println("{$node}")
    } else {
        val type = file.nodes[node]
        if (type != null) {
            if (type is TypeText) {
                println(type.text!!.replaceWithOrder(*args))
            } else {
                error("Not yet implemented")
            }
        } else {
            println("{$node}")
        }
    }
}

suspend fun Contact.sendLang(node: String, vararg args: Any) {
    val file = getLocaleFile(ConfigReader.language)
    if (file == null) {
        sendMessage("{$node}")
    } else {
        val type = file.nodes[node]
        if (type != null) {
            if (type is TypeText) {
                sendMessage(type.text!!.replaceWithOrder(*args))
            } else {
                error("Not yet implemented")
            }
        } else {
            sendMessage("{$node}")
        }
    }
}

private fun getLocaleFile(locale: String): LanguageFile? {
    return Language.languageFile.entries.firstOrNull { it.key.equals(locale, true) }?.value
        ?: Language.languageFile[Language.default]
        ?: Language.languageFile.values.firstOrNull()
}