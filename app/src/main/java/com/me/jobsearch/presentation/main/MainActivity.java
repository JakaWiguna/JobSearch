package com.me.jobsearch.presentation.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.me.jobsearch.R;
import com.me.jobsearch._core.SimpleBaseActivity;
import com.me.jobsearch.databinding.ActivityMainBinding;
import com.me.jobsearch.presentation.account.AccountFragment;
import com.me.jobsearch.presentation.home.HomeFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends SimpleBaseActivity<ActivityMainBinding> {
    private static final String TAG = "DEBUG_MAIN_ACTIVITY";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        binding.setLifecycleOwner(this);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    binding.toolbarTitle.setText("Job List");
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.account:
                    binding.toolbarTitle.setText("Account");
                    replaceFragment(new AccountFragment());
                    break;
            }
            return true;
        });
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

}