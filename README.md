# RoadStatus
## How to build the code
Before building the project, please add the following line to your *local.properties*

`APP_KEY=YOUR_APP_KEY`
where as
`YOUR_APP_KEY` is the *APP Key* provided by TFL to access the APIs

Please Note: The app will not work if correct value is not set for this variables.

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
This code is to demo Clean Architecture using Dagger, Retrofit, coroutines, MVVM pattern and Kotlin in an Android App.
There might be scope of improvement in this piece of code. Any suggestions are welcome.

