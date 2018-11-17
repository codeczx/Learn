package io.github.animationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

	@BindView(R.id.btn_anim_activity)
	Button mBtnAnimActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.btn_anim_activity)
	public void onViewClicked() {
		startAnimationActivity();
	}

	private void startAnimationActivity() {
		Intent intent = new Intent(MainActivity.this, AnimationActivity.class);
		startActivity(intent);
	}
}
