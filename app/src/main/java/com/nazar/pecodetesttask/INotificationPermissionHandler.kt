package com.nazar.pecodetesttask

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

interface INotificationPermissionHandler {

    val isPermissionGranted: Boolean

    fun requestPermission()

    class Default(
        private val activity: AppCompatActivity,
        private val binding: ViewBinding
    ) : INotificationPermissionHandler {

        private val context: Context
            get() = activity

        override val isPermissionGranted: Boolean
            get() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true

                return ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            }


        override fun requestPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        private val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showPermissionGrantedToast()
            } else {
                showDontHavePermissionOpenSettingsSnackbar()
            }
        }

        private fun showPermissionGrantedToast() {
            Toast.makeText(
                context,
                getString(R.string.notification_permission_granted),
                Toast.LENGTH_SHORT
            ).show()
        }

        private fun showDontHavePermissionOpenSettingsSnackbar() {
            Snackbar.make(
                binding.root,
                getString(R.string.we_dont_have_permission),
                Snackbar.LENGTH_SHORT
            ).apply {
                setAction(getString(R.string.open_settings)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        context.startActivity(settingsIntent)
                    }
                }

            }.show()
        }

        private fun getString(@StringRes id: Int) = context.getString(id)
    }
}