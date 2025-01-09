package com.dongsu.presentation.ui.albumlist.intent

sealed class AlbumEvent {
    data object LoadAlbums : AlbumEvent()
    data object SaveAllPhotoCache : AlbumEvent()
}