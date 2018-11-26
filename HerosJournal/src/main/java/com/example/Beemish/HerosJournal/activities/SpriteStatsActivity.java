package com.example.Beemish.HerosJournal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.Beemish.HerosJournal.R;
import com.example.Beemish.HerosJournal.helpers.SettingsHelper;
import com.example.Beemish.HerosJournal.helpers.TagDBHelper;
import com.example.Beemish.HerosJournal.helpers.UserDBHelper;
import com.example.Beemish.HerosJournal.models.UserModel;

public class SpriteStatsActivity extends AppCompatActivity {

    private TagDBHelper tagDBHelper;
    private UserDBHelper userDBHelper;

    private RecyclerView tenTags;
    private ImageView userAvatar, avatarWeapon, avatarShirt, avatarHelmet, avatarBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsHelper.applyTheme(this);
        setContentView(R.layout.activity_sprite_stats);

        setTitle(getString(R.string.sprite_page_title));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar)); // Custom tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SettingsHelper.applyThemeToolbar((Toolbar)findViewById(R.id.toolbar), this);

        tenTags=(RecyclerView)findViewById(R.id.attributeLevelsView);
        tenTags.setVisibility(View.GONE); //TODO

        userDBHelper = new UserDBHelper(this);

        avatarWeapon = findViewById(R.id.spritePageAvatarSword);
        avatarHelmet = findViewById(R.id.spritePageAvatarHelment);
        avatarShirt = findViewById(R.id.spritePageAvatarShirt);
        avatarBackground = findViewById(R.id.spritePageBackground);
        userAvatar = (ImageView) findViewById(R.id.spritePageAvatar);

        UserModel userModel = userDBHelper.fetchUser("root");

        userAvatar.setImageResource(userModel.getUserAvatar());
        avatarWeapon.setImageResource(userModel.getUserWeaponValue());
        avatarHelmet.setImageResource(userModel.getUserHelmetValue());
        avatarShirt.setImageResource(userModel.getUserShirtValue());
        avatarBackground.setImageResource(userModel.getUserBackgroundValue());
    }

    public void onClick(View view) {
        Intent i = new Intent(this, AvatarCustomizationActivity.class);
        //i.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(i);
    }
}
