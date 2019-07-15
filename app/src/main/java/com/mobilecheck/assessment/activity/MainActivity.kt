package com.mobilecheck.assessment.activity

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mobilecheck.assessment.R
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobilecheck.assessment.adapter.ListAdapter
import com.mobilecheck.assessment.database.DatabaseHandler
import com.mobilecheck.assessment.model.SystemInfo
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Handler
import com.google.android.things.update.StatusListener
import com.scottyab.rootbeer.RootBeer
import com.google.android.things.update.UpdateManagerStatus;
import com.google.android.things.update.UpdateManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.res.ResourcesCompat
import com.mobilecheck.assessment.Constants

class MainActivity : AppCompatActivity(), StatusListener {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "assessments"
    val databaseHandler: DatabaseHandler = DatabaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        if (sharedPref.getBoolean(PREF_NAME, false)) {
            populateList()

        } else {
            setDatabase(sharedPref)
        }
    }

    fun setDatabase(sharedPref: SharedPreferences) {
        databaseHandler.addItems(
            SystemInfo(
                Constants().KEY_SUPPORT,
                "1.1 System is supported",
                "1.1 System is not supported",
                Constants().IS_NOT_UP_TO_DATE,
                Constants().IS_AUTO
            )
        )
        databaseHandler.addItems(
            SystemInfo(
                Constants().KEY_SYSTEM_UPDATE,
                "1.2 System is up-to-date",
                "1.2 Pending Updates",
                Constants().IS_NOT_UP_TO_DATE,
                Constants().IS_MANUAL
            )
        )
        databaseHandler.addItems(
            SystemInfo(
                Constants().KEY_APP_UPDATE,
                "1.3 App updates are enabled",
                "1.3 Needs Review: App updates",
                Constants().IS_NOT_UP_TO_DATE,
                Constants().IS_MANUAL
            )
        )
        databaseHandler.addItems(
            SystemInfo(
                Constants().KEY_ROOTED,
                "1.4 Device is not rooted",
                "1.4 Device is rooted",
                Constants().IS_NOT_UP_TO_DATE,
                Constants().IS_AUTO
            )
        )
        databaseHandler.addItems(
            SystemInfo(
                Constants().KEY_PASSWORD,
                "3.2 Login password is set",
                "3.2 No login password is set",
                Constants().IS_NOT_UP_TO_DATE,
                Constants().IS_AUTO
            )
        )
        databaseHandler.addItems(
            SystemInfo(
                Constants().KEY_BACKUP,
                "5.2 Google Drive backup is enabled",
                "5.2 Needs Review: Google Drive backups",
                Constants().IS_NOT_UP_TO_DATE,
                Constants().IS_MANUAL
            )
        )
        databaseHandler.addItems(
            SystemInfo(
                Constants().KEY_FIND_MY_DEVICE,
                "5.3 FindMyDeivce is enabled",
                "5.3 Needs Review: FindMyDevice",
                Constants().IS_NOT_UP_TO_DATE,
                Constants().IS_MANUAL
            )
        )
        databaseHandler.addItems(
            SystemInfo(
                Constants().KEY_VPN,
                "6.1 A VPN is installed",
                "6.1 Needs Review: VPN",
                Constants().IS_NOT_UP_TO_DATE,
                Constants().IS_AUTO
            )
        )
        val editor = sharedPref.edit()
        editor.putBoolean(PREF_NAME, true)
        editor.apply()
        populateList()
    }

    fun getItems(): List<SystemInfo> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val items: List<SystemInfo> = databaseHandler.getItems()
        return items
    }

    fun populateList() {
        if (checkAutoUpdates() == 4) {
            var itemlist:List<SystemInfo>  = getItems()
            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            recyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = ListAdapter(applicationContext,itemlist)
            }
            displayStatus(itemlist)
        }
    }

    fun displayStatus(itemList:List<SystemInfo>){
        var count:Int = 0
        for(i in 0..itemList.size-1){
            if(itemList.get(i).status==Constants().IS_NOT_UP_TO_DATE){
                count=+1
            }
        }
        if(count>0){
            ivAlert.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.need_review,
                    null
                )
            )
            tvNote.text = "$count Settings Need Review!"
        }else{
            ivAlert.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.all_good_icon,
                    null
                )
            )
            tvNote.text = "All Good!"
        }
    }
    //method to check keypad lock security
    fun checkPasscode(): Int {
        var status: Int
        val km = applicationContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (km.isKeyguardSecure) {
            status = Constants().IS_UP_TO_DATE
        } else {
            status = Constants().IS_NOT_UP_TO_DATE
        }
        databaseHandler.updateStatus(Constants().KEY_PASSWORD, status)
        return 1
    }

    //check for rooted device
    fun checkRootedDevice(): Int {
        var status: Int
        val rootBeer = RootBeer(applicationContext)
        if (rootBeer.isRooted()) {
            status = Constants().IS_NOT_UP_TO_DATE
        } else {
            status = Constants().IS_UP_TO_DATE
        }
        databaseHandler.updateStatus(Constants().KEY_ROOTED, status)
        return 1
    }

    //Check if VPN is active
    fun checkActiveVPN(): Int {
        var status: Int
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = cm.allNetworks
        var count: Int = 0
        for (i in networks.indices) {
            val caps = cm.getNetworkCapabilities(networks[i])
            if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                count = count + 1
            }
        }
        if (count > 0) {
            status = Constants().IS_UP_TO_DATE
        } else {
            status = Constants().IS_NOT_UP_TO_DATE
        }
        databaseHandler.updateStatus(Constants().KEY_VPN, status)
        return 1
    }

    fun checkSupportedVersion(): Int {
        var status: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            status = Constants().IS_UP_TO_DATE
        } else {
            status = Constants().IS_NOT_UP_TO_DATE
        }
        databaseHandler.updateStatus(Constants().KEY_SUPPORT, status)
        return 1
    }

    fun checkSystemUpdate(): Int {
        /*val mUpdateManager: UpdateManager = UpdateManager.getInstance()
        mUpdateManager.addStatusListener(this)
        val status = mUpdateManager.status
        Toast.makeText(applicationContext,"Status"+status,Toast.LENGTH_SHORT).show()*/
       // DevicePolicyManager().getPendingSystemUpdate()
        return 1
    }

    override fun onStatusUpdate(status: UpdateManagerStatus) {
        Handler().post {
            Runnable {
                kotlin.run {
                    handleStatusUpdate(status)
                }
            }
        }
    }

    fun handleStatusUpdate(status: UpdateManagerStatus) {
        if (status.currentState == UpdateManagerStatus.STATE_UPDATE_AVAILABLE) {
            Toast.makeText(applicationContext, "update available", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "up to date", Toast.LENGTH_LONG).show()
        }
    }
    fun checkAutoUpdates(): Int {
        var count: Int = 0
        //checkSystemUpdate()
        count = +checkPasscode() + checkRootedDevice() + checkActiveVPN() + checkSupportedVersion()
        return count
    }
}
