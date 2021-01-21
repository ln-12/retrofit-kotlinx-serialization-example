package com.example.retrofittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.http.GET
import retrofit2.http.Path

@ExperimentalSerializationApi
class MainActivity : AppCompatActivity() {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(json.asConverterFactory(MediaType.parse("application/json")!!))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = retrofit.create(GitHubService::class.java)
        val repos: Call<List<Repo>> = service.listRepos("ln-12")

        MainScope().launch {
            println(repos.await())
        }
    }
}

@Serializable
data class Repo(
    @SerialName("full_name")
    val someWeirdRandomName_12345: String,
    val name: String
)

interface GitHubService {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<Repo>>
}