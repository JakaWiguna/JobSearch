package com.me.jobsearch.presentation.account;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.me.jobsearch.R;
import com.me.jobsearch._core.SimpleBaseFragment;
import com.me.jobsearch.databinding.FragmentAccountBinding;
import com.me.jobsearch.presentation.login.LoginActivity;


public class AccountFragment extends SimpleBaseFragment<FragmentAccountBinding> {

    private FirebaseAuth firebaseAuth;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initViews() {
        binding.setLifecycleOwner(getViewLifecycleOwner());
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            Glide.with(this).load(photoUrl).centerCrop().into(binding.civProfile);
            binding.tvName.setText(name);
        }

        binding.btnSignOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }
}