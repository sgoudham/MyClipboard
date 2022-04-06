[license]: https://img.shields.io/github/license/sgoudham/MyClipboard
[maven-central]: https://img.shields.io/maven-central/v/me.goudham/MyClipboard.svg?label=Maven%20Central
[build-status]: https://goudham.me/jenkins/job/sgoudham/job/MyClipboard/job/release/badge/icon
[codecov]: https://codecov.io/gh/sgoudham/MyClipboard/branch/main/graph/badge.svg?token=F4LKql7rIq
[issues]: https://img.shields.io/github/issues/sgoudham/MyClipboard?label=issues
[pull-requests]: https://img.shields.io/github/issues-pr/sgoudham/MyClipboard
[fossa]: https://app.fossa.com/api/projects/git%2Bgithub.com%2Fsgoudham%2FMyClipboard.svg?type=shield

![fossa]
![license]
![maven-central]
![build-status]
![codecov]
![issues]
![pull-requests]

# MyClipboard
Utility Library to Access and Manipulate the Default System Clipboard

# About
The inspiration for this project came from my frustration of macOS not having clipboard history 
built-in unlike Windows. This library will allow you to access the system clipboard and manipulate it. 

# Configuration

Follow the steps below for a basic implementation: 

```java
public class Main {
    public static void main(String[] args) {
        // Retrieve an instance of MyClipboard
        MyClipboard myClipboard = MyClipboard.getSystemClipboard();

        // Start listening for clipboard notifications 
        myClipboard.startListening();

        // Add event listeners for different types of events (Text, Image & File)
        myClipboard.addEventListener((TextEvent) (oldContent, newContent) -> {
            String oldText = oldContent.getText();
            BufferedImage oldImage = oldContent.getImage();
            List<File> oldFiles = oldContent.getFiles();
            System.out.println("Old Text: " + oldText);
            System.out.println("Old Image: " + oldImage);
            System.out.println("Old File: " + oldFiles);
            System.out.println("New String Content: " + newContent);
        });

        myClipboard.addEventListener((ImageEvent) (oldContent, newContent) -> {
            String oldText = oldContent.getText();
            BufferedImage oldImage = oldContent.getImage();
            List<File> oldFiles = oldContent.getFiles();
            System.out.println("Old Text: " + oldText);
            System.out.println("Old Image: " + oldImage);
            System.out.println("Old File: " + oldFiles);
            System.out.println("New Image Content: " + newContent);
        });

        myClipboard.addEventListener((FileEvent) (oldContent, newContent) -> {
            String oldText = oldContent.getText();
            BufferedImage oldImage = oldContent.getImage();
            List<File> oldFiles = oldContent.getFiles();
            System.out.println("Old Text: " + oldText);
            System.out.println("Old Image: " + oldImage);
            System.out.println("Old File" + oldFiles);
            System.out.println("New File Content: " + newContent);
        });
        
        // Insert into the clipboard
        myClipboard.insert("exampleContent");
        
        // Insert and notify MyClipboard of the new content
        myClipboard.insertAndNotify("exampleContent");

        // Set monitoring for clipboard types
        myClipboard.setImageMonitored(true || false);
        myClipboard.setTextMonitored(true || false);
        myClipboard.setFileMonitored(true || false);
        
        // Toggle monitoring for clipboard types
        myClipboard.toggleTextMonitored();
        myClipboard.toggleImagesMonitored();
        myClipboard.toggleFilesMonitored();
        
        // Stop listening for clipboard notifications
        myClipboard.stopListening();
    }
}
```


# Windows / *Unix
This approach differs from the macOS section below as Windows/*Unix properly notify the program with global clipboard events.
This allows for a more event-driven approach as lostOwnership() is triggered whenever the clipboard has lost ownership - clipboard
has new content within it - and the contents can be observed by multiple consumers.

# macOS
Unlike the aforementioned event-driven approach, macOS unfortunately is not very good at notifying the program if the 
system clipboard has changed. To query the system clipboard contents, we need to employ a polling schedule. I have chosen
**200ms** to ensure that images and large files can be copied over as well as reducing the load on the CPU.

# Installation

Latest Stable Version: ![maven-central]
<p>Be sure to replace the <strong>VERSION</strong> key below with the one of the versions shown above!</p>

**Maven**
```xml
<!-- https://mvnrepository.com/artifact/me.goudham/MyClipboard -->
<dependency>
    <groupId>me.goudham</groupId>
    <artifactId>MyClipboard</artifactId>
    <version>VERSION</version>
</dependency>
```

**Gradle**
```gradle
repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/me.goudham/MyClipboard
    implementation group: 'me.goudham', name: 'MyClipboard', version: 'VERSION'
}
```

# License 
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fsgoudham%2FMyClipboard.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fsgoudham%2FMyClipboard?ref=badge_large)
