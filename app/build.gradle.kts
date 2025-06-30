plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")

    id("androidx.room")
    id("com.google.devtools.ksp")
}

android {
    namespace = "pe.edu.upeu.appturismo202501"
    compileSdk = 35

    defaultConfig {
        applicationId = "pe.edu.upeu.appturismo202501"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:deprecation")
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.compose.foundation:foundation:1.4.0")
    // Material 3
    implementation("androidx.compose.material3:material3:1.4.0-alpha13")



    // Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.4.0")



    //Navegacion
    implementation("androidx.navigation:navigation-compose:2.8.9")
//Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.11.0")
//Formularios
    implementation ("com.github.k0shk0sh:compose-easyforms:0.2.0")
//Agregados Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.52") //old 2.47
    kapt ("com.google.dagger:hilt-compiler:2.52") //old 2.47
//Agregado Dagger - Hilt Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") //old 1.0.0
    implementation("com.valentinilk.shimmer:compose-shimmer:1.3.2")
    //old 1.0.5, 1.3.1
//implementation ("io.coil-kt:coil-compose:2.7.0") //old 2.4.0
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
//Agregado LiveData compose//implementation ("androidx.compose.ui:ui-tooling")
    implementation ("androidx.compose.foundation:foundation")
    implementation ("androidx.compose.runtime:runtime-livedata")
//App Compact para detectar modo dia noche
    val appcompat_version = "1.7.0" //old 1.6.1
    implementation("androidx.appcompat:appcompat:$appcompat_version")
    //Agrega do recien




    val room_version = "2.7.0"

    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")


    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation("com.google.accompanist:accompanist-permissions:0.37.2")

    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("androidx.activity:activity-compose:1.7.2")

    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.0")

    implementation ("androidx.compose.animation:animation:1.5.0")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")


    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.34.0")

    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    // Stripe Android SDK
    implementation("com.stripe:stripe-android:21.19.0")
    // Include the financial connections SDK to support US bank account as a payment method
    implementation("com.stripe:financial-connections:21.19.0")

    // Pager (para el carrusel)
    implementation ("com.google.accompanist:accompanist-pager:0.28.0")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.28.0")
}