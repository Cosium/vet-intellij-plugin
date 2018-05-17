# vet-intellij-plugin
IntelliJ plugin for https://github.com/Cosium/vet

### Build

IntelliJ is not fully usable on JVM 9+. Therefore this plugin must be built using JDK 8.
Vet core is compatible Java 8, but built using Java 9 in order to be JLinked.

Consequently, as long as this situation lasts, to save the author mental health, Vet core source must be pulled into the current project using symbolic link.

```bash
git clone https://github.com/Cosium/vet
git clone https://github.com/Cosium/vet-intellij-plugin
cd vet-intellij-plugin/src/main/java/com/cosium
ln -sfn ../../../../../../vet/src/main/java/com/cosium/vet/ .
```