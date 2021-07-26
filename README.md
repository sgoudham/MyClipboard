[license]: https://img.shields.io/github/license/sgoudham/MyClipboard
[maven-central]: https://img.shields.io/maven-central/v/me.goudham/MyClipboard.svg?label=Maven%20Central
[build-status]: https://goudham.me/jenkins/job/MyClipboard/job/release/badge/icon

[comment]: <> ([codecov]: )
[issues]: https://img.shields.io/github/issues/sgoudham/MyClipboard?label=issues
[pull-requests]: https://img.shields.io/github/issues-pr/sgoudham/MyClipboard
[fossa]: https://app.fossa.com/api/projects/git%2Bgithub.com%2Fsgoudham%2FMyClipboard.svg?type=shield

![fossa]
![license]
![maven-central]
![build-status]
![issues]
![pull-requests]

# MyClipboard
Utility Library to Access and Manipulate the Default System Clipboard

# About
The inspiration for this project came from my frustration of macOS not having clipboard history 
built-in unlike Windows. This library will allow you to access the system clipboard and manipulate it. 

# Configuration
TODO

# Windows / *Unix
This approach differs from the macOS section below as Windows/*Unix properly notify the program with global clipboard events.
This allows for a more event-driven approach as lostOwnership() is triggered whenever the clipboard has lost ownership - clipboard
has new content within it - and the contents can be observed by multiple consumers.

# macOS
Unlike the aforementioned event-driven approach, macOS unfortunately is not very good at notifying the program if the 
system clipboard has changed. To query the system clipboard contents, we need to employ a polling schedule. I have chosen
**350ms** to ensure that images and large files can be copied over as well as reducing the load on the CPU.


# Contributing
TODO

# License 
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fsgoudham%2FMyClipboard.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fsgoudham%2FMyClipboard?ref=badge_large)