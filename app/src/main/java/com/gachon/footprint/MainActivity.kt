package com.gachon.footprint

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.firebase.ui.auth.data.model.User
import com.gachon.footprint.data.CurrentUser
import com.gachon.footprint.navigation.CashFragment
import com.gachon.footprint.navigation.HomeFragment
import com.gachon.footprint.navigation.SettingFragment
import com.gachon.footprint.settingfragment.AccountInfo
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var user = CurrentUser()

    companion object {
        var sydney: LatLng? = null
    }

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getUserInfo()

        if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions,0)
        }
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 0)
        fun getLastLocationNewMethod() {
            val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            mFusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.apply {
                        sydney = LatLng(this.latitude, this.longitude)
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }
    }

    private fun getUserInfo(){
        var cuser = FirebaseAuth.getInstance().currentUser
        if(cuser!=null) {
            user.uid = cuser.uid
            db.collection("User").document(user.uid!!).get().addOnSuccessListener { documentSnapshot ->
                var map: Map<String, Any> = documentSnapshot.data as Map<String, Any>
                user.nickname = map["nickname"].toString()
                user.email = map["userEmail"].toString()
                user.password = map["password"].toString()
                user.Img = map["userImg"].toString()
            }
        }
    }
}