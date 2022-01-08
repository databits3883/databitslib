# databitslib
Library for FRC project development, created by FRC 3883, the Data Bits.
Built with wpilib.

### Include in a project
To include databitslib in a gradle project, first add jitpack as a repository in 
your build.gradle. Make sure jitpack is the last repository in your list:
```
repositories{
    ...
    maven { url "https://jitpack.io" }
}
```
Then add databitslib as a dependency:
```
dependencies {
    implementation 'com.github.databits3883:databitslib:0.1.0-alpha.1
}