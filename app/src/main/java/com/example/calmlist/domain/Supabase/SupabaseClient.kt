package com.example.calmlist.domain.supabase


import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
//import io.github.jan.supabase.auth.Auth
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install
import kotlinx.serialization.json.Json

object SupabaseClient {

    private const val SUPABASE_URL = "https://rdbadfcazapunynatrdb.supabase.co"
    private const val SUPABASE_KEY = ""

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
    }
}
