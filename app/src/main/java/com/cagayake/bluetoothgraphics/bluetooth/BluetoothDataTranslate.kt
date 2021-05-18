package com.cagayake.bluetoothgraphics.bluetooth

import android.bluetooth.BluetoothAdapter


class BluetoothDataTranslate() {
    init {

        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter.isEnabled){
            adapter.enable()
        }


    }
}