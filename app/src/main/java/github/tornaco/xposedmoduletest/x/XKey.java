package github.tornaco.xposedmoduletest.x;

import github.tornaco.xposedmoduletest.BuildConfig;

/**
 * Created by guohao4 on 2017/10/19.
 * Email: Tornaco@163.com
 */

public interface XKey {
    String LOCK_PATTERN_ENABLED = "key_lock_pattern_enabled";
    String CROP_CIRCLE_ENABLED = "key_crop_circle_enabled";
    String SHOW_APP_ICON_ENABLED = "show_app_icon_enabled";
    String BLUR = "blur_enabled";
    String BLUR_ALL = "blur_all_enabled";
    String ALLOW_3RD_VERIFIER = "3rd_ver_allowed";
    String TAKE_PHOTO_ENABLED = "key_take_photo_enabled";
    String TAKE_FULL_SCREEN_NOTER = "key_full_screen_noter";
    String PASSCODE_ENCRYPT = "key_pass_code_enc";
    String ACTIVATE_CODE = "key_act_code";
    String DEV_MODE = "dev_mode_enabled";
    String FP_ENABLED = "fp_enabled";
    String VERIFY_ON_HOME = "verify_on_home";
    String VERIFY_ON_SCREEN_OFF = "verify_on_scroff";
    String THEME = "theme";
    String FIRST_RUN = "first_ru" + BuildConfig.VERSION_NAME;

    String EXTRA_PKG_NAME = "extra.pkg";
    String EXTRA_TRANS_ID = "extra.tid";
}
