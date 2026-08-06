# Implementation Plan - Wordsense Quiz App Transformation

Transform the existing dictionary app into a Daily English Quiz App while preserving the dictionary as a side feature.

## User Review Required

> [!IMPORTANT]
> The current `MainActivity` and `HomepageActivity` will be heavily refactored. `MainActivity` will become the entry point hosting the navigation graph, and `HomepageActivity` logic will move to `DictionarySearchFragment`.

## Proposed Changes

### Core & Navigation
#### [NEW] [nav_graph.xml](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/res/navigation/nav_graph.xml)
Define the navigation flow: Home -> QuizPack -> Quiz -> Result -> Review. Include `DictionarySearchFragment` as a top-level destination.

#### [MODIFY] [MainActivity](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/ui/MainActivity.kt)
Implement `DrawerLayout` and `NavHostFragment`. Set up `NavigationView` to link with the navigation graph.

#### [MODIFY] [activity_main.xml](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/res/layout/activity_main.xml)
Replace existing search UI with a `DrawerLayout` containing a `NavHostFragment` and `NavigationView`.

---

### Data Layer
#### [NEW] [quizzes.json](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/assets/quizzes.json)
Mock data for the three categories (Common Mistakes, Daily Sentences, Vocabulary).

#### [NEW] [QuizModels.kt](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/data/model/QuizModels.kt)
Define `QuizCategory`, `QuizPack`, and `Question` data classes.

#### [NEW] [QuizRepository.kt](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/data/repository/QuizRepository.kt)
Handle loading and parsing the local JSON data.

---

### UI Components (Fragments)
#### [NEW] [HomeFragment](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/ui/HomeFragment.kt)
Implement the new home screen with Hero banner, Category grid, and "More Quiz Packs" list.

#### [NEW] [QuizPackFragment](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/ui/QuizPackFragment.kt)
List quiz sets for a selected category.

#### [NEW] [QuizFragment](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/ui/QuizFragment.kt)
Handle the quiz interactive flow: question display, answer selection, and showing the explanation state.

#### [NEW] [ResultFragment](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/ui/ResultFragment.kt)
Show final score, accuracy, and options to review or retry.

#### [NEW] [ReviewFragment](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/ui/ReviewFragment.kt)
List all questions with user's answers and correct explanations.

#### [NEW] [DictionarySearchFragment](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/java/studios/darkzen/dictionaryapp/ui/DictionarySearchFragment.kt)
Migrated logic from the old `HomepageActivity`.

---

### Themes & Resources
#### [MODIFY] [colors.xml](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/res/values/colors.xml)
Add the new color palette (Purples, Lavenders, Success/Error colors).

#### [MODIFY] [strings.xml](file:///Users/ewnbd/StudioProjects/dictionary-app-MVVM/app/src/main/res/values/strings.xml)
Add new strings for quiz UI, categories, and Bangla explanations.

## Verification Plan

### Automated Tests
- Unit tests for `QuizRepository` to ensure JSON parsing is correct.
- ViewModel tests for `QuizViewModel` to verify state transitions (Question -> Correct/Incorrect -> Next).

### Manual Verification
1. Open the app, verify the Home Screen appears with all categories.
2. Open a Quiz Pack, start a quiz.
3. Select an answer, verify the explanation state appears immediately.
4. Complete the quiz, verify the Result Screen shows correct stats.
5. Navigate to Dictionary Search from the drawer and verify it still works.
