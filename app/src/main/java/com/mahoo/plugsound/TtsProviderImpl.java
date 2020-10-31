package com.mahoo.plugsound;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TtsProviderImpl extends TtsProviderFactory implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;

    public void init(Context context) {
        if (tts == null) {
            tts = new TextToSpeech(context, this);
        }
    }

    @Override
    public void say(String sayThis) {
        tts.speak(sayThis, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        Locale loc = new Locale("tr", "", "");
        if (tts.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
            tts.setLanguage(loc);
        }
    }

    public void shutdown() {
        tts.shutdown();
    }
}
