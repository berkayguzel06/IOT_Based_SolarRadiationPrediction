import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ThingSpeakAPI {
    @GET("channels/{channel_id}/fields/{field_number}.json")
    fun getSensorData(
        @Path("channel_id") channelId: Int,
        @Path("field_number") fieldNumber: Int,
        @Query("api_key") apiKey: String,
        @Query("results") results: Int
    ): Call<ResponseBody>
}
