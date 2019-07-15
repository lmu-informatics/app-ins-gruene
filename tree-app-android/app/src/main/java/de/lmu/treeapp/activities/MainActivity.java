package de.lmu.treeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.fragments.OverviewFragment;
import de.lmu.treeapp.fragments.TreeSelectionFragment;
import de.lmu.treeapp.service.FragmentManagerService;


public class MainActivity extends AppCompatActivity {

    private final int BARCODE_READER_REQUEST_CODE = 1;
    private TextView welcomeTextView;

    private DataManager dm;

    private FragmentManagerService fragmentManager = FragmentManagerService.getInstance(getSupportFragmentManager());
    private final Fragment treeSelectionFragment = new TreeSelectionFragment();
    private final Fragment overviewFragment = new OverviewFragment(fragmentManager, treeSelectionFragment);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null ) {
            getSupportActionBar().hide();
        }

        FloatingActionButton qrCodeButton = this.findViewById(R.id.qr_code_button);
        welcomeTextView = findViewById(R.id.textView);
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottom_navigation);


        qrCodeButton.setOnClickListener(getQrCodeButtonOnClickListener());
        bottomNavigationView.setOnNavigationItemSelectedListener(fragmentManager.getOnNavigationItemSelectedListener(overviewFragment, treeSelectionFragment));


        GetContent();

        Fragment[] bottomNavigationFragments = new Fragment[] { overviewFragment, treeSelectionFragment};
        fragmentManager.registerTransactions(bottomNavigationFragments);

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

    private void GetContent(){
        dm = DataManager.getInstance(getApplicationContext());
        while (dm.loaded == false){} //Wait for everything to be loaded --> A Future/Promise/Callback may be better in the future
    }

    // Helper-Function -> Show a Toast from any Thread
    private void ShowToast(final String toastText){
        runOnUiThread(new Runnable(){
            public void run(){
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
            }
        });
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
        Tree tree = dm.GetTreeByQR(barcode.displayValue);
        if (tree != null) {
            dm.UnlockTree(tree);
            welcomeTextView.setText(tree.name);
        }
        else
            welcomeTextView.setText("Kein Baum mit diesem QR-Code: "+ barcode.displayValue);
    }
}
