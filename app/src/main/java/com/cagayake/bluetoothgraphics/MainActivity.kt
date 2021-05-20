package com.cagayake.bluetoothgraphics

import android.Manifest
import android.bluetooth.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.cagayake.bluetoothgraphics.bluetooth.BluetoothDataTranslate
import com.cagayake.bluetoothgraphics.databinding.ActivityMainBinding
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.DialogPlusBuilder
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothDataTranslate: BluetoothDataTranslate
    private lateinit var dialog:DialogPlusBuilder
    private lateinit var deviceList: MutableList<BluetoothDevice>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkedPermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        dialog = DialogPlus.newDialog(this);
        binding.fab.setOnClickListener {
            bluetoothDataTranslate = BluetoothDataTranslate()
            checkAndEnableBlueTooth()
            deviceList = bluetoothDataTranslate.getBlueToothDevice()
            val adapter = ArrayAdapter<BluetoothDevice>(this,R.layout.support_simple_spinner_dropdown_item,deviceList);
            val dialogPlus = dialog.setAdapter(adapter).setOnItemClickListener{
                dialogPlus,item,view,posion->
                run {
                    Toast.makeText(this,"start",Toast.LENGTH_SHORT).show()
                    bluetoothDataTranslate.connect(item as BluetoothDevice)
                    dialogPlus.dismiss()
                }
            }.setExpanded(true).create()
            dialogPlus.show()

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun checkedPermission(){
        val permissions = arrayOf<String>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        for (str in permissions) {
            if (ContextCompat.checkSelfPermission(this,str) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 111)
                break
            }
        }
    }

    private fun checkAndEnableBlueTooth(){
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter ==null){
            Toast.makeText(this,"本机没有找到蓝牙硬件或驱动",Toast.LENGTH_SHORT).show()
        }else{
            if (!adapter.isEnabled){
                Toast.makeText(this,"请打开蓝牙",Toast.LENGTH_SHORT).show()
                startActivityForResult( Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 112)

            }
        }

    }
}