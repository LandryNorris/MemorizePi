import org.jetbrains.compose.ComposeBuildConfig.composeVersion

val decomposeVersion: String by project
val sqlVersion: String by project
val koinVersion: String by project
val coroutinesVersion: String by project

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
    id("com.squareup.sqldelight")
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
}

kotlin {
    android()
    
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    iosSimulatorArm64 {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.insert-koin:koin-core:$koinVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.squareup.sqldelight:coroutines-extensions:1.5.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.insert-koin:koin-test:$koinVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }

        val composeMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("com.google.android.material:material:1.5.0")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.preview)
                implementation(compose.uiTooling)
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
            }
        }

        val androidMain by getting {
            dependsOn(composeMain)

            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqlVersion")
            }
        }
        val androidTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("com.squareup.sqldelight:sqlite-driver:$sqlVersion")
            }
        }
        val iosMain by getting {
            dependsOn(commonMain)
            //dependsOn(composeMain) //once compose supports iOS

            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlVersion")
            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosTest by getting {
            dependsOn(commonTest)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }
}

tasks {
    koverMergedHtmlReport {
        isEnabled = true
        htmlReportDir.set(layout.buildDirectory.dir("report/html"))
        includes = listOf("com.memorizepi.*")
        excludes = listOf("com.memorizepi.generated.*",
            "com.memorizepi.ui.*") //no need to test generated code
    }

    koverMergedXmlReport {
        isEnabled = true
        xmlReportFile.set(layout.buildDirectory.file("report/report.xml"))
        includes = listOf("com.memorizepi.*")
        excludes = listOf("com.memorizepi.generated.*",
            "com.memorizepi.ui.*") //no need to test generated code
    }
}

kover {
    isDisabled = false
    coverageEngine.set(kotlinx.kover.api.CoverageEngine.INTELLIJ)
    generateReportOnCheck = true
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0"
    }
}

detekt {
    source = files(
        "src/commonMain/kotlin"
    )
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.memorizepi.generated"
    }
}
