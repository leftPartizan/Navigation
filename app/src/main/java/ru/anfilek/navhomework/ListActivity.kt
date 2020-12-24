package ru.anfilek.navhomework

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest.permission.CAMERA as CAMERA_PERMISSION

class ListActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 666
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        findViewById<FloatingActionButton>(R.id.fabStartCamera).setOnClickListener {
            startCameraFeature()
        }

        findViewById<Button>(R.id.buttonItem).setOnClickListener {
            startItemActivity()
        }

    }

    private fun startCameraFeature() {
        handleCheckPermissionResult(checkPermission(CAMERA_PERMISSION), CAMERA_PERMISSION)
    }

    private fun startItemActivity() {
        startActivity(Intent(this, ItemActivity::class.java))
    }

    private fun checkPermission(permission: String): PermissionType {
        return when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> PermissionType.GRANTED
            ContextCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED -> PermissionType.GRANTED
            else -> PermissionType.DENIED
        }
    }

    private fun handleCheckPermissionResult(result: PermissionType, permission: String) {
        when(result) {
            PermissionType.GRANTED -> startActivity(Intent(this, CameraActivity::class.java))
            PermissionType.DENIED -> showDialogPermissionInterpretation(permission)
        }
    }

    private fun showDialogPermissionInterpretation(permission: String) {
        AlertDialog.Builder(this)
            .setTitle("Permission Needed")
            .setMessage("Permission is needed to access camera")
            .setNegativeButton("fix") {_, _ -> showOptionPermissionAccepted(permission)}
            .setPositiveButton("OK", null)
            .show()
    }


    private fun showOptionPermissionAccepted(permission: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), CAMERA_PERMISSION_CODE)
    }

    enum class PermissionType {
        GRANTED,
        DENIED
    }

}