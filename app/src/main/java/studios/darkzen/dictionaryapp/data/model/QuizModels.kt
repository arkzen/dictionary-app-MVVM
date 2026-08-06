package studios.darkzen.dictionaryapp.data.model

import com.google.gson.annotations.SerializedName

data class QuizData(
    @SerializedName("categories") val categories: List<QuizCategory>
)

data class QuizCategory(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon_placeholder") val iconPlaceholder: String, // e.g. "ic_mistake", "ic_sentence", "ic_vocab"
    @SerializedName("packs") val packs: List<QuizPack>
)

data class QuizPack(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("question_count") val questionCount: Int,
    @SerializedName("questions") val questions: List<Question>
)

data class Question(
    @SerializedName("id") val id: Int,
    @SerializedName("question_text") val questionText: String,
    @SerializedName("options") val options: List<String>,
    @SerializedName("correct_answer_index") val correctAnswerIndex: Int,
    @SerializedName("explanation_bn") val explanationBn: String,
    @SerializedName("example_sentence") val exampleSentence: String
)
