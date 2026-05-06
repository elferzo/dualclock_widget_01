#!/bin/bash
# Reconstructs full Android project structure from flat repo files
set -e

echo "Setting up Android project structure..."

# --- Directory structure ---
mkdir -p app/src/main/kotlin/com/dualclock/widget
mkdir -p app/src/main/res/layout
mkdir -p app/src/main/res/xml
mkdir -p app/src/main/res/values
mkdir -p .github/workflows

# --- Kotlin sources ---
cp DualClockWidget.kt     app/src/main/kotlin/com/dualclock/widget/
cp ClockUpdateService.kt  app/src/main/kotlin/com/dualclock/widget/
cp BootReceiver.kt        app/src/main/kotlin/com/dualclock/widget/

# --- Resources ---
cp widget_layout.xml  app/src/main/res/layout/
cp widget_info.xml    app/src/main/res/xml/
cp strings.xml        app/src/main/res/values/

# --- Manifest ---
cp AndroidManifest.xml app/src/main/

# --- Gradle files ---
cp build.gradle    app/
cp settings.gradle .

# --- Root build.gradle (project-level) ---
cat > build.gradle << 'EOF'
plugins {
    id 'com.android.application' version '8.2.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.22' apply false
}
EOF

# --- gradle wrapper ---
cat > gradle/wrapper/gradle-wrapper.properties << 'EOF' 2>/dev/null || (mkdir -p gradle/wrapper && cat > gradle/wrapper/gradle-wrapper.properties << 'EOF2'
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.4-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOF2
)
EOF

mkdir -p gradle/wrapper
cat > gradle/wrapper/gradle-wrapper.properties << 'EOF'
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.4-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOF

# Download gradle wrapper jar
curl -sL "https://raw.githubusercontent.com/nicowillis/android-gradle-wrapper/main/gradle/wrapper/gradle-wrapper.jar" \
  -o gradle/wrapper/gradle-wrapper.jar 2>/dev/null || \
  (cd /tmp && wget -q https://services.gradle.org/distributions/gradle-8.4-bin.zip -O gradle.zip && \
   unzip -q gradle.zip && cp gradle-8.4/lib/plugins/gradle-wrapper-8.4.jar \
   $GITHUB_WORKSPACE/gradle/wrapper/gradle-wrapper.jar 2>/dev/null) || true

# Use gradle wrapper from SDK if available
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
  gradle wrapper --gradle-version 8.4
fi

# --- gradlew script ---
cat > gradlew << 'EOF'
#!/bin/sh
exec "$(dirname "$0")/gradle/wrapper/gradle-wrapper" "$@"
EOF

# Use standard gradlew
curl -sL "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradlew" -o gradlew 2>/dev/null || true
chmod +x gradlew

# --- CI workflow ---
mkdir -p .github/workflows
cp build_apk.yml .github/workflows/

echo "Project structure ready!"
echo ""
echo "Structure:"
find app/src -type f | sort
