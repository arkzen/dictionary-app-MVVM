package studios.darkzen.dictionaryapp.data.model

data class RootResponse(
    val word: String? = null,
    val origin: String? = null,
    val sourceUrls: List<String>? = null,
    val phonetics: List<Phonetics>? = null,
    val meanings: List<Meanings>? = null
)

data class Phonetics(
    val text: String? = null,
    val audio: String? = null
)

data class Meanings(
    val partOfSpeech: String? = null,
    val definitions: List<Definitions>? = null
)

data class Definitions(
    val definition: String? = null,
    val example: String? = null,
    val synonyms: List<String>? = null,
    val antonyms: List<String>? = null
)
