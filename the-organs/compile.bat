@echo off
if exist out rmdir /s /q out
mkdir out
for /r src\organs %%f in (*.java) do (
    javac --module-path javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,java.desktop -d out -cp src %%f
)