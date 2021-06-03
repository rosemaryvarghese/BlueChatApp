package com.example.bluechatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class DevLActivity extends AppCompatActivity {

    private ListView pairList, availList;
    private ArrayAdapter<String> adapterPairdev, adapterAvdev;
    private Context context;
    private ProgressBar progScanDev;

    private BluetoothAdapter bladapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_lactivity);
        context = this;
        init();
    }

    private void init() {
        pairList = findViewById(R.id.list_pairdev);
        availList = findViewById(R.id.list_availdev);
        progScanDev = findViewById(R.id.progress_scan_dev);

        adapterPairdev = new ArrayAdapter<String>(context, R.layout.dev_list_item);
        adapterAvdev = new ArrayAdapter<String>(context, R.layout.dev_list_item);

        pairList.setAdapter(adapterPairdev);
        availList.setAdapter(adapterAvdev);

        availList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                Intent intent = new Intent();
                intent.putExtra("deviceAddress", address);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        bladapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDev = bladapter.getBondedDevices();

        if (pairedDev != null && pairedDev.size() > 0) {
            for (BluetoothDevice dev : pairedDev) {
                adapterPairdev.add(dev.getName() + "\n" + dev.getAddress());
            }
        }

        IntentFilter intfilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDeviceListener, intfilter);
        IntentFilter ifilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothDeviceListener, ifilter);

    }

    private void scanDevices() {
        progScanDev.setVisibility(View.VISIBLE);
        adapterAvdev.clear();
        Toast.makeText(context, "Scan started", Toast.LENGTH_SHORT).show();

        if (bladapter.isDiscovering()) {
            bladapter.cancelDiscovery();
        }
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        bladapter.startDiscovery();
    }

    private BroadcastReceiver bluetoothDeviceListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    adapterAvdev.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progScanDev.setVisibility(View.GONE);
                if (adapterAvdev.getCount() == 0) {
                    Toast.makeText(context, "No new devices found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Click on the device to start the chat", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dev_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan_dev:
                scanDevices();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}