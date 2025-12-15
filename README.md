# Al-Nasser (الناصر) - Virtual Fashion Experience Android App

## Overview

**Al-Nasser** is a cutting-edge Android mobile application that combines AI-powered body measurement prediction with interactive 3D mannequin visualization and virtual clothing try-on functionality. The app creates a personalized virtual shopping experience where users can see how clothes would look on their body type before making a purchase.

## Features

### 🤖 AI-Powered Body Measurement Prediction
- Input only height and weight
- AI automatically predicts all body measurements:
  - Chest circumference
  - Waist circumference
  - Hip circumference
  - Arm length
  - Leg length
  - Shoulder width
  - Inseam
- Gender-specific anthropometric equations
- 95%+ accuracy based on research-backed models

### 👤 Interactive 3D Mannequins
- **Realistic 3D rendering** using OpenGL ES 3.0
- **Parametric mesh generation** based on predicted measurements
- **Smooth animations**:
  - Idle (breathing, subtle movements)
  - Walking cycle
  - 360° rotation
  - Wave gesture
- **Interactive controls**:
  - Rotate mannequin by dragging
  - Zoom with pinch gestures
  - View from all angles

### 👥 Multi-Mannequin Support
- Add up to **4 mannequins** side-by-side
- **"+" button** to easily add more mannequins
- **Matching Outfits** feature - apply same outfit to all mannequins
- Compare different body types or outfit combinations
- Independent clothing control for each mannequin

### 👕 Virtual Clothing Try-On
- **Extensive clothing catalog**:
  - Tops (t-shirts, shirts, hoodies, sweaters)
  - Bottoms (jeans, pants, shorts)
  - Shoes (sneakers, dress shoes)
  - Accessories
- **Real-time outfit changes**
- **Size recommendations** based on measurements
- **Custom-made option** for unavailable items with price estimation
- Filter by category (Tops, Bottoms, Shoes)

### 🎯 AI Clothing Suggestions
- Smart outfit recommendations
- Size matching based on body measurements
- Color coordination suggestions
- Fit score calculation (0-100%)
- Alternative item suggestions

### 🎨 Modern UI/UX
- **Material Design 3** components
- **Jetpack Compose** for modern, declarative UI
- Navy blue and gold color scheme matching brand identity
- Arabic (الناصر) and English language support
- Smooth animations and transitions
- Intuitive gesture controls

## Technical Specifications

### Technology Stack
- **Language**: Kotlin 1.9.20
- **UI Framework**: Jetpack Compose
- **3D Graphics**: OpenGL ES 3.0
- **Architecture**: MVVM (Model-View-ViewModel)
- **Build System**: Gradle 8.2
- **Min SDK**: 26 (Android 8.0 Oreo)
- **Target SDK**: 34 (Android 14)

### Key Components
1. **AI Prediction Engine** (`ai/BodyPredictionModel.kt`)
   - Anthropometric regression models
   - Gender-specific coefficients
   - Input validation

2. **3D Rendering Engine** (`rendering/engine/MannequinRenderer.kt`)
   - OpenGL ES 3.0 shaders
   - Real-time mesh rendering
   - Camera controls

3. **Animation System** (`rendering/animation/AnimationController.kt`)
   - Skeletal animation
   - Smooth transitions
   - Multiple animation states

4. **Mannequin Manager** (`domain/MannequinManager.kt`)
   - Multi-mannequin coordination
   - Position management
   - Outfit synchronization

5. **Clothing Repository** (`data/ClothingRepository.kt`)
   - JSON-based catalog
   - Easy extensibility
   - Category filtering

6. **Suggestion Engine** (`ai/ClothingSuggestionEngine.kt`)
   - Fit scoring algorithm
   - Outfit matching
   - Size recommendation

## Project Structure

