plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
//    id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha11" // <- add this additionally
    id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha01" // <- add this additionally
}