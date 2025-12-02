package com.alarmy.near.local.contact

import android.content.ContentResolver
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import javax.inject.Inject

data class ContactImageData(
    val fileName: String,
    val contentType: String,
    val fileSize: Int,
    val data: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ContactImageData
        if (fileName != other.fileName) return false
        if (contentType != other.contentType) return false
        if (fileSize != other.fileSize) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + fileSize
        result = 31 * result + data.contentHashCode()
        return result
    }
}

class ContactImageReader
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
    ) {
        fun read(uriString: String): ContactImageData? {
            val uri = runCatching { uriString.toUri() }.getOrNull() ?: return null
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
