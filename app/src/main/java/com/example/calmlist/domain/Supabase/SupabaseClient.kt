package com.example.calmlist.domain.supabase


import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionManager
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object SupabaseClient {

    private const val SUPABASE_URL = "https://rdbadfcazapunynatrdb.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_OEk-AzZ0J2pHdIudys4mZw_UsBeYx5j"

    lateinit var client: io.github.jan.supabase.SupabaseClient
        private set

    fun initialize(context: android.content.Context) {
        client = createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth)


        }
    }
}

