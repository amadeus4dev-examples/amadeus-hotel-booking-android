# Amadeus demo - Android Hotel Booking Engine

[![Discord](https://img.shields.io/discord/696822960023011329?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/cVrFBqx)

This prototype shows how to use the [Android SDK](https://github.com/amadeus4dev/amadeus-android) to build a Hotel Booking Engine.
To know more about how we built this prototype please check our [Android Hotel Booking app tutorial](https://developers.amadeus.com/blog/android-hotel-booking-app-tutorial).


This demo combines 3 different APIs:
* [Airport & City Search](https://developers.amadeus.com/self-service/category/air/api-doc/airport-and-city-search/api-reference)

* [Hotel Search](https://developers.amadeus.com/self-service/category/hotel/api-doc/hotel-search/api-reference)

* [Hotel Booking](https://developers.amadeus.com/self-service/category/hotel/api-doc/hotel-booking/api-reference)

## How to run the project locally

* Clone the repository:
```sh
git https://github.com/amadeus4dev/amadeus-hotel-booking-android.git
```

* Install latest [Android Studio version](https://developer.android.com/studio)
* Open project's root folder `build.gradle` with Android Studio `File > Open`
* For authentication: Open the `.gradle` folder (root user) and edit/create the `gradle.properties` file. Add your [API Key and API Secret](https://developers.amadeus.com/get-started/get-started-with-self-service-apis-335) in the `gradle.properties`:
```gradle
amadeus.api.key="REPLACE_BY_YOUR_API_KEY"
amadeus.api.secret="REPLACE_BY_YOUR_API_SECRET"
```
* Once your credentials are setup, click `Run` in Android Studio.

## License

This library is released under the [MIT License](LICENSE).

## Help

Our [developer support team](https://developers.amadeus.com/support) is here
to help you. You can find us on
[StackOverflow](https://stackoverflow.com/questions/tagged/amadeus) and
[email](mailto:developers@amadeus.com).

