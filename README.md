# MusicApp
# About

This app displays Artists lists and its information retrieved from last.fm API using Kotlin and Retrofit
https://www.last.fm/api/intro

MainActivity screen displays top artists information by default and by clicking individual artists takes you to the detail page
https://www.last.fm/api/show/chart.getTopArtists

Implemented basic search  - Searches the artists based on artist name and display the results.

# Architecture

Used MVVM Architecture
Hilt for dependency injection
Used Databinding
Gide to render images
Secured API Key by adding it in gradle.properties.


# How to run the APP

- Create a developer account in https://www.last.fm/api/intro and obtain a API key.
- Configure API key in gradle.properties with key name "API_KEY"