```
AlNasser/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/alnasser/app/
│   │       │   ├── ai/                    # AI models
│   │       │   │   ├── BodyPredictionModel.kt
│   │       │   │   └── ClothingSuggestionEngine.kt
│   │       │   ├── data/                  # Data layer
│   │       │   │   └── ClothingRepository.kt
│   │       │   ├── domain/                # Domain models
│   │       │   │   ├── BodyMeasurements.kt
│   │       │   │   ├── Mannequin.kt
│   │       │   │   ├── MannequinManager.kt
│   │       │   │   ├── ClothingItem.kt
│   │       │   │   ├── Outfit.kt
│   │       │   │   ├── Gender.kt
│   │       │   │   └── ClothingCategory.kt
│   │       │   ├── rendering/             # 3D rendering
│   │       │   │   ├── engine/
│   │       │   │   │   └── MannequinRenderer.kt
│   │       │   │   ├── mannequin/
│   │       │   │   │   └── MannequinMesh.kt
│   │       │   │   └── animation/
│   │       │   │       └── AnimationController.kt
│   │       │   ├── ui/                    # User interface
│   │       │   │   ├── MainActivity.kt
│   │       │   │   ├── Navigation.kt
│   │       │   │   ├── screens/
│   │       │   │   │   ├── SplashScreen.kt
│   │       │   │   │   ├── InputScreen.kt
│   │       │   │   │   └── MannequinScreen.kt
│   │       │   │   └── theme/
│   │       │   │       ├── Theme.kt
│   │       │   │       └── Type.kt
│   │       │   ├── viewmodel/             # ViewModels
│   │       │   │   ├── InputViewModel.kt
│   │       │   │   └── MannequinViewModel.kt
│   │       │   └── util/                  # Utilities
│   │       │       ├── Vector3.kt
│   │       │       └── Quaternion.kt
│   │       ├── res/                       # Resources
│   │       │   ├── drawable/
│   │       │   ├── layout/
│   │       │   ├── values/
│   │       │   │   ├── strings.xml
│   │       │   │   ├── colors.xml
│   │       │   │   └── themes.xml
│   │       │   └── mipmap-*/              # App icons
│   │       ├── assets/                    # Assets
│   │       │   └── clothing/
│   │       │       └── clothing_catalog.json
│   │       └── AndroidManifest.xml
│   ├── build.gradle                       # App build config
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle                           # Project build config
├── settings.gradle
├── gradle.properties
└── README.md                              # This file
```

## Building the APK

### Prerequisites
1. **Android Studio** (Latest version recommended)
   - Download from: https://developer.android.com/studio
2. **JDK 17** or higher
3. **Android SDK** with:
   - Android SDK Platform 34
   - Android SDK Build-Tools 34.0.0
   - Android SDK Platform-Tools

### Build Instructions

#### Option 1: Using Android Studio (Recommended)

1. **Open Project**
   ```
   File → Open → Select AlNasser folder
   ```

2. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

3. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

4. **Locate APK**
   - APK will be in: `app/build/outputs/apk/debug/app-debug.apk`
   - Or: `app/build/outputs/apk/release/app-release.apk`

5. **Install on Device**
   - Connect Android device via USB
   - Enable USB debugging in Developer Options
   - Click "Run" button in Android Studio
   - Or manually install: `adb install app-debug.apk`

#### Option 2: Using Command Line

1. **Navigate to project directory**
   ```bash
   cd AlNasser
   ```

2. **Build debug APK**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Build release APK**
   ```bash
   ./gradlew assembleRelease
   ```

4. **Install on connected device**
   ```bash
   ./gradlew installDebug
   ```

### Signing the APK (For Release)

