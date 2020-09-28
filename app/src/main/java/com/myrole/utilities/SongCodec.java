package com.myrole.utilities;

import android.media.MediaExtractor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;

public class SongCodec {

    private static final String TAG = "SongCodec";
    /* access modifiers changed from: private */
    public static Handler handler = new Handler(Looper.getMainLooper());

    public interface SongDecodeListener {
        void decodeFail();

        void decodeOver();
    }

    public interface DecodeOverListener {
        void decodeFail();

        void decodeIsOver();
    }

    public static void PcmToAudio(String str, String str2, final SongDecodeListener songDecodeListener) {

        new Thread(new SongEncodeRunnable(str, str2, new SongDecodeListener() {
            public void decodeFail() {
                if (songDecodeListener != null) {
                    SongCodec.handler.post(new Runnable() {
                        public void run() {
                            songDecodeListener.decodeFail();
                        }
                    });
                }
            }

            public void decodeOver() {
                if (songDecodeListener != null) {
                    SongCodec.handler.post(new Runnable() {
                        public void run() {
                            songDecodeListener.decodeOver();
                        }
                    });
                }
            }
        })).start();
    }

    public static void addADTStoPacket(byte[] bArr, int i) {
        bArr[0] = -1;
        bArr[1] = -7;
        bArr[2] = (byte) 80;
        bArr[3] = (byte) ((i >> 11) + 128);
        bArr[4] = (byte) ((i & 2047) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    @RequiresApi(api = 16)
    public static void getPCMFromSong(String audioSong, String pcmPath, final SongDecodeListener songDecodeListener) {
        boolean z = false;
        MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(audioSong);
            int i = 0;
            while (true) {
                if (i >= mediaExtractor.getTrackCount()) {
                    Log.d(TAG, "audioSong track: " + mediaExtractor.getTrackCount());
                    i = -1;
                    break;
                } else if (mediaExtractor.getTrackFormat(i).getString("mime").startsWith("audio/")) {
                    Log.d(TAG, "audioSong mime: " + mediaExtractor.getTrackFormat(i).getString("name"));
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if (z) {
                mediaExtractor.selectTrack(i);
                new Thread(new SongDecodeRunnable(mediaExtractor, i, pcmPath, new DecodeOverListener() {
                    public void decodeFail() {
                        SongCodec.handler.post(new Runnable() {
                            public void run() {
                                if (songDecodeListener != null) {
                                    songDecodeListener.decodeFail();
                                }
                            }
                        });
                    }

                    public void decodeIsOver() {
                        SongCodec.handler.post(new Runnable() {
                            public void run() {
                                if (songDecodeListener != null) {
                                    songDecodeListener.decodeOver();
                                }
                            }
                        });
                    }
                })).start();
            } else if (songDecodeListener != null) {
                songDecodeListener.decodeFail();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (songDecodeListener != null) {
                songDecodeListener.decodeFail();
            }
        }
    }
}
