# The Organs - JavaFX Musical Instrument Simulator

A JavaFX application that simulates various human organs as musical instruments.

## How to Run

### Using VS Code (Recommended)

1. **Build the project**: Press `Ctrl+Shift+B` or go to Terminal → Run Build Task → compile
2. **Run the application**: Press `F5` or go to Run → Start Debugging → "Run The Organs"
3. **Clean build**: Terminal → Run Task → clean (to remove compiled files)

### Manual Compilation

```bash
# Compile using the batch file (recommended)
compile.bat

# Or manually compile each file:
for /r src\organs %%f in (*.java) do javac --module-path javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,java.desktop -d out -cp src %%f

# Run
java --module-path "out;javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml,java.desktop -m organs/organs.MainApp
```

## Controls

- **H** - Heart
- **L** - Lungs (hold to sustain)
- **T** - Trachea (hold to sustain)
- **D** - Diaphragm
- **S** - Stomach (hold to sustain)
- **K** - Kidneys
- **I** - Intestines (drag mouse in intestines box for varying pitch)
- **A** - Appendix
- **V** - Vocal Cords

## Project Structure

- `src/organs/` - Main application classes
- `src/organs/audio/` - Audio engine classes
- `src/organs/instruments/` - Individual organ instrument classes
- `javafx-sdk/` - JavaFX 21 SDK
- `resources/` - Application resources (body image, soundfont)
- `compile.bat` - Build script for Windows
- `out/` - Compiled class files (generated)

## Requirements

- Java 11 or higher
- JavaFX 21 SDK (included in project)