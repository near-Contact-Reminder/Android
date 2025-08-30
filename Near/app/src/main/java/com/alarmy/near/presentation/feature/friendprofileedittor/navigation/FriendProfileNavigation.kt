package com.alarmy.near.presentation.feature.friendprofileedittor.navigation

import android.os.Build
import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.savedstate.SavedState
import com.alarmy.near.model.Friend
import com.alarmy.near.presentation.feature.friendprofileedittor.FriendProfileEditorRoute
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.RouteFriendProfileEditor.Companion.routeTypeMap
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
@Parcelize
data class RouteFriendProfileEditor(
    val friend: Friend,
) : Parcelable {
    companion object {
        val routeTypeMap =
            mapOf<KType, NavType<*>>(
                typeOf<Friend>() to FriendType,
            )
    }
}

fun NavController.navigateToFriendProfileEditor(
    friend: Friend,
    navOptions: NavOptions? = null,
) {
    navigate(
        RouteFriendProfileEditor(
            friend = friend,
        ),
        navOptions,
    )
}

fun NavGraphBuilder.friendProfileEditorNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickBackButton: () -> Unit = {},
) {
    composable<RouteFriendProfileEditor>(
        typeMap =
        routeTypeMap,
    ) { backStackEntry ->
        FriendProfileEditorRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}

internal val FriendType =
    object : NavType<Friend>(
        isNullableAllowed = false,
    ) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: Friend,
        ) {
            bundle.putParcelable(key, value)
        }

        override fun get(
            bundle: SavedState,
            key: String,
        ): Friend? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, Friend::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(key)
            }

        override fun parseValue(value: String): Friend = Json.decodeFromString<Friend>(value)

        override fun serializeAsValue(value: Friend): String = Json.encodeToString(value)
    }
