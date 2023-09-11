import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
    java
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()

}

tasks.run.configure{
    //main = ""
    classpath = sourceSets["test"].runtimeClasspath

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

javafx{
    version = "19"
    modules = listOf("javafx.controls", "javafx.media")
}

application {
    //mainModule.set("test.kotlin")

    mainClass.set("com.oyosite.ticon.games.MapExplorer")

}