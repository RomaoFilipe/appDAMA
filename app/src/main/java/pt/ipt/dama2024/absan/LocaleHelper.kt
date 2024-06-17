package pt.ipt.dama2024.absan

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.*
import pt.ipt.dama2024.absan.R

/* LocaleHelper é um objeto que ajuda a gerir a localidade (idioma) do aplicativo */
object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language" /* Chave para armazenar o idioma selecionado */

    /* Método chamado ao anexar o contexto, define o idioma persistido */
    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    /* Método para definir o idioma do aplicativo */
    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return updateResources(context, language)
    }

    /* Método para obter o idioma atual */
    fun getLanguage(context: Context): String {
        return getPersistedData(context, Locale.getDefault().language)
    }

    /* Método para obter os dados persistidos do idioma */
    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        val preferences: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage) ?: defaultLanguage
    }

    /* Método para persistir o idioma selecionado */
    private fun persist(context: Context, language: String) {
        val preferences: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    /* Método para atualizar os recursos do contexto com o idioma selecionado */
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        return context.createConfigurationContext(config)
    }
}
