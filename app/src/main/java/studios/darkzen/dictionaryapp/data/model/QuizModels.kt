package studios.darkzen.dictionaryapp.data.model

import com.google.gson.annotations.SerializedName

data class QuizData(
    @SerializedName("version") val version: Int,
    @SerializedName("content_style") val contentStyle: String,
    @SerializedName("categories") val categories: List<QuizCategory>
)

data class QuizCategory(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon_placeholder") val iconPlaceholder: String,
    @SerializedName("packs") val packs: List<QuizPack>
)

data class QuizPack(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("question_count") val questionCount: Int?,
    @SerializedName("questions") val questions: List<Question>
)

data class Question(
    @SerializedName("id") val id: Int,
    @SerializedName("hook_text") val hookText: String?,
    @SerializedName("question_text") val questionText: String,
    @SerializedName("options") val options: List<String>,
    @SerializedName("correct_answer_index") val correctAnswerIndex: Int,
    @SerializedName("explanation_bn") val explanationBn: String,
    @SerializedName("example_sentence") val exampleSentence: String,
    @SerializedName("reel_talking_point_bn") val reelTalkingPointBn: String?
)
