# Android app using Jetpack compose, Hilt, Retrofit and Glide
This is an app I built to learn how to write an android app. There is a backend part which can be found [here](https://github.com/AceBasket/preteirb-api)

The last version pushed to the main branch is set up to connect to the deployed version on my Azure account. To use a local version, 
go to the app/src/main/java/com/example/preteirb/common/Constants.kt file and change the `BASE_URL` constant to the base url of your running backend server. 
If your backend server is running locally on your machine, you can run the app in the emulator and set `BASE_URL` to `http://10.0.2.2:8000/`. Everything else 
should run by itself if you use Android Studio to run the app. 
