package com.example.faithportal.model

import com.google.gson.annotations.SerializedName

data class SpotifyResponse (
    @SerializedName("tracks") var tracks: WorshipMusic
)

data class WorshipMusic (
    @SerializedName("items") var items: List<TrackObject>
)

data class TrackObject (
    @SerializedName("album") var album: Album,
    @SerializedName("artists") var artists: List<SimplifiedArtistObject>,
    @SerializedName("external_urls") var external_urls: Link,
    @SerializedName("name") var name: String,
    @SerializedName("preview_url") var preview_url: String?
)

data class Album(
    @SerializedName("images") var images: List<ImageObject>
)

data class SimplifiedArtistObject(
    @SerializedName("name") var name: String
)

data class Link(
    @SerializedName("spotify") var spotify: String
)

data class ImageObject(
    @SerializedName("url") var url: String
)