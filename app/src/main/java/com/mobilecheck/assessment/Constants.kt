package com.mobilecheck.assessment

class Constants {
    val KEY_SUPPORT = "systemSupported"
    val KEY_SYSTEM_UPDATE = "systemUpdates"
    val KEY_APP_UPDATE = "appUpdated"
    val KEY_ROOTED = "rooted"
    val KEY_PASSWORD = "password"
    val KEY_BACKUP = "backup"
    val KEY_FIND_MY_DEVICE = "findMyDevice"
    val KEY_VPN = "VPN"
    val EXTRA_ID = "id"
    val EXTRA_STATUS = "status"
    val IS_UP_TO_DATE = 1
    val IS_NOT_UP_TO_DATE = 0
    val IS_AUTO = 1
    val IS_MANUAL = 0

    //description and settings
    val SUPPORT_DESCRIPTION ="It is important to only use devices that are still receiving security updates. Refer to the checklist for what the minimum supported Android version is."
    val SUPPORT_SETTINGS ="If your device can be upgraded to a supported android version, here is how you do it: Go to Settings -->About Phone -->System update"

    //SYSTEM_UPDATE
    val SYSTEM_UPDATE_DESCRIPTION ="Supported devices get regular updates, which include fixes for security issues. These updates should be installed within a month of being released."
    val SYSTEM_UPDATE_SETTINGS ="Go to Settings --> About Phone --> System update"
    val SYSTEM_UPDATE_MANUAL ="I have enabled auto system update"

    //APP_UPDATE
    val APP_UPDATE_DESCRIPTION = "If you have a good Internet connection, set your device to automatically update your apps, otherwise, manually update them on a regular basis."
    val APP_UPDATE_SETTINGS = "Go to Google Play Store app --> Menu --> Settings --> tap on Auto-update apps"
    val APP_UPDATE_MANUAL = "I have enabled auto app update"

    //ROOTED
    val ROOTED_DESCRIPTION = "Rooting your device strips it of key security features."
    val ROOTED_SETTINGS = "You will need to unroot device manually"

    //PASSWORD
    val PASSWORD_DESCRIPTION = "Your device should have a minimum 6 character login password."
    val PASSWORD_SETTINGS = "Go to Settings --> Security & location"

    //BACKUP
    val BACKUP_DESCRIPTION = "Backing up the data on your device is important in case it is lost or stolen."
    val BACKUP_SETTINGS = "Go to Settings --> Google --> Services --> tap on Backup"
    val BACKUP_MANUAL = "I have enabled auto backup"

    //FIND MY DEVICE
    val FIND_DEVICE_DESCRIPTION = "To be prepared in case you lose your Android phone, you can check that Find My Device can find it."
    val FIND_DEVICE_SETTINGS = "Go to Settings --> Google --> Security --> tap on Find My Device"
    val FIND_DEVICE_MANUAL = "I have enabled find my device"

    //VPN
    val VPN_DESCRIPTION =  "It is highly recommended to use a VPN when utilizing untrusted WiFi."
    val VPN_SETTINGS = "Install a VPN on your device, Go to Settings --> Wireless & networks --> More --> VPN"
}