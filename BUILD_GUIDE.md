# Al-Nasser App - Complete Build Guide

This guide provides detailed, step-by-step instructions for building the Al-Nasser Android app from source code to installable APK.

## Prerequisites

Before you begin, ensure you have the following software installed on your computer.

### Required Software

**Android Studio** (Latest stable version)
- Download from: https://developer.android.com/studio
- Recommended version: Hedgehog (2023.1.1) or newer
- Installation size: ~3 GB

**Java Development Kit (JDK) 17**
- Android Studio includes JDK, but you can also install separately
- Download from: https://www.oracle.com/java/technologies/downloads/
- Verify installation: `java -version`

**Android SDK Components**
- Android SDK Platform 34 (Android 14)
- Android SDK Build-Tools 34.0.0
- Android SDK Platform-Tools
- Android Emulator (optional, for testing)

### System Requirements

**For Windows**
- OS: Windows 10 (64-bit) or Windows 11
- RAM: 8 GB minimum, 16 GB recommended
- Storage: 10 GB free space minimum
- Screen resolution: 1280 x 800 minimum

**For macOS**
- OS: macOS 10.14 (Mojave) or newer
- RAM: 8 GB minimum, 16 GB recommended
- Storage: 10 GB free space minimum
- Screen resolution: 1280 x 800 minimum

**For Linux**
- OS: Ubuntu 18.04 LTS or newer (or equivalent)
- RAM: 8 GB minimum, 16 GB recommended
- Storage: 10 GB free space minimum
- Screen resolution: 1280 x 800 minimum

## Step 1: Install Android Studio

### Windows Installation

1. Download Android Studio installer from https://developer.android.com/studio
2. Run the downloaded `.exe` file
3. Follow the installation wizard
4. Select "Standard" installation type
5. Choose your preferred UI theme
6. Wait for component downloads to complete
7. Click "Finish" when installation is complete

### macOS Installation

1. Download Android Studio `.dmg` file
2. Open the downloaded file
3. Drag Android Studio to Applications folder
4. Launch Android Studio from Applications
5. Follow the setup wizard
6. Select "Standard" installation
7. Wait for SDK components to download

### Linux Installation

1. Download Android Studio `.tar.gz` file
2. Extract to desired location:
   ```bash
   tar -xzf android-studio-*.tar.gz
   ```
3. Navigate to extracted folder:
   ```bash
   cd android-studio/bin
   ```
4. Run the studio script:
   ```bash
   ./studio.sh
   ```
5. Follow the setup wizard

## Step 2: Configure Android SDK

After installing Android Studio, you need to ensure the correct SDK components are installed.

### Open SDK Manager

1. Launch Android Studio
2. Click on "More Actions" on welcome screen
3. Select "SDK Manager"
4. Or go to: `Tools → SDK Manager` if project is open

### Install Required Components

In the SDK Manager window, ensure the following are checked and installed:

**SDK Platforms Tab**
- ✅ Android 14.0 (API Level 34) - Check "Show Package Details"
  - Android SDK Platform 34
  - Sources for Android 34
  - Google APIs Intel x86_64 Atom System Image (for emulator)

**SDK Tools Tab**
- ✅ Android SDK Build-Tools 34.0.0
- ✅ Android SDK Platform-Tools
- ✅ Android SDK Tools
- ✅ Android Emulator
- ✅ Intel x86 Emulator Accelerator (HAXM installer)

Click "Apply" to download and install selected components.

## Step 3: Open the Al-Nasser Project

### Extract Project Files

If you received the project as a ZIP file, extract it to a location on your computer where you have write permissions, such as:
- Windows: `C:\Users\YourName\AndroidProjects\AlNasser`
- macOS: `/Users/YourName/AndroidProjects/AlNasser`
- Linux: `/home/yourname/AndroidProjects/AlNasser`

### Open in Android Studio

1. Launch Android Studio
2. Click "Open" on the welcome screen
3. Navigate to the extracted `AlNasser` folder
4. Select the folder and click "OK"
5. Wait for Android Studio to load the project
6. Gradle will automatically start syncing

### Gradle Sync

When you first open the project, Android Studio will:
1. Read the `build.gradle` files
2. Download required dependencies
3. Index the project files
4. Build the project structure

This process may take 5-15 minutes depending on your internet speed. You'll see a progress bar at the bottom of the window.

**If Gradle sync fails:**
- Check your internet connection
- Go to `File → Invalidate Caches / Restart`
- Try `File → Sync Project with Gradle Files`
- Check the "Build" tab at the bottom for error messages

