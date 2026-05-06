#!/bin/bash
# Reconstructs full Android project structure from flat repo files
set -e

echo "Setting up Android project structure..."

# --- Directory structure ---
mkdir -p app/src/main/kotlin/com/dualclock/widget
mkdir -p app/src/main/res/layout
mkdir -p app/src/main/res/xml
mkdir -p app/src/main/res/values
mkdir -p gradle/wrapper
mkdir -p .github/workflows

# --- Kotlin sources ---
cp ClockService.kt       app/src/main/kotlin/com/dualclock/widget/
 cp DualClockWidget.kt     app/src/main/kotlin/com/dualclock/widget/
cp BootReceiver.kt        app/src/main/kotlin/com/dualclock/widget/

# --- Resources ---
cp widget_layout.xml  app/src/main/res/layout/
cp widget_info.xml    app/src/main/res/xml/
cp strings.xml        app/src/main/res/values/

# --- Manifest ---
cp AndroidManifest.xml app/src/main/

# --- App-level build.gradle ---
cp build.gradle app/

# --- Root build.gradle (project-level) ---
printf 'plugins {\n    id '"'"'com.android.application'"'"' version '"'"'8.2.0'"'"' apply false\n    id '"'"'org.jetbrains.kotlin.android'"'"' version '"'"'1.9.22'"'"' apply false\n}\n' > build.gradle

# --- gradle-wrapper.properties ---
printf 'distributionBase=GRADLE_USER_HOME\ndistributionPath=wrapper/dists\ndistributionUrl=https\\://services.gradle.org/distributions/gradle-8.4-bin.zip\nzipStoreBase=GRADLE_USER_HOME\nzipStorePath=wrapper/dists\n' > gradle/wrapper/gradle-wrapper.properties

# --- Download gradle wrapper jar ---
curl -sL "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar" \
  -o gradle/wrapper/gradle-wrapper.jar || true

if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
  gradle wrapper --gradle-version 8.4 2>/dev/null || true
fi

# --- Download standard gradlew ---
curl -sL "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradlew" -o gradlew || true
# --- gradle.properties ---
printf "android.useAndroidX=true\nandroid.enableJetifier=true\norg.gradle.jvmargs=-Xmx2048m\n" > gradle.properties

chmod +x gradlew

# --- CI workflow ---

echo "Project structure ready!"
echo ""
find app/src -type f | sort
