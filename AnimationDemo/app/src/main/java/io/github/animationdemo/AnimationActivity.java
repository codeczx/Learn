package io.github.animationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimationActivity extends AppCompatActivity {

	@BindView(R.id.btn_effect_1)
	Button mBtnEffect1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.btn_effect_1)
	public void onViewClicked() {
		startEffect1Activity();
	}

	private void startEffect1Activity() {
		Intent intent = new Intent(AnimationActivity.this, AnimationEnterActivity.class);
		startActivity(intent);
	}
}