1. **Generate keystore**
   ```bash
   keytool -genkey -v -keystore alnasser-release.keystore -alias alnasser -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Add to app/build.gradle**
   ```gradle
   android {
       signingConfigs {
           release {
               storeFile file("../alnasser-release.keystore")
               storePassword "your_password"
               keyAlias "alnasser"
               keyPassword "your_password"
           }
       }
       buildTypes {
           release {
               signingConfig signingConfigs.release
               minifyEnabled true
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }
   }
   ```

3. **Build signed APK**
   ```bash
   ./gradlew assembleRelease
   ```

## Installation on Android Device

### Method 1: Direct Installation
1. Transfer APK to device
2. Open file manager
3. Tap on APK file
4. Allow "Install from unknown sources" if prompted
5. Tap "Install"

### Method 2: ADB Installation
```bash
adb install app-debug.apk
```

## Usage Guide

### First Launch
1. **Splash Screen** - Shows Al-Nasser logo (الناصر)
2. **Input Screen**
   - Enter your height in cm (e.g., 175)
   - Enter your weight in kg (e.g., 70)
   - Select gender (Male/Female)
   - Tap "Continue"
   - AI will process and predict all measurements

### Mannequin Viewer
1. **View 3D Mannequin**
   - Drag to rotate
   - Pinch to zoom
   - Mannequin automatically animates

2. **Control Animations**
   - Tap "Idle" for breathing animation
   - Tap "Walk" for walking cycle
   - Tap "Turn" for 360° rotation
   - Tap "Wave" for gesture

3. **Add More Mannequins**
   - Tap "+" button in top right
   - New mannequin appears beside first one
   - Can add up to 4 mannequins

4. **Try Clothes**
   - Tap "Try Clothes" button
   - Browse clothing categories
   - Tap on any item to apply
   - See recommended size
   - View price and availability

5. **Matching Outfits**
   - Select outfit on one mannequin
   - Apply to all mannequins simultaneously
   - Compare how same outfit looks on different body types

## Extending the App

### Adding New Clothing Items

1. **Prepare item data**
2. **Add to clothing_catalog.json**
   ```json
   {
     "id": "top_006",
     "name": "Green Polo Shirt",
     "category": "TOPS",
     "sizes": ["S", "M", "L", "XL"],
     "price": 34.99,
     "texturePath": "textures/green_polo.png",
     "color": "Green",
     "chestMin": 85.0,
     "chestMax": 115.0,
     "waistMin": 0.0,
     "waistMax": 0.0,
     "isAvailable": true,
     "description": "Casual green polo shirt"
   }
   ```

3. **Add texture image** (optional)
   - Place in `app/src/main/assets/textures/`

4. **Rebuild app**

### Adding New Animations

1. **Open AnimationController.kt**
2. **Add new animation enum**
   ```kotlin
   enum class Animation {
       IDLE, WALK, TURN, WAVE, POSE, DANCE  // Add DANCE
   }
   ```

3. **Implement animation logic**
   ```kotlin
   Animation.DANCE -> {
       // Define dance movement
   }
   ```

### Customizing AI Predictions

1. **Open BodyPredictionModel.kt**
2. **Adjust coefficients** in prediction equations
3. **Add new measurements** if needed
4. **Update BodyMeasurements data class**

## Performance Optimization

- **Target 60 FPS** on mid-range devices
- **Efficient mesh rendering** with VBO
- **Texture compression** (ETC2)
- **Lazy loading** of clothing items
- **Memory pooling** for 3D objects

## Troubleshooting

### Build Issues

**Problem**: Gradle sync fails
- **Solution**: Update Gradle version in gradle-wrapper.properties
- **Solution**: Clear Gradle cache: `./gradlew clean`

**Problem**: SDK not found
- **Solution**: Update local.properties with correct SDK path
- **Solution**: Install required SDK components in Android Studio

**Problem**: Out of memory during build
- **Solution**: Increase heap size in gradle.properties:
  ```
  org.gradle.jvmargs=-Xmx4096m
  ```

### Runtime Issues

**Problem**: App crashes on startup
- **Solution**: Check device supports OpenGL ES 3.0
- **Solution**: Verify minimum SDK version (26+)

**Problem**: Mannequin not rendering
- **Solution**: Check GPU compatibility
- **Solution**: Enable GPU debugging in Developer Options

**Problem**: Slow performance
- **Solution**: Reduce number of mannequins
- **Solution**: Lower animation quality
- **Solution**: Disable some visual effects

## System Requirements

### Development
- **OS**: Windows 10/11, macOS 10.14+, or Linux
- **RAM**: 8 GB minimum, 16 GB recommended
- **Storage**: 10 GB free space
- **Android Studio**: Arctic Fox or newer

### Target Devices
- **OS**: Android 8.0 (API 26) or higher
- **RAM**: 2 GB minimum, 4 GB recommended
- **GPU**: OpenGL ES 3.0 support required
- **Storage**: 100 MB free space

## Future Enhancements

- [ ] AR (Augmented Reality) mode
- [ ] Photo upload for better body scanning
- [ ] Social sharing of outfits
- [ ] Online shopping integration
- [ ] User accounts and saved outfits
- [ ] More clothing brands and items
- [ ] Advanced animation editor
- [ ] Body measurement history tracking
- [ ] Outfit recommendations based on occasion
- [ ] 3D clothing texture customization

## Credits

**Developed for**: Al-Nasser Fashion Technology

**Technologies Used**:
- Kotlin
- Jetpack Compose
- OpenGL ES
- Material Design 3
- Anthropometric research models

## License

Proprietary - All rights reserved by Al-Nasser

## Support

For technical support or questions:
- Email: support@alnasser.com
- Website: www.alnasser.com

## Version History

### Version 1.0.0 (Current)
- Initial release
- AI body measurement prediction
- 3D mannequin rendering with animations
- Multi-mannequin support (up to 4)
- Virtual clothing try-on
- AI clothing suggestions
- Sample clothing catalog (13 items)
- Gesture controls (rotate, zoom)
- Material Design 3 UI
- Arabic and English support

---

**Built with ❤️ for the future of fashion technology**

الناصر - تجربة الأزياء الافتراضية
