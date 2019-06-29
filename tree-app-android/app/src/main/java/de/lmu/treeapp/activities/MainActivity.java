package de.lmu.treeapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.lmu.treeapp.R;
import de.lmu.treeapp.fragments.OverviewFragment;
import de.lmu.treeapp.fragments.TreeSelectionFragment;


public class MainActivity extends AppCompatActivity {

    private final int BARCODE_READER_REQUEST_CODE = 1;
    private TextView welcomeTextView;
    private BottomNavigationView bottomNavigationView;


    private final Fragment fragment1 = new OverviewFragment();
    private final Fragment fragment2 = new TreeSelectionFragment();
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton qrCodeButton = this.findViewById(R.id.qr_code_button);
        welcomeTextView = findViewById(R.id.textView);
        qrCodeButton.setOnClickListener(getQrCodeButtonOnClickListener());


        bottomNavigationView = this.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(getOnNavigationItemSelectedListener());

        fragmentManager.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.main_container,fragment1, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_overview:
                        fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        Toast.makeText(MainActivity.this, "Overview", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_tree_selection:
                        fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        Toast.makeText(MainActivity.this, "Tree selection", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        };
    }

    private Button.OnClickListener getQrCodeButtonOnClickListener() {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast qrToast = Toast.makeText(getApplicationContext(), "QR Code Button clicked", Toast.LENGTH_LONG );
                qrToast.show();

                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != BARCODE_READER_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode != CommonStatusCodes.SUCCESS) {
            Log.e("MainActivity", String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
            return;
        }
        if (data == null) {
            welcomeTextView.setText(R.string.no_barcode_captured);
            return;
        }

        Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
        welcomeTextView.setText(barcode.displayValue);
    }
}
