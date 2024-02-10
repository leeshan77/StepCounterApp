package com.kolee.composepedometer2.presentation.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kolee.composepedometer2.ui.theme.Dimens
import com.kolee.composepedometer2.R
import com.kolee.composepedometer2.ui.theme.TextDefaultColor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckAndRequestPermissions(
    permissions: List<String>,
    appContent: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
    var isPermissionDenied by remember { mutableStateOf(false) }

    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.Six)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.walking),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(200.dp)
                        .background(color = Color.Yellow)
                        .padding(10.dp)
                )
                Spacer(modifier = Modifier.height(Dimens.Six))
                Text(
                    text = stringResource(id = R.string.permission_prompt),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.TextDefaultColor,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(Dimens.Six))
                TextButton(
                    onClick = { permissionState.launchMultiplePermissionRequest() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(Dimens.Three)
                ) {
                    Text(text = stringResource(R.string.enable_permissions))
                }
            }
        },
        permissionsNotAvailableContent = {
            isPermissionDenied = true
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.Six)
            ) {
                Text(
                    stringResource(R.string.permissions_rationale), textAlign = TextAlign.Center,
                    color = Color.Red,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(Dimens.Sixteen))
                TextButton(
                    onClick = { activity.openAppSettings() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(Dimens.Three)
                ) {
                    Text(text = stringResource(R.string.goto_settings))
                }
            }
        }
    ) {
        appContent.invoke()
    }
}

private fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    ).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(this)
    }
}