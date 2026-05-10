## private cps_project
wku cps 2231 final project - simple_flight_booking_system.

## configure this project first if it does not run normally in your IDE.
use path-to-javafx to define.
/cmd/
 From the project root (where src/ is located). Adjust the JavaFX SDK path if different.
```console
mkdir -p out
```
 collect all .java sources (works in Git Bash / WSL / macOS / Linux)
 
```console
find src -name "*.java" > sources.txt
```
 compile with JavaFX module path

```console
javac --module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml -d out @sources.txt
```

 run the app with the same JavaFX module path

```console
java --module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp out app.Main
```

 If you see:
```

WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by com.sun.glass.utils.NativeLibLoader in module javafx.graphics (file:/C:/javafx-sdk-25.0.1/lib/javafx.graphics.jar)
WARNING: Use --enable-native-access=javafx.graphics to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled

```
. Expaination:
JavaFX is loading its native (C++) libraries using System.load(), and since Java 21 tightened security around this, 
the JVM warns you about it.


If catch error: package does not exist, change dir: out -> bin.


* **POWERED BY GOOGLE-ANTIGRAVITY**
