import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.5.0"
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    jcenter()
    maven{url = uri("https://jitpack.io" )}
}

sourceSets.main {
    java.srcDirs("src")
    resources.srcDirs("src/resources")
}


application {
    mainClass.set("ru.team10.graphApp.MainApp")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.CWTSLeiden:networkanalysis:1.1.0")
    implementation("no.tornado:tornadofx:1.7.20") {
        exclude("org.jetbrains.kotlin")
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

javafx {
    version = "11.0.2"
    modules("javafx.controls")
}
