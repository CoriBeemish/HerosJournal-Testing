package com.example.Beemish.HerosJournal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.Beemish.HerosJournal.R;
import com.example.Beemish.HerosJournal.helpers.SettingsHelper;
import com.example.Beemish.HerosJournal.helpers.UserDBHelper;

public class AvatarCustomizationActivity extends AppCompatActivity {

    private ImageView avatar;
    UserDBHelper userDBHelper;

    //column is skin color, row is eye color
    int[][] drawableArray= {{R.drawable.avatar, R.drawable.avatar} , {R.drawable.avatar, R.drawable.avatar}};
    int rowIndex = 0;
    int columnIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_customization);
        SettingsHelper.applyTheme(this);

        setTitle(getString(R.string.sprite_customization_title));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar)); // Custom tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SettingsHelper.applyThemeToolbar((Toolbar)findViewById(R.id.toolbar), this);

        setAvatarImage();
    }

    private void setAvatarImage() {
        userDBHelper = new UserDBHelper(this);
        avatar = (ImageView)findViewById(R.id.avatarCustomization);
        avatar.setImageResource(userDBHelper.fetchUser("root").getUserAvatar());
    }

    public void onClick(View view) {
        userDBHelper = new UserDBHelper(this);
        switch (view.getId()) {
            case R.id.skinColorNextButton:
                if (columnIndex == drawableArray.length - 1) {
                    columnIndex = 0;
                } else ++columnIndex;
                userDBHelper.fetchUser(userDBHelper.fetchUser("root").getUserPassword()).setUserAvatar(drawableArray[columnIndex][rowIndex]);
                userDBHelper.fetchUser("root").setUserAvatar(drawableArray[columnIndex][rowIndex]);
                setAvatarImage();
                break;
            case R.id.skinColorPreviousButton:
                if (columnIndex == 0) {
                    columnIndex = drawableArray.length - 1;
                } else --columnIndex;
                userDBHelper.fetchUser(userDBHelper.fetchUser("root").getUserPassword()).setUserAvatar(drawableArray[columnIndex][rowIndex]);
                userDBHelper.fetchUser("root").setUserAvatar(drawableArray[columnIndex][rowIndex]);
                setAvatarImage();
                break;
            case R.id.eyeStyleNextButton:
                if (rowIndex == drawableArray[columnIndex].length - 1) {
                    rowIndex = 0;
                } else ++rowIndex;
                userDBHelper.fetchUser(userDBHelper.fetchUser("root").getUserPassword()).setUserAvatar(drawableArray[columnIndex][rowIndex]);
                userDBHelper.fetchUser("root").setUserAvatar(drawableArray[columnIndex][rowIndex]);
                setAvatarImage();
                break;
            case R.id.eyeStylePreviousButton:
                if (rowIndex == 0) {
                    rowIndex = drawableArray[columnIndex].length - 1;
                } else ++rowIndex;
                userDBHelper.fetchUser(userDBHelper.fetchUser("root").getUserPassword()).setUserAvatar(drawableArray[columnIndex][rowIndex]);
                userDBHelper.fetchUser("root").setUserAvatar(drawableArray[columnIndex][rowIndex]);
                setAvatarImage();
                break;
        }
    }
}