## Step 4: Configure Project Settings

### Update SDK Location (if needed)

If Android Studio cannot find the SDK:

1. Open `local.properties` file in project root
2. Update the SDK path:
   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   ```
   
**Default SDK locations:**
- Windows: `C:\Users\YourName\AppData\Local\Android\Sdk`
- macOS: `/Users/YourName/Library/Android/sdk`
- Linux: `/home/yourname/Android/Sdk`

### Verify Build Configuration

1. Open `File → Project Structure`
2. Check "Project" settings:
   - Gradle Version: 8.2
   - Android Gradle Plugin Version: 8.2.0
   - Gradle JDK: JDK 17
3. Click "OK" to save

## Step 5: Build the Project

### Clean Build

Before building the APK, perform a clean build to ensure everything is fresh:

1. Go to `Build → Clean Project`
2. Wait for the process to complete
3. Then go to `Build → Rebuild Project`
4. Wait for rebuild to finish (may take 2-5 minutes)

### Check for Errors

After rebuilding, check the "Build" tab at the bottom:
- ✅ Green check mark = Build successful
- ❌ Red X = Build failed (check error messages)

**Common build errors and solutions:**

**Error: "SDK location not found"**
- Solution: Update `local.properties` with correct SDK path

**Error: "Failed to resolve dependencies"**
- Solution: Check internet connection, sync Gradle again

**Error: "Unsupported Gradle version"**
- Solution: Update Gradle in `gradle-wrapper.properties`

**Error: "Compilation failed"**
- Solution: Check for syntax errors in Kotlin files

## Step 6: Build the APK

### Build Debug APK (For Testing)

The debug APK is unsigned and suitable for testing on your own devices.

**Using Menu:**
1. Go to `Build → Build Bundle(s) / APK(s) → Build APK(s)`
2. Wait for build to complete (1-3 minutes)
3. A notification will appear: "APK(s) generated successfully"
4. Click "locate" in the notification

**APK Location:**
```
AlNasser/app/build/outputs/apk/debug/app-debug.apk
```

**Using Terminal (Alternative):**
```bash
cd AlNasser
./gradlew assembleDebug
```

### Build Release APK (For Distribution)

The release APK should be signed for distribution to users.

#### Step 6.1: Generate Signing Key

1. Go to `Build → Generate Signed Bundle / APK`
2. Select "APK" and click "Next"
3. Click "Create new..." under Key store path
4. Fill in the form:
   - **Key store path**: Choose location (e.g., `alnasser-release.keystore`)
   - **Password**: Create a strong password (remember this!)
   - **Alias**: `alnasser`
   - **Alias password**: Same or different password
   - **Validity**: 25 years (default)
   - **Certificate**:
     - First and Last Name: Your Name
     - Organization: Al-Nasser
     - City: Your City
     - State: Your State
     - Country Code: Your Country (e.g., US, SA)
5. Click "OK"

**IMPORTANT**: Save your keystore file and passwords securely! You'll need them for all future app updates.

#### Step 6.2: Sign the APK

1. After creating keystore, you'll return to the signing dialog
2. Verify the information is correct
3. Click "Next"
4. Select build variant: "release"
5. Check both signature versions (V1 and V2)
6. Click "Finish"
7. Wait for build to complete

**Release APK Location:**
```
AlNasser/app/build/outputs/apk/release/app-release.apk
```

**Using Command Line (After configuring signing):**
```bash
./gradlew assembleRelease
```

## Step 7: Test the APK

### Option A: Test on Physical Device

**Enable Developer Options:**
1. On your Android device, go to Settings
2. About Phone → Tap "Build Number" 7 times
3. Go back to Settings → Developer Options
4. Enable "USB Debugging"

**Install APK:**
1. Connect device to computer via USB
2. Accept "Allow USB debugging" prompt on device
3. In Android Studio, click the "Run" button (green triangle)
4. Select your device from the list
5. App will install and launch automatically

**Manual Installation:**
1. Transfer `app-debug.apk` to device
2. Open file manager on device
3. Tap the APK file
4. Allow "Install from unknown sources" if prompted
5. Tap "Install"

### Option B: Test on Emulator

**Create Emulator:**
1. In Android Studio, go to `Tools → Device Manager`
2. Click "Create Device"
3. Select device definition (e.g., Pixel 6)
4. Click "Next"
5. Select system image: "API 34" (Android 14)
6. Download if needed
7. Click "Next"
8. Name your emulator
9. Click "Finish"

**Run on Emulator:**
1. Click "Run" button in Android Studio
2. Select your emulator from the list
3. Wait for emulator to boot (first time may take 2-3 minutes)
4. App will install and launch

### Verify App Functionality

Test the following features:

**Splash Screen:**
- ✅ Logo displays correctly
- ✅ Arabic text "الناصر" is visible
- ✅ Transitions to input screen after 2.5 seconds

**Input Screen:**
- ✅ Enter height (e.g., 175)
- ✅ Enter weight (e.g., 70)
- ✅ Select gender
- ✅ Click Continue
- ✅ Loading indicator appears
- ✅ Transitions to mannequin screen

**Mannequin Screen:**
- ✅ 3D mannequin renders correctly
- ✅ Can rotate mannequin by dragging
- ✅ Can zoom with pinch gesture
- ✅ Animation buttons work (Idle, Walk, Turn, Wave)
- ✅ "+" button adds new mannequin
- ✅ "Try Clothes" button opens clothing panel

**Clothing Panel:**
- ✅ Category filters work
- ✅ Clothing items display
- ✅ Can select and apply clothing
- ✅ Size recommendations show
- ✅ Prices display correctly

## Step 8: Distribute the APK

### For Internal Testing

**Share Debug APK:**
1. Locate `app-debug.apk`
2. Upload to cloud storage (Google Drive, Dropbox, etc.)
3. Share link with testers
4. Testers download and install on their devices

### For Production Release

**Google Play Store:**
1. Create Google Play Developer account ($25 one-time fee)
2. Go to https://play.google.com/console
3. Create new app
4. Fill in app details
5. Upload `app-release.apk` (signed)
6. Complete store listing
7. Submit for review

**Direct Distribution:**
1. Host `app-release.apk` on your website
2. Users download and install
3. Ensure APK is signed with release keystore

## Troubleshooting

### Build Fails with "Out of Memory"

**Solution:**
1. Open `gradle.properties`
2. Add or update:
   ```properties
   org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=1024m
   ```
3. Restart Android Studio
4. Rebuild project

### APK Won't Install on Device

**Solution:**
1. Uninstall any previous version of the app
2. Enable "Install from unknown sources"
3. Check device has enough storage space
4. Verify device is running Android 8.0 or higher

### Gradle Sync Keeps Failing

**Solution:**
1. Check internet connection
2. Clear Gradle cache:
   ```bash
   rm -rf ~/.gradle/caches/
   ```
3. Invalidate caches: `File → Invalidate Caches / Restart`
4. Update Gradle version if needed

### App Crashes on Launch

**Solution:**
1. Check device supports OpenGL ES 3.0
2. View logcat in Android Studio for error messages
3. Ensure device meets minimum requirements
4. Try on different device or emulator

## Performance Tips

### Optimize Build Speed

1. Enable Gradle daemon in `gradle.properties`:
   ```properties
   org.gradle.daemon=true
   org.gradle.parallel=true
   org.gradle.configureondemand=true
   ```

2. Use Gradle build cache:
   ```properties
   org.gradle.caching=true
   ```

3. Increase heap size:
   ```properties
   org.gradle.jvmargs=-Xmx4096m
   ```

### Reduce APK Size

1. Enable ProGuard for release builds (already configured)
2. Use APK Analyzer: `Build → Analyze APK`
3. Remove unused resources
4. Compress images and assets

## Next Steps

After successfully building the APK:

1. **Test thoroughly** on multiple devices
2. **Gather feedback** from users
3. **Fix bugs** and improve features
4. **Update version** in `app/build.gradle`
5. **Rebuild and redistribute**

## Support

If you encounter issues not covered in this guide:

1. Check Android Studio error messages
2. Search for error on Stack Overflow
3. Consult Android Developer documentation
4. Contact Al-Nasser technical support

## Summary Checklist

- [ ] Android Studio installed
- [ ] SDK components installed
- [ ] Project opened successfully
- [ ] Gradle sync completed
- [ ] Project builds without errors
- [ ] Debug APK generated
- [ ] Release keystore created
- [ ] Release APK signed and generated
- [ ] APK tested on device/emulator
- [ ] All features working correctly
- [ ] APK ready for distribution

**Congratulations!** You have successfully built the Al-Nasser Android app. The APK is ready to install on Android devices running Android 8.0 or higher.
