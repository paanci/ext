package eu.kanade.tachiyomi.extension.id.shinigamix

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Serializable
data class ShinigamiXBrowseDto(
    val url: String? = "",
    val title: String? = "",
    @SerialName("cover") val thumbnail: String? = "",
)

@Serializable
data class ShinigamiXMangaDetailDto(
    @SerialName("synopsis") val description: String = "",
    val detailList: List<ShinigamiXMangaDetailListDto>? = null,
)

@Serializable
data class ShinigamiXMangaDetailListDto(
    val name: String = "",
    val value: String = "",
)

@Serializable
data class ShinigamiXChapterListDto(
    val chapterList: List<ShinigamiXChapterDto>? = null,
)

@Serializable
data class ShinigamiXChapterDto(
    @SerialName("releaseDate") val date: String = "",
    @SerialName("title") val name: String = "",
    val url: String = "",
    @SerialName("imageList") val pages: List<String> = emptyList(),
    val slug: String = "",
) {
    fun getParsedDate(): Date? {
        return try {
            when {
                // Parsing format "2 hours ago"
                date.contains("hours ago", ignoreCase = true) -> {
                    val hoursAgo = date.split(" ")[0].toIntOrNull() ?: return null
                    Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(hoursAgo.toLong()))
                }

                // Parsing format "yesterday"
                date.contains("yesterday", ignoreCase = true) -> {
                    Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
                }

                // Parsing tanggal lengkap seperti "October 26, 2024"
                else -> SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date)
            }
        } catch (e: Exception) {
            null // Jika parsing gagal, kembalikan null
        }
    }
}
