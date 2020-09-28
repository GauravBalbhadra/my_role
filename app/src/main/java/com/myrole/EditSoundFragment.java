package com.myrole;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.myrole.databinding.FragmentEditSoundBinding;

public class EditSoundFragment extends Fragment implements View.OnClickListener {

    private FragmentEditSoundBinding fragmentEditSoundBinding;
    private NavController mNavController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentEditSoundBinding = FragmentEditSoundBinding.inflate(inflater, container, false);
        return fragmentEditSoundBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        init();
    }

    private void init() {
        fragmentEditSoundBinding.linearSpeedMotion.setOnClickListener(this);
        fragmentEditSoundBinding.linearTrim.setOnClickListener(this);
        fragmentEditSoundBinding.linearVoiceOver.setOnClickListener(this);
        fragmentEditSoundBinding.linearVolume.setOnClickListener(this);
        fragmentEditSoundBinding.tvSoundMixUp.setOnClickListener(this);
        fragmentEditSoundBinding.tvSoundFilter.setOnClickListener(this);
        fragmentEditSoundBinding.tvSoundFilter.setOnClickListener(this);
        fragmentEditSoundBinding.backIcon.setOnClickListener(this);
        fragmentEditSoundBinding.ivNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNext:
                //startActivity(new Intent(EditSoundFragment.this, PostRecordingFragment.class));
                break;
            case R.id.linear_speed_motion:
                Toast.makeText(getContext(), "speed motion click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linear_trim:
                Toast.makeText(getContext(), "trim click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linear_voice_over:
                Toast.makeText(getContext(), "voice over click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linear_volume:
                Toast.makeText(getContext(), "volume click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_sound_mix_up:
                Toast.makeText(getContext(), "mixup click", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(EditSoundFragment.this, MixupFragment.class).putExtra("show", 0)
                        //.putExtra("home_type", false));
                break;
            case R.id.tv_sound_filter:
                Toast.makeText(getContext(), "sound effect click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_sound_effect:
                Toast.makeText(getContext(), "sound filter click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.back_icon:
                mNavController.popBackStack();
                break;

        }
    }
}
