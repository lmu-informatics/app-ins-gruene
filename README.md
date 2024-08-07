# App Ins Grüne
![Flyer_AppInsGruene_GitHub](https://user-images.githubusercontent.com/18674912/119205995-30b79680-ba9a-11eb-82f5-3b022ff5d3dc.jpg)

<!--
[<img src="https://f-droid.org/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80">](https://f-droid.org/repository/browse/?fdid=de.lmu.treeapp)
-->
[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png"
alt="Download from Google Play"
height="80">](https://play.google.com/store/apps/details?id=de.lmu.treeapp)
[<img src=".github/assets/direct-apk-download.png"
alt="Direct apk download"
height="80">](https://github.com/mimuc/app-ins-gruene/releases/latest)

# Table of Contents
- [About the App](#about-the-App)
  - [Core Functions](#core-Functions)
- [Instructions](#instructions)
  - [Getting Started](#getting-started)
    - [What do you need to run the project](#what-do-you-need-to-run-the-project)
    - [How to get the project](#how-to-get-the-project)
    - [How to contribute](#how-to-contribute)
    - [How to access the database](#how-to-access-the-database)
    - [Update Licenses and Dependencies](#update-Licenses-and-Dependencies)
  - [How to run tests](#how-to-run-tests)
  - [Deployment and Release](#deployment-and-release)
- [Authors](#authors)
- [Notes](#notes)

# About the App  
'App Ins Grüne' is a digital educational trail about native trees in Germany. It is intended to bring children (and adults) closer to native trees in a playful way. The focus is on the smartphone as a companion in nature: many minigames in the app require interaction with the environment, e.g. photographing the leaves of the trees. The photos are collected in profiles for each tree and can be viewed again at any time.  
After downloading the app, it does not need a connection while on the move. In the profiles, however, videos provided by BISA [(Biodiversität im Schulalltag)](https://www.bisa100.de/) can be viewed with an existing internet connection. All games can be played multiple times.  

## Core Functions
- Minigames on 10 different native tree species
- Multiplayer mode for some games available
- Profiles for each tree for further information
- A collection of photos and creative contributions created by oneself
- A user profile for you and your friends with information such like the own favorite tree and much more  

# Instructions
This Android Project is used to manage the source code of a biodidactic tree app.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
See deployment for notes on how to deploy the project on a live system.

### What do you need to run the project
- [Android Studio](https://developer.android.com/studio) or [IntelliJ (Ultimate)](https://www.jetbrains.com/idea/download)
- [Git](https://git-scm.com/)

### How to get the project
- Open your terminal/cmd & switch to the directory you want to download your project: 
```
cd path/to/your/directory
```
- Clone the project: 
```
git clone https://github.com/mimuc/app-ins-gruene.git
```
- Now you are able to open the project in Android Studio.

### How to contribute
- Create an [issue](https://github.com/mimuc/app-ins-gruene/issues), so we can discuss and monitor the topic.
- You can check and manage the state of your issues in the [Issue-Board](https://github.com/mimuc/app-ins-gruene/projects). 
- Use the branch `master` as basis for a new branch `git checkout 123-my-issue`, where `123` is the issue number. Don't forget to update your master before checkout with `git pull origin master`.
- Make your changes, consider formatting them with your IDE (e.g. `Ctrl+Alt+L` in IntelliJ / AndroidStudio).
- Commit your changes, consider [cross-linking](https://docs.github.com/en/github/managing-your-work-on-github/linking-a-pull-request-to-an-issue) the resolved issue with `(closes #123)` in your commit message.
- Create a Merge-Request for your branch (also consider a cross-link to an existing issue).

You may want to use the Version-Control-System (VCS) [of IntelliJ](https://www.jetbrains.com/help/idea/version-control-integration.html), to easier track and compare your changes.

### How to access the database
The prepopulated database is stored in `tree-app-android/app/src/main/assets/databases/content.db`. You can access and edit it with every database tool for SQLite.

We recommend one of the following tools:
- [Database Tools and SQL](https://www.jetbrains.com/help/idea/relational-databases.html): **IntelliJ Ultimate** → Settings (`Ctrl + Alt + S`) → Plugins → Marketplace → Database Tools and SQL _(see also the [documentation](https://www.jetbrains.com/help/idea/accessing-android-sqllite-databases-from-product.html))_
- [Database Inspector](https://developer.android.com/studio/inspect/database) _(only for debugging)_: **Android Studio** → Menu Bar → View → Tool Windows → Database Inspector

### Update Licenses and Dependencies
We use the [License Tools Plugin for Android](https://github.com/cookpad/LicenseToolsPlugin) to maintain the dependencies. Copy the file `app/src/main/assets/licenses.yml` to `app` and exexute `./gradlew checkLicenses`. Copy the missing licenses to the `generated` list. Add custom dependencies to the `custom` list.

## How to run tests
```
./gradlew clean test
```

Detect lint issues:
```
./gradlew lint
```

## Deployment and Release
Read the docs for [Deployment](docs/playstore/Deployment.md).

# Authors
'App Ins Grüne' was created by computer science and art students in various courses at the Ludwig-Maximilians-University of Munich. However, it is not an official product of the LMU. The app was developed in cooperation with BISA [(Biodiversität im Schulalltag)](https://www.bisa100.de/) and the Institute for Biology Education of the LMU.     

With a lot of commitment and attention to detail, a learning app was created that uses numerous minigames to convey knowledge about the local tree species.  

Since the development of the app will not be continued by us students, everyone is welcome to further develop on this project, to improve the app and to fix bugs. The source code is subject to the MIT license.  

***
Development:  
- Summer semester 2019: Practical Course [Design Workshop II](http://www.medien.ifi.lmu.de/lehre/ss19/dw2/)
- 2019-2020: Further development by a group of three students of the Design Workshop II
- Winter semester 2020/21: Practical Course [Development of Media Systems III](https://www.medien.ifi.lmu.de/lehre/ws2021/pem3/)
***

# Notes
- Built with [Gradle](https://gradle.org/) 
- Versioning with [Git](http://git.org/)
- Documentation written in [Markdown](https://guides.github.com/features/mastering-markdown/)
