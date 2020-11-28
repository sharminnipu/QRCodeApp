package com.sharmin.qrcodereadapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private lateinit var codeScanner:CodeScanner

    val MY_CAMERA_PERMISSION_REQUEST=111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        codeScanner=CodeScanner(this,codeScannerView)
        codeScanner.camera=CodeScanner.CAMERA_BACK
        codeScanner.formats=CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode=AutoFocusMode.SAFE
        codeScanner.scanMode=ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled=true
        codeScanner.isFlashEnabled=false

        codeScanner.decodeCallback= DecodeCallback {
            runOnUiThread {
                Toast.makeText(this,"Scan File:${it.text}",Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback= ErrorCallback {
            runOnUiThread {
                Toast.makeText(this,"Scan File:${it.message}",Toast.LENGTH_LONG).show()

            }
        }
        checkPermission()
    }


    fun checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),MY_CAMERA_PERMISSION_REQUEST)
        }else{
            codeScanner.startPreview()
        }
    }


    @Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==MY_CAMERA_PERMISSION_REQUEST && grantResults.isNotEmpty()
            &&grantResults[0]==PackageManager.PERMISSION_GRANTED){

            codeScanner.startPreview()
        }else{
            Toast.makeText(this,"You can not scan until you  give the camera permission",Toast.LENGTH_LONG).show()
        }
    }

    @Override
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    @Override
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}