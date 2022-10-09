## Integration

1. Add the maven repository to `android/build.gradle` file:

```
allprojects {
    repositories {
		// make sure to add this before other repos
		maven { url "https://cdn.veriff.me/android/" } // <------------ ADD THIS

        google()
        mavenCentral()
    }
}


```