package com.cagayake.bluetoothgraphics.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothSocket
import com.cagayake.bluetoothgraphics.rxbus.RxBus
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*


class BluetoothDataTranslate  {

    private var mSocket: BluetoothSocket? = null
    private var mOut: DataOutputStream? = null
    private var isRead = false
    private val FLAG_MSG = 0
    private val PP_UUID:UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")


    private fun loopRead(socket: BluetoothSocket?) {
        mSocket = socket
        try {
            if (!mSocket!!.isConnected) {
                mSocket?.connect()}

            mOut = DataOutputStream(mSocket?.outputStream)
            val inputStream = DataInputStream(mSocket?.inputStream)
            isRead = true
            while (isRead) {
                when (inputStream.readInt()) {
                    FLAG_MSG -> {
                        val msg: String = inputStream.readUTF()
                        RxBus.post(msg)
                    }
                }
            }
        } catch (e: Throwable) {
           e.printStackTrace()
            isRead = false
            mSocket?.close()
        }
    }

    fun connect(dev: BluetoothDevice) {
        try {
            val socket = dev.createInsecureRfcommSocketToServiceRecord(PP_UUID)
            // 开启子线程
            Thread {
                loopRead(socket) // 循环读取
            }.start()
        } catch (e: Throwable) {
            e.printStackTrace()
            isRead = false;
            mSocket?.close();
        }
    }

    fun getBlueToothDevice():MutableList<BluetoothDevice>{
        val adapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices: Set<BluetoothDevice> = adapter.bondedDevices
        val devices: MutableList<BluetoothDevice> = ArrayList()
        devices.addAll(pairedDevices)
        return devices
    }
}