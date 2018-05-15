# LRNCurriculum
LRNCurriculum is a simple library that you can plugin to your Android apps to quickly get the curriculum from dot Learn servers.

## Install
The Gradle dependency is available via jCenter. jCenter is the default Maven repository used by Android Studio.

Add this in your (app) module's `build.gradle` file:
```groovy
    implementation 'io.paperdb:paperdb:2.6'
    implementation (group: 'dotlearn.io', name: 'lrncurriculum', version: '1.3.8', ext: 'aar', classifier: 'release')
```

Then add this to your project level `build.gradle` repositories:
```groovy
maven {
            url "s3://dl.android.s3.amazonaws.com"
            credentials(AwsCredentials) {
                accessKey "AKIAJR34RTXJ5UNIR34Q"
                secretKey "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"
            }
        }
```

## Basic Tutorial

#### Intro
The curriculum is made up of four main types of objects listed below:
        Courses
           ↓
        Sections
           ↓
        Lessons
           ↓
        Videos | Quizzes

1. Course - This is also called a subject.
2. Section - A collection of related lessons. Can also be called topics
3. Lesson - A collection of videos explaining a concept
4. Video - A lesson video
5. Quiz - A lesson quiz

Usually, you will follow the steps below when using this library:
1. Load all available courses
2. When a course is clicked, load all the sections in the clicked course
3. When a section is clicked, load all lessons in the clicked section
4. When a lesson is clicked, load all the videos in the lesson
5. When a video is clicked, play the selected video using the `LRNPlayerView`
6. When the user completes a lesson, show the quizzes for that lesson using `LRNQuestion`

#### Initializing

Before making any calls to the library, ensure that you have called the `init` function (preferably
in your Application class `onCreate` method):

```kotlin
    CurriculumProvider.init(context)
```

You can safely call the `init` function on the main UI thread, as it doesn't block.
All other functions from the CurriculumProvider class are synchronous and they block until the task is
completed. You will have to call them from a different thread to avoid the `NetworkOnMainThreadException`

#### Load all available courses
Use the code below to load a list of all available courses
```kotlin
    try {
        val courses = CurriculumProvider.getCourses()
    }
    catch (e: Exception) {
        e.printStackTrace()
        // An error occurred connecting to the server or loading the local curriculum
    }
```


#### Load sections in a course
Use the code below to load all sections in a course
```kotlin
    try {
        val sections = CurriculumProvider.getSections(courseId)
    }
    catch (e: Exception) {
        e.printStackTrace()
        // An error occurred connecting to the server or loading the local curriculum
    }
```

#### Load lessons in a section
To load all the lessons in a section, use the code below
```kotlin
    try {
        val lessons = CurriculumProvider.getLessons(sectionId)
    }
    catch (e: Exception) {
        e.printStackTrace()
        // An error occurred connecting to the server or loading the local curriculum
    }
```

#### Load videos in a lesson
To load all the videos in a lesson, use the code below
```kotlin
    try {
        val videos = CurriculumProvider.getVideos(sectionId)
    }
    catch (e: Exception) {
        e.printStackTrace()
        // An error occurred connecting to the server or loading the local curriculum
    }
```

#### Load quizzes in a lesson
Finally, to load all the quizzes in a lesson, use the code below
```kotlin
    try {
        val quizzes = CurriculumProvider.getQuizzes(sectionId)
    }
    catch (e: Exception) {
        e.printStackTrace()
        // An error occurred connecting to the server or loading the local curriculum
    }
```

#### Additional functionality
The tutorial above was just a basic example/flow. There are a few other functions that
allows you to jump a few steps down the tree.

#### 1. Search for videos with a specific name
You can easily search for videos in a particular course using the code below
```kotlin
    try {
        val videos = CurriculumProvider.searchVideos(searchQuery, courseId)
    }
    catch (e: Exception) {
        e.printStackTrace()
        // An error occurred connecting to the server or loading the local curriculum
    }
```

When you make a request for a particular item for the first time a request is made to the server
to download the item. Subsequent request for that item will be served from a local database.
This local database can get stale over time and you might want to have a `Service` that runs
periodically (like every x days) to update the local database. We created a helper class called
`CurriculumSync` that you can use in your background service to update the local database.

`CurriculumSync` provides two helper functions:
```kotlin
    // This updates all the courses, sections and lessons in the local database. It is faster
    CurriculumSync.shallowSyncAll()
    
    // This updates all the courses, sections, lessons and modules in the local database. It is
    // quite slower
    CurriculumSync.deepSyncAll()
```

That's all. You can see all this in action in the `CurriculumActivity` in the sample project in
 the `app` module.