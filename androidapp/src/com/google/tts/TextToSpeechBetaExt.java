package com.google.tts;

import com.google.tts.ITtsBeta;
import com.google.tts.ITtsCallbackBeta;

// import android.annotation.SdkConstant;
// import android.annotation.SdkConstant.SdkConstantType;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * 
 * Synthesizes speech from text for immediate playback or to create a sound
 * file.
 * <p>
 * A TextToSpeech instance can only be used to synthesize text once it has
 * completed its initialization. Implement the
 * {@link TextToSpeechBetaExt.OnInitListener} to be notified of the completion of
 * the initialization.<br>
 * When you are done using the TextToSpeech instance, call the
 * {@link #shutdown()} method to release the native resources used by the
 * TextToSpeech engine.
 * 
 */
public class TextToSpeechBetaExt extends TextToSpeech {
  public static final String USING_PLATFORM_TTS =
      "TextToSpeechBeta not installed - defaulting to basic platform TextToSpeech for ";
  public static final String NOT_ON_PLATFORM_TTS =
      "TextToSpeechBeta not installed - basic platform TextToSpeech does not support ";

  /**
   * Denotes a successful operation.
   */
  public static final int SUCCESS = 0;
  /**
   * Denotes a generic operation failure.
   */
  public static final int ERROR = -1;

  /**
   * Queue mode where all entries in the playback queue (media to be played and
   * text to be synthesized) are dropped and replaced by the new entry.
   */
  public static final int QUEUE_FLUSH = 0;
  /**
   * Queue mode where the new entry is added at the end of the playback queue.
   */
  public static final int QUEUE_ADD = 1;


  /**
   * Denotes the language is available exactly as specified by the locale.
   */
  public static final int LANG_COUNTRY_VAR_AVAILABLE = 2;


  /**
   * Denotes the language is available for the language and country specified by
   * the locale, but not the variant.
   */
  public static final int LANG_COUNTRY_AVAILABLE = 1;


  /**
   * Denotes the language is available for the language by the locale, but not
   * the country and variant.
   */
  public static final int LANG_AVAILABLE = 0;

  /**
   * Denotes the language data is missing.
   */
  public static final int LANG_MISSING_DATA = -1;

  /**
   * Denotes the language is not supported.
   */
  public static final int LANG_NOT_SUPPORTED = -2;


  /**
   * Broadcast Action: The TextToSpeech synthesizer has completed processing of
   * all the text in the speech queue.
   */
  // @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
  public static final String ACTION_TTS_QUEUE_PROCESSING_COMPLETED =
      "android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED";


  /**
   * Interface definition of a callback to be invoked indicating the completion
   * of the TextToSpeech engine initialization.
   */
  public interface OnInitListener {
    /**
     * Called to signal the completion of the TextToSpeech engine
     * initialization.
     * 
     * @param status {@link TextToSpeechBeta#SUCCESS} or
     *        {@link TextToSpeechBeta#ERROR}.
     * 
     * @param version The version of TextToSpeechBeta Service that the user has
     *        installed, or -1 if the user does not have it installed.
     */
    public void onInit(int status, int version);
  }

  /**
   * Interface definition of a callback to be invoked indicating the
   * TextToSpeech engine has completed synthesizing an utterance with an
   * utterance ID set.
   * 
   */
  public interface OnUtteranceCompletedListener {
    /**
     * Called to signal the completion of the synthesis of the utterance that
     * was identified with the string parameter. This identifier is the one
     * originally passed in the parameter hashmap of the synthesis request in
     * {@link TextToSpeechBeta#speak(String, int, HashMap)} or
     * {@link TextToSpeechBeta#synthesizeToFile(String, HashMap, String)} with
     * the {@link TextToSpeechBeta.Engine#KEY_PARAM_UTTERANCE_ID} key.
     * 
     * @param utteranceId the identifier of the utterance.
     */
    public void onUtteranceCompleted(String utteranceId);
  }


  /**
   * Internal constants for the TextToSpeech functionality
   * 
   */
  public class Engine {
    // default values for a TTS engine when settings are not found in the
    // provider
    /**
     * {@hide}
     */
    public static final int DEFAULT_RATE = 100; // 1x
    /**
     * {@hide}
     */
    public static final int DEFAULT_PITCH = 100;// 1x
    /**
     * {@hide}
     */
    public static final int USE_DEFAULTS = 0; // false
    /**
     * {@hide}
     */
    public static final String DEFAULT_SYNTH = "com.svox.pico";

    // default values for rendering
    /**
     * Default audio stream used when playing synthesized speech.
     */
    public static final int DEFAULT_STREAM = AudioManager.STREAM_MUSIC;

    // return codes for a TTS engine's check data activity
    /**
     * Indicates success when checking the installation status of the resources
     * used by the TextToSpeech engine with the {@link #ACTION_CHECK_TTS_DATA}
     * intent.
     */
    public static final int CHECK_VOICE_DATA_PASS = 1;
    /**
     * Indicates failure when checking the installation status of the resources
     * used by the TextToSpeech engine with the {@link #ACTION_CHECK_TTS_DATA}
     * intent.
     */
    public static final int CHECK_VOICE_DATA_FAIL = 0;
    /**
     * Indicates erroneous data when checking the installation status of the
     * resources used by the TextToSpeech engine with the
     * {@link #ACTION_CHECK_TTS_DATA} intent.
     */
    public static final int CHECK_VOICE_DATA_BAD_DATA = -1;
    /**
     * Indicates missing resources when checking the installation status of the
     * resources used by the TextToSpeech engine with the
     * {@link #ACTION_CHECK_TTS_DATA} intent.
     */
    public static final int CHECK_VOICE_DATA_MISSING_DATA = -2;
    /**
     * Indicates missing storage volume when checking the installation status of
     * the resources used by the TextToSpeech engine with the
     * {@link #ACTION_CHECK_TTS_DATA} intent.
     */
    public static final int CHECK_VOICE_DATA_MISSING_VOLUME = -3;

    // intents to ask engine to install data or check its data
    /**
     * Activity Action: Triggers the platform TextToSpeech engine to start the
     * activity that installs the resource files on the device that are required
     * for TTS to be operational. Since the installation of the data can be
     * interrupted or declined by the user, the application shouldn't expect
     * successful installation upon return from that intent, and if need be,
     * should check installation status with {@link #ACTION_CHECK_TTS_DATA}.
     */
    // @SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final String ACTION_INSTALL_TTS_DATA =
        "android.speech.tts.engine.INSTALL_TTS_DATA";

    /**
     * Broadcast Action: broadcast to signal the completion of the installation
     * of the data files used by the synthesis engine. Success or failure is
     * indicated in the {@link #EXTRA_TTS_DATA_INSTALLED} extra.
     */
    // @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_TTS_DATA_INSTALLED =
        "android.speech.tts.engine.TTS_DATA_INSTALLED";
    /**
     * Activity Action: Starts the activity from the platform TextToSpeech
     * engine to verify the proper installation and availability of the resource
     * files on the system. Upon completion, the activity will return one of the
     * following codes: {@link #CHECK_VOICE_DATA_PASS},
     * {@link #CHECK_VOICE_DATA_FAIL}, {@link #CHECK_VOICE_DATA_BAD_DATA},
     * {@link #CHECK_VOICE_DATA_MISSING_DATA}, or
     * {@link #CHECK_VOICE_DATA_MISSING_VOLUME}.
     * <p>
     * Moreover, the data received in the activity result will contain the
     * following fields:
     * <ul>
     * <li>{@link #EXTRA_VOICE_DATA_ROOT_DIRECTORY} which indicates the path to
     * the location of the resource files,</li>
     * <li>{@link #EXTRA_VOICE_DATA_FILES} which contains the list of all the
     * resource files,</li>
     * <li>and {@link #EXTRA_VOICE_DATA_FILES_INFO} which contains, for each
     * resource file, the description of the language covered by the file in the
     * xxx-YYY format, where xxx is the 3-letter ISO language code, and YYY is
     * the 3-letter ISO country code.</li>
     * </ul>
     */
    // @SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final String ACTION_CHECK_TTS_DATA = "android.speech.tts.engine.CHECK_TTS_DATA";

    // extras for a TTS engine's check data activity
    /**
     * Extra information received with the {@link #ACTION_CHECK_TTS_DATA} intent
     * where the TextToSpeech engine specifies the path to its resources.
     */
    public static final String EXTRA_VOICE_DATA_ROOT_DIRECTORY = "dataRoot";
    /**
     * Extra information received with the {@link #ACTION_CHECK_TTS_DATA} intent
     * where the TextToSpeech engine specifies the file names of its resources
     * under the resource path.
     */
    public static final String EXTRA_VOICE_DATA_FILES = "dataFiles";
    /**
     * Extra information received with the {@link #ACTION_CHECK_TTS_DATA} intent
     * where the TextToSpeech engine specifies the locale associated with each
     * resource file.
     */
    public static final String EXTRA_VOICE_DATA_FILES_INFO = "dataFilesInfo";

    // extras for a TTS engine's data installation
    /**
     * Extra information received with the {@link #ACTION_TTS_DATA_INSTALLED}
     * intent. It indicates whether the data files for the synthesis engine were
     * successfully installed. The installation was initiated with the
     * {@link #ACTION_INSTALL_TTS_DATA} intent. The possible values for this
     * extra are {@link TextToSpeechBeta#SUCCESS} and
     * {@link TextToSpeechBeta#ERROR}.
     */
    public static final String EXTRA_TTS_DATA_INSTALLED = "dataInstalled";

    // keys for the parameters passed with speak commands. Hidden keys are used
    // internally
    // to maintain engine state for each TextToSpeech instance.
    /**
     * {@hide}
     */
    public static final String KEY_PARAM_RATE = "rate";
    /**
     * {@hide}
     */
    public static final String KEY_PARAM_LANGUAGE = "language";
    /**
     * {@hide}
     */
    public static final String KEY_PARAM_COUNTRY = "country";
    /**
     * {@hide}
     */
    public static final String KEY_PARAM_VARIANT = "variant";
    /**
     * Parameter key to specify the audio stream type to be used when speaking
     * text or playing back a file.
     * 
     * @see TextToSpeechBeta#speak(String, int, HashMap)
     * @see TextToSpeechBeta#playEarcon(String, int, HashMap)
     */
    public static final String KEY_PARAM_STREAM = "streamType";
    /**
     * Parameter key to identify an utterance in the
     * {@link TextToSpeechBeta.OnUtteranceCompletedListener} after text has been
     * spoken, a file has been played back or a silence duration has elapsed.
     * 
     * @see TextToSpeechBeta#speak(String, int, HashMap)
     * @see TextToSpeechBeta#playEarcon(String, int, HashMap)
     * @see TextToSpeechBeta#synthesizeToFile(String, HashMap, String)
     */
    public static final String KEY_PARAM_UTTERANCE_ID = "utteranceId";
    /**
     * Parameter key to specify the synthesis engine 
     * 
     * @see TextToSpeechBeta#setEngineByPackageName(String)
     */
    public static final String KEY_PARAM_ENGINE = "engine";
    
    public static final String KEY_PARAM_PITCH = "pitch";

    // key positions in the array of cached parameters
    /**
     * {@hide}
     */
    protected static final int PARAM_POSITION_RATE = 0;
    /**
     * {@hide}
     */
    protected static final int PARAM_POSITION_LANGUAGE = 2;
    /**
     * {@hide}
     */
    protected static final int PARAM_POSITION_COUNTRY = 4;
    /**
     * {@hide}
     */
    protected static final int PARAM_POSITION_VARIANT = 6;
    /**
     * {@hide}
     */
    protected static final int PARAM_POSITION_STREAM = 8;
    /**
     * {@hide}
     */
    protected static final int PARAM_POSITION_UTTERANCE_ID = 10;
    /**
     * {@hide}
     */
    protected static final int PARAM_POSITION_ENGINE = 12;
    /**
     * {@hide}
     */
    protected static final int NB_CACHED_PARAMS = 7;
  }

  /**
   * Connection needed for the TTS.
   */
  private ServiceConnection mServiceConnection;

  private ITtsBeta mITts = null;
  private ITtsCallbackBeta mITtscallback = null;
  private Context mContext = null;
  private String mPackageName = "";
  private static OnInitListener mInitListener = null;
  private boolean mStarted = false;
  private final Object mStartLock = new Object();
  /**
   * Used to store the cached parameters sent along with each synthesis request
   * to the TTS service.
   */
  private String[] mCachedParams;


  static boolean ttsBetaInstalled = false;
  static TextToSpeech.OnInitListener platformOnInitListener = new TextToSpeech.OnInitListener() {
    @Override
    public void onInit(int status) {
      if (!ttsBetaInstalled) {
        if (mInitListener != null) {
          mInitListener.onInit(status, -1);
        }
      }
    }
  };

  /**
   * The constructor for the TextToSpeech class. This will also initialize the
   * associated TextToSpeech engine if it isn't already running.
   * 
   * @param context The context this instance is running in.
   * @param listener The {@link TextToSpeechBeta.OnInitListener} that will be
   *        called when the TextToSpeech engine has initialized.
   */
  public TextToSpeechBetaExt(Context context, OnInitListener listener) {
    super(context, platformOnInitListener);
//PC522    ttsBetaInstalled = isInstalled(context);
    mInitListener = listener;
    if (ttsBetaInstalled) {
      super.shutdown();
      mContext = context;
      mPackageName = mContext.getPackageName();

      mCachedParams = new String[2 * Engine.NB_CACHED_PARAMS]; // store key and
      // value
      mCachedParams[Engine.PARAM_POSITION_RATE] = Engine.KEY_PARAM_RATE;
      mCachedParams[Engine.PARAM_POSITION_LANGUAGE] = Engine.KEY_PARAM_LANGUAGE;
      mCachedParams[Engine.PARAM_POSITION_COUNTRY] = Engine.KEY_PARAM_COUNTRY;
      mCachedParams[Engine.PARAM_POSITION_VARIANT] = Engine.KEY_PARAM_VARIANT;
      mCachedParams[Engine.PARAM_POSITION_STREAM] = Engine.KEY_PARAM_STREAM;
      mCachedParams[Engine.PARAM_POSITION_UTTERANCE_ID] = Engine.KEY_PARAM_UTTERANCE_ID;
      mCachedParams[Engine.PARAM_POSITION_ENGINE] = Engine.KEY_PARAM_ENGINE;

      mCachedParams[Engine.PARAM_POSITION_RATE + 1] = String.valueOf(Engine.DEFAULT_RATE);
      // initialize the language cached parameters with the current Locale
      Locale defaultLoc = Locale.getDefault();
      mCachedParams[Engine.PARAM_POSITION_LANGUAGE + 1] = defaultLoc.getISO3Language();
      mCachedParams[Engine.PARAM_POSITION_COUNTRY + 1] = defaultLoc.getISO3Country();
      mCachedParams[Engine.PARAM_POSITION_VARIANT + 1] = defaultLoc.getVariant();

      mCachedParams[Engine.PARAM_POSITION_STREAM + 1] = String.valueOf(Engine.DEFAULT_STREAM);
      mCachedParams[Engine.PARAM_POSITION_UTTERANCE_ID + 1] = "";
      
      mCachedParams[Engine.PARAM_POSITION_ENGINE + 1] = Engine.DEFAULT_SYNTH;

      initTts();
    }
  }


  private void initTts() {
    mStarted = false;

    // Initialize the TTS, run the callback after the binding is successful
    mServiceConnection = new ServiceConnection() {
      public void onServiceConnected(ComponentName name, IBinder service) {
        synchronized (mStartLock) {
          mITts = ITtsBeta.Stub.asInterface(service);
          mStarted = true;
          if (mInitListener != null) {
            try {
              PackageManager pm = mContext.getPackageManager();
              PackageInfo info = pm.getPackageInfo("com.google.tts", 0);
              mInitListener.onInit(SUCCESS, info.versionCode);
            } catch (NameNotFoundException e) {
              e.printStackTrace();
            }
          }
        }
      }

      public void onServiceDisconnected(ComponentName name) {
        synchronized (mStartLock) {
          mITts = null;
          mInitListener = null;
          mStarted = false;
        }
      }
    };

    // Remove "_BETA" tags when in framework
    Intent intent = new Intent("com.google.intent.action.START_TTS_SERVICE_BETA");
    intent.addCategory("com.google.intent.category.TTS_BETA");
    mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    // TODO handle case where the binding works (should always work) but
    // the plugin fails
  }


  /**
   * Releases the resources used by the TextToSpeech engine. It is good practice
   * for instance to call this method in the onDestroy() method of an Activity
   * so the TextToSpeech engine can be cleanly stopped.
   */
  public void shutdown() {
    if (!ttsBetaInstalled) {
      Log.d("TextToSpeechBetaExt", USING_PLATFORM_TTS + "shutdown");
      super.shutdown();
      return;
    }
    try {
      mContext.unbindService(mServiceConnection);
    } catch (IllegalArgumentException e) {
      // Do nothing and fail silently since an error here indicates that
      // binding never succeeded in the first place.
    }
  }


  /**
   * Adds a mapping between a string of text and a sound resource in a package.
   * After a call to this method, subsequent calls to
   * {@link #speak(String, int, HashMap)} will play the specified sound resource
   * if it is available, or synthesize the text it is missing.
   * 
   * @param text The string of text. Example: <code>"south_south_east"</code>
   * 
   * @param packagename Pass the packagename of the application that contains
   *        the resource. If the resource is in your own application (this is
   *        the most common case), then put the packagename of your application
   *        here.<br/>
   *        Example: <b>"com.google.marvin.compass"</b><br/>
   *        The packagename can be found in the AndroidManifest.xml of your
   *        application.
   *        <p>
   *        <code>&lt;manifest xmlns:android=&quot;...&quot;
   *      package=&quot;<b>com.google.marvin.compass</b>&quot;&gt;</code>
   *        </p>
   * 
   * @param resourceId Example: <code>R.raw.south_south_east</code>
   * 
   * @return Code indicating success or failure. See {@link #ERROR} and
   *         {@link #SUCCESS}.
   */
  public int addSpeech(String text, String packagename, int resourceId) {
    if (!ttsBetaInstalled) {
      Log.d("TextToSpeechBetaExt", USING_PLATFORM_TTS + "addSpeech");
      return super.addSpeech(text, packagename, resourceId);
    }
    synchronized (mStartLock) {
      if (!mStarted) {
        return ERROR;
      }
      try {
        mITts.addSpeech(mPackageName, text, packagename, resourceId);
        return SUCCESS;
      } catch (RemoteException e) {
        // TTS died; restart it.
        Log.e("TextToSpeech.java - addSpeech", "RemoteException");
        e.printStackTrace();
        mStarted = false;
        initTts();
      } catch (NullPointerException e) {
        // TTS died; restart it.
        Log.e("TextToSpeech.java - addSpeech", "NullPointerException");
        e.printStackTrace();
        mStarted = false;
        initTts();
      } catch (IllegalStateException e) {
        // TTS died; restart it.
        Log.e("TextToSpeech.java - addSpeech", "IllegalStateException");
        e.printStackTrace();
        mStarted = false;
        initTts();
      }
      return ERROR;
    }
  }
}

