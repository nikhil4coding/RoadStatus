# RoadStatus
## How to build the code
The `APP_KEY` and `APP_BASE_URL` are congfiguratble.These variables are found in *app/build.gradle*

Please Note: The app will not work if correct values are not set for these variables.

Please replace 

`APP_KEY` with actual API key

and 

`APP_BASE_URL` with actual base API URL (e.g. https://api.tfl.gov.uk)

To *build* the app run the following commands on terminal

On Windows: `gradlew assembleDebug`

On Mac or Linux: `./gradlew assembleDebug`

## How to run the output

To *run* the app on an Android device, please use the following command from terminal

On Windows: `gradlew installDebug`

On Mac or Linux: `./gradlew installDebug`

## How to run tests

To *run* the tests, please use the following command from termial

On Windows: `gradlew testDebugUnitTest`

On Mac or Linux: `./gradlew testDebugUnitTest`

## Assumptions
While writing the code following assumptions are made
- Device will be aways connected to internet
- App will run on Potrait mode only

## Extra
This code is to demo Clean Architecture using Dagger, Retrofit, coroutines and Kotlin in an Android App.
There might be scope of improvement in this piece of code. Any suggestions are welcome.

