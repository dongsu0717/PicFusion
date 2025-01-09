package com.dongsu.presentation.navigation

sealed class Destination(val route: String) {
    data object Album : Destination("album")
    data object Photos : Destination("photos")
    data object Edit : Destination("edit")
}