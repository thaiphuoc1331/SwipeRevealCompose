plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "lib.tpstudio.swipereveal"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "io.github.tpstudio"
                artifactId = "swipereveal-compose"
                version = project.findProperty("VERSION_NAME").toString()

                pom {
                    name.set("SwipeRevealCompose")
                    description.set("A lightweight swipe-to-reveal library for Jetpack Compose.")
                    url.set("https://github.com/thaiphuoc1331/SwipeRevealCompose.git")

                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("thaiphuoc1331")
                            name.set("Thai phuoc")
                            email.set("dev.thaiphuoc@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:https://github.com/thaiphuoc1331/SwipeRevealCompose.git")
                        developerConnection.set("scm:git:ssh://github.com/thaiphuoc1331/SwipeRevealCompose.git")
                        url.set("https://github.com/thaiphuoc1331/SwipeRevealCompose.git")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "MavenCentral"
                url = uri("https://central.sonatype.com/api/v1/publish")
                credentials {
                    username = project.findProperty("mavenCentralUsername").toString()
                    password = project.findProperty("mavenCentralPassword").toString()
                }
            }
        }
    }
}