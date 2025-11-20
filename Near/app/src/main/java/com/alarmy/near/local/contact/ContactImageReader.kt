package com.alarmy.near.local.contact

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import javax.inject.Inject

data class ContactImageData(
    val fileName: String,
    val contentType: String,
    val fileSize: Int,
    val data: ByteArray,
)

class ContactImageReader
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
    ) {
        fun read(uriString: String): ContactImageData? {
            val uri = runCatching { Uri.parse(uriString) }.getOrNull() ?: return null
            val bytes = contentResolver.openInputStream(uri)?.use { inputStream -> inputStream.readBytes() } ?: return null
            val resolvedMimeType = contentResolver.getType(uri) ?: guessMimeType(uriString) ?: DEFAULT_MIME_TYPE
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(resolvedMimeType) ?: DEFAULT_EXTENSION
            val fileName = "contact_${System.currentTimeMillis()}.$extension"
            return ContactImageData(
                fileName = fileName,
                contentType = resolvedMimeType,
                fileSize = bytes.size,
                data = bytes,
            )
        }

        private fun guessMimeType(uriString: String): String? {
            val lowerCase = uriString.lowercase()
            return when {
                lowerCase.endsWith(".png") -> "image/png"
                lowerCase.endsWith(".webp") -> "image/webp"
                lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") -> "image/jpeg"
                else -> null
            }
        }

        companion object {
            private const val DEFAULT_MIME_TYPE = "image/jpeg"
            private const val DEFAULT_EXTENSION = "jpg"
        }
    }

