package com.mobilecheck.assessment.activity

import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import com.mobilecheck.assessment.Constants
import com.mobilecheck.assessment.R
import com.mobilecheck.assessment.database.DatabaseHandler
import com.mobilecheck.assessment.model.SystemInfo
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.toolbar
import kotlinx.android.synthetic.main.activity_main.*

class DetailActivity : AppCompatActivity() {
    private lateinit var systemInfo: SystemInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        ivNavIcon.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        var id = intent.getStringExtra(Constants().EXTRA_ID)
        val databaseHandler = DatabaseHandler(this)
        getData(databaseHandler, id)
        ivSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                databaseHandler.updateItemsManually(
                    SystemInfo(
                        id,
                        systemInfo.successTitle,
                        systemInfo.failTitle,
                        Constants().IS_UP_TO_DATE,
                        Constants().IS_MANUAL
                    )
                )
                getData(databaseHandler, id)
            } else {
                databaseHandler.updateItemsManually(
                    SystemInfo(
                        id,
                        systemInfo.successTitle,
                        systemInfo.failTitle,
                        Constants().IS_NOT_UP_TO_DATE,
                        Constants().IS_MANUAL
                    )
                )
                getData(databaseHandler, id)
            }
        }
    }

    /**
     * displayDetails for displaying details and handle UI
     * */
    fun displayDetails(id: String, status: Int) {
        if (status == Constants().IS_UP_TO_DATE) {
            tvStatus.text = systemInfo.successTitle
            ivStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.updated, null))
            if (systemInfo.isAuto == Constants().IS_AUTO) {
                llSwitch.visibility = GONE
            } else {
                ivSwitch.isChecked = true
                llSwitch.visibility = VISIBLE
            }
        } else {
            tvStatus.text = systemInfo.failTitle
            if (systemInfo.isAuto == Constants().IS_AUTO) {
                ivStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.alert, null))
                llSwitch.visibility = GONE
            } else {
                ivStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.review, null))
                ivSwitch.isChecked = false
                llSwitch.visibility = VISIBLE
            }
        }
        when (id) {
            Constants().KEY_SUPPORT -> {
                tvDescription.text = ""
            }
            Constants().KEY_SYSTEM_UPDATE -> {
                tvDescription.text =Constants().SYSTEM_UPDATE_DESCRIPTION
                ivSwitch.text = Constants().SYSTEM_UPDATE_MANUAL
                tvSettings.text = Constants().SYSTEM_UPDATE_SETTINGS
            }
            Constants().KEY_APP_UPDATE -> {
                tvDescription.text = Constants().APP_UPDATE_DESCRIPTION
                ivSwitch.text = Constants().APP_UPDATE_MANUAL
                tvSettings.text = Constants().APP_UPDATE_SETTINGS
            }
            Constants().KEY_ROOTED -> {
                tvDescription.text = ""
            }
            Constants().KEY_PASSWORD -> {
                tvDescription.text = Constants().PASSWORD_DESCRIPTION
                tvSettings.text = Constants().PASSWORD_SETTINGS
            }
            Constants().KEY_BACKUP -> {
                tvDescription.text = Constants().BACKUP_DESCRIPTION
                ivSwitch.text = Constants().BACKUP_MANUAL
                tvSettings.text = Constants().BACKUP_SETTINGS //TODO do more research
            }
            Constants().KEY_FIND_MY_DEVICE -> {
                tvDescription.text =Constants().FIND_DEVICE_DESCRIPTION
                ivSwitch.text = Constants().FIND_DEVICE_MANUAL
                tvSettings.text = Constants().FIND_DEVICE_SETTINGS
            }
            Constants().KEY_VPN -> {
                tvDescription.text = Constants().VPN_DESCRIPTION
                tvSettings.text = Constants().VPN_SETTINGS
            }
        }
    }

    fun getData(databaseHandler: DatabaseHandler, id: String) {
        val cursor: Cursor? = databaseHandler.getItems(id)
        if (cursor!!.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex("status"))
            val isAuto = cursor.getInt(cursor.getColumnIndex("isAuto"))
            val successTitle = cursor.getString(cursor.getColumnIndex("itemSuccess"))
            val failTitle = cursor.getString(cursor.getColumnIndex("itemFail"))
            systemInfo = SystemInfo(
                id = id,
                successTitle = successTitle,
                failTitle = failTitle,
                status = status,
                isAuto = isAuto
            )
        }
        displayDetails(id, systemInfo.status)
    }
}
