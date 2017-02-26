package com.superapp.guessthemusicnhactrenew.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.banner.RevMobBanner;
import com.revmob.ads.interstitial.RevMobFullscreen;
import com.superapp.guessthemusicnhactrenew.R;
import com.superapp.guessthemusicnhactrenew.model.STATIC_DATA;
import com.superapp.guessthemusicnhactrenew.util.Util;

/**
 * Created by ManhNV on 2/19/17.
 *
 * @author ManhNV
 */

public class AdFragment extends Fragment {
    private ViewGroup view;
    private RevMob revmob;
    private RevMobBanner banner;
    private RevMobFullscreen fullscreen;
    private boolean fullscreenIsLoaded;
    private boolean hasFullScreen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasFullScreen = getArguments().getBoolean(STATIC_DATA.HAS_FULL_SCREEN,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ad, container, false);
        view = (ViewGroup) v.findViewById(R.id.bannerLayout);
        startRevMobSession();
        return v;
    }

    public void startRevMobSession() {
        //RevMob's Start Session method:
        revmob = RevMob.startWithListener(getActivity(), new RevMobAdsListener() {
            @Override
            public void onRevMobSessionStarted() {
                loadBanner(); // Cache the banner once the session is started
                if (hasFullScreen)
                    loadFullscreen();
                Log.i("RevMob", "Session Started");
            }

            @Override
            public void onRevMobSessionNotStarted(String message) {
                //If the session Fails to start, no ads can be displayed.
                Log.i("RevMob", "Session Failed to Start");
            }
        }, getActivity().getString(R.string.revmob_id));
        if (revmob != null) {
            loadBanner();
            if (hasFullScreen)
                loadFullscreen();
        }
        Log.i("Revmob", "ahihi");
    }

    public void loadBanner() {
        banner = revmob.preLoadBanner(getActivity(), new RevMobAdsListener() {
            @Override
            public void onRevMobAdReceived() {
                showBanner();
                Log.i("RevMob", "Banner Ready to be Displayed"); //At this point, the banner is ready to be displayed.
            }

            @Override
            public void onRevMobAdNotReceived(String message) {
                Log.i("RevMob", "Banner Not Failed to Load");
            }

            @Override
            public void onRevMobAdDisplayed() {
                Log.i("RevMob", "Banner Displayed");
            }
        });
    }

    public void showBanner() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (banner.getParent() == null) {
                    view.addView(banner);
                }
                banner.show(); //This method must be called in order to display the ad.

            }
        });
    }

    public void loadFullscreen() {
        //load it with RevMob listeners to control the events fired
        fullscreen = revmob.createFullscreen(getActivity(), new RevMobAdsListener() {
            @Override
            public void onRevMobAdReceived() {
                Log.i("RevMob", "Fullscreen loaded.");
                fullscreenIsLoaded = true;
                int rand = Util.random(1, 3);
                Log.d("Random", rand + "");
                if (rand == 1) {
                    showFullscreen();
                }
            }

            @Override
            public void onRevMobAdNotReceived(String message) {
                Log.i("RevMob", "Fullscreen not received.");
            }

            @Override
            public void onRevMobAdDismissed() {
                Log.i("RevMob", "Fullscreen dismissed.");
            }

            @Override
            public void onRevMobAdClicked() {
                Log.i("RevMob", "Fullscreen clicked.");
            }

            @Override
            public void onRevMobAdDisplayed() {
                Log.i("RevMob", "Fullscreen displayed.");
            }
        });
    }

    public void showFullscreen() {
        if (fullscreenIsLoaded) {
            fullscreen.show(); // call it wherever you want to show the fullscreen ad
            fullscreenIsLoaded = false;
        } else {
            Log.i("RevMob", "Ad not loaded yet.");
        }
    }
}
