// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("room_version", "2.6.0")
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10" apply false
}