// build.gradle.kts (Project Level)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.2") // Match AGP version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10") // Ensure correct Kotlin version
    }
}
