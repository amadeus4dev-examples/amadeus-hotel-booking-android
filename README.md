# Amadeus demo - Android Hotel Booking Engine

[![Discord](https://img.shields.io/discord/696822960023011329?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/cVrFBqx)

## Amadeus Hotel Booking

With the Hotel Booking API you are able to integrate booking capabilities directly in your application. In this prototype we demonstrate the end-to-end booking process, calling the following endpoints:

* [Hotel Search](https://developers.amadeus.com/self-service/category/hotel/api-doc/hotel-search/api-reference)
  * GET /shopping/hotel-offers to find hotels
  * GET /shopping/hotel-offers/by-hotel to view rooms given a hotel
  * GET /shopping/hotel-offers/{offerId} to confirm room availability

* [Hotel Booking](https://developers.amadeus.com/self-service/category/hotel/api-doc/hotel-booking/api-reference)
  * POST /booking/hotel-bookings to book the room

## Use your credentials

App `build.gradle` file will read your credentials from your user `gradle.properties` configuration. Project does not come with default credentials, so be sure to setup your account and get yours [here](https://developers.amadeus.com/get-started/get-started-with-self-service-apis-335).

Open `.gradle` folder in your root user and edit/create `gradle.properties`. You should put your credentials like this.

```gradle
amadeus.api.key="XXXXXXXXXXXXXXX"
amadeus.api.secret="XXXXXXXXXXXXXXX"
```

## Setup

* Download project from Git
* Install latest [Android Studio version](https://developer.android.com/studio)
* Open project root folder build.gradle with Android Studio `File > Open`
* Once your credentials are setup, click `Run` green arrow.

For more details about the project, check out the official [article](https://developers.amadeus.com/blog/android-hotel-booking-app-tutorial).
