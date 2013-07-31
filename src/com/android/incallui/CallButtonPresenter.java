/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.incallui;

import android.media.AudioManager;

/**
 * Logic for call buttons.
 */
public class CallButtonPresenter extends Presenter<CallButtonPresenter.CallButtonUi> {

    private AudioManager mAudioManager;
    private EndCallListener mEndCallListener;

    public void init(AudioManager audioManager) {
        mAudioManager = audioManager;
    }

    @Override
    public void onUiReady(CallButtonUi ui) {
        super.onUiReady(ui);
        getUi().setMute(mAudioManager.isMicrophoneMute());
        getUi().setSpeaker(mAudioManager.isSpeakerphoneOn());
    }

    public void show() {
        getUi().setVisible(true);
    }

    public void endCallClicked() {
        // TODO(klp): hook up call id.
        CallCommandClient.getInstance().disconnectCall(1);

        mEndCallListener.onCallEnd();

        // TODO(klp): These states should come from Call objects from the CallList.
        reset();
    }

    private void reset() {
        getUi().setVisible(false);
        getUi().setMute(false);
        getUi().setSpeaker(false);
        getUi().setHold(false);
    }

    public void muteClicked(boolean checked) {
        CallCommandClient.getInstance().mute(checked);
        getUi().setMute(checked);
    }

    public void speakerClicked(boolean checked) {
        CallCommandClient.getInstance().turnSpeakerOn(checked);
        getUi().setSpeaker(checked);
    }

    public void holdClicked(boolean checked) {
        // TODO(klp): use appropriate hold callId.
        CallCommandClient.getInstance().hold(1, true);
        getUi().setHold(checked);
    }

    public void setEndCallListener(EndCallListener endCallListener) {
        mEndCallListener = endCallListener;
    }

    public interface CallButtonUi extends Ui {
        void setVisible(boolean on);
        void setMute(boolean on);
        void setSpeaker(boolean on);
        void setHold(boolean on);
    }

    public interface EndCallListener {
        void onCallEnd();
    }
}