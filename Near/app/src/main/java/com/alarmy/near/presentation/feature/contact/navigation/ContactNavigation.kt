package com.alarmy.near.presentation.feature.contact.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.model.contact.Contact
import com.alarmy.near.presentation.feature.contact.ContactRoute
import kotlinx.serialization.Serializable

const val CONTACT_SELECTION_COMPLETE_KEY = "CONTACT_SELECTION_COMPLETE_KEY"

@Serializable
object RouteContact

/*
* 추후 홈으로 화면 이동이 필요할 때 이 함수를 사용합니다.
* */
fun NavController.navigateToContact(navOptions: NavOptions) {
    navigate(RouteContact, navOptions)
}

fun NavGraphBuilder.contactNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBackClick: () -> Unit,
    onCompletedSelection: (List<Contact>) -> Unit,
) {
    composable<RouteContact> { backStackEntry ->
        ContactRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            onCompletedSelection = onCompletedSelection,
        )
    }
}
