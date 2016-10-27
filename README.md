# LETO-Toggl_Android

An OpenSource Location Based Time Tracking App  
![Platform](https://img.shields.io/badge/platform-Android-green.svg) [![License](https://img.shields.io/aur/license/yaourt.svg)](https://github.com/letolab/LETO-Toggl_iOS/blob/master/LICENSE)

[![image](https://play.google.com/intl/en_gb/badges/images/badge_new.png)](https://play.google.com/store/apps/details?id=beacons.leto.com.letoibeacons)

It uses [Toggl](https://toggl.com/) service for time tracking. 

## Project Set Up

The project is developed on Android Studio and 19 is the minimum SDK supported verison.. To start working on the project, clone this repository on your machine using your Git client of choice (e.g. [SourceTree](http://www.sourcetreeapp.com/)) or by running the following command in a Terminal window:

	git clone https://github.com/letolab/LETO-Toggl_Android.git

LETO Toggl uses [Fabric](https://fabric.io) for crash reports. Please set it up on `AndroidManifest.xml` with the key `io.fabric.ApiKey` or remove Fablic from the project.

Before you run your application, you need a Google Maps API key.
[Follow the directions here](https://developers.google.com/maps/documentation/android/signup) on how to set up a new key.

## Git Branching Strategy

The base for your work and current state of app will be on the 'develop' branch. This branch should always be kept in a deployable state. The 'master' branch will always be up-to-date with the latest deployed version of your code (Google play Store released). Features and fixes are worked on separate branches.

## License

 LETO Toggl is a location based time tracking App.
    Copyright (C) 2016  LETO

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

   [![img](https://d1orqdsmyxzawu.cloudfront.net/dist/leto/emails/2/leto-logo-black-email.png)](https://weareleto.com/)
