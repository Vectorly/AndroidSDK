# LRNQuestion
LRNCurriculum is a simple library that you can use to display quizzes from the dotLearn servers

## Install
Add this in your (app) module's `build.gradle` file:
```groovy
    implementation 'com.google.code.gson:gson:2.8.4'
    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    implementation (group: 'dotlearn.io', name: 'lrnquestion', version: '1.4.0', ext: 'aar', classifier: 'release')
```

Then add this to your project level `build.gradle` repositories:
```groovy
maven {
            url "s3://dl.android.s3.amazonaws.com"
            credentials(AwsCredentials) {
                accessKey "AKIAJR34RTXJ5UNIR34Q"
                secretKey "bPV8v6VPq3d8L+183A8cSP6IzSOC4bbEoXS66K1f"
            }
        }
```

## How to use

To display a quiz in your app, you use the `QuizFragment` class which shows a quiz UI.
```kotlin
supportFragmentManager.beginTransaction().replace(R.id.questions_container,
                                QuizFragment.newInstance("QuizId")).commit()
```

The Activity or parent Fragment have to implement the `QuizFragment.OnFragmentInteractionListener`

```kotlin
    // Called when a quiz option is selected by the user
    override fun onOptionSelected(quiz: QuizData?, userSelectedOption: Int, quizFragment: QuizFragment?) {
    }

    // Called when the quiz data is loaded from the server or local database
    override fun onQuizDataLoaded(quizData: QuizData?, quizFragment: QuizFragment?) {
    }
```

When the onOptionSelected callback is called, you can show the question answer and explanation to the user.

```kotlin
    override fun onOptionSelected(quiz: QuizData?, userSelectedOption: Int, quizFragment: QuizFragment?) {
        quizFragment.showAnswerAndExplanation()
    }
```

You are responsible for showing a UI like a button for navigation between quizzes. The `QuizFragment` class only shows one quiz alone and doesn't have any navigation option.

That's all. You can see all this in action in the `CurriculumActivity` in the sample project in
 the `app` module.