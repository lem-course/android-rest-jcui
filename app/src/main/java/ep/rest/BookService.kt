package ep.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object BookService {

    interface RestApi {

        companion object {
            // TODO: Nastavite posredovanje števil vrat v programu VirtualBox
            const val URL = "http://10.0.2.2:8080/netbeans/mvc-rest/api/"
        }

        @GET("books")
        fun getAll(): Call<List<Book>>

        @GET("books/{id}")
        fun get(@Path("id") id: Int): Call<Book>

        @FormUrlEncoded
        @POST("books")
        fun insert(
            @Field("author") author: String,
            @Field("title") title: String,
            @Field("price") price: Double,
            @Field("year") year: Int,
            @Field("description") description: String
        ): Call<Unit>

        @FormUrlEncoded
        @PUT("books/{id}")
        fun update(
            @Path("id") id: Int,
            @Field("author") author: String,
            @Field("title") title: String,
            @Field("price") price: Double,
            @Field("year") year: Int,
            @Field("description") description: String
        ): Call<Unit>
    }

    val instance: RestApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(RestApi.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RestApi::class.java)
    }
}
