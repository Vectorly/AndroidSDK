package io.dotlearn.lrnquestion;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import io.dotlearn.lrnquestion.utils.Logger;

public class QuizFragment extends Fragment {

    private static final String ARG_QUIZ_ID = "ARG_QUIZ_ID";

    private QuizDataWrapper mQuizModule;
    private WebView mWebView;
    private View mProgressView;
    private View mErrorView;
    private OnFragmentInteractionListener mListener;
    private String mQuizId;
    private QuizFetchTask quizFetchTask = new QuizFetchTask();
    private Gson gson = new Gson();

    public QuizFragment() {}

    public static QuizFragment newInstance(String quizId) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUIZ_ID, quizId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuizId = getArguments().getString(ARG_QUIZ_ID);
        if(savedInstanceState == null) {
            Logger.INSTANCE.d("Quiz id: " + mQuizId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressView = view.findViewById(R.id.progress_container);
        mErrorView = view.findViewById(R.id.error_container);
        mWebView = view.findViewById(R.id.webView);
        mWebView.setScrollContainer(false);

        loadQuiz();
    }

    private void loadQuiz() {
        quizFetchTask.fetchQuiz(mQuizId, new QuizFetchTask.QuizFetchCallback() {

            @Override
            public boolean isActive() {
                return isAdded();
            }

            @Override
            public void onQuizFetchStarted() {
                Logger.INSTANCE.d("onQuizFetchStarted");
                mProgressView.setVisibility(View.VISIBLE);
                mErrorView.setVisibility(View.GONE);
            }

            @Override
            public void onQuizFetched(@NotNull QuizData quiz) {
                Logger.INSTANCE.d("onQuizFetched");
                mProgressView.setVisibility(View.GONE);
                mQuizModule = new QuizDataWrapper(quiz);
                initWebView();
                mListener.onQuizDataLoaded(quiz, QuizFragment.this);
            }

            @Override
            public void onQuizFetchError() {
                Logger.INSTANCE.d("onQuizFetchError");
                mProgressView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
            }

        });
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        dotLearnInterface dli = new dotLearnInterface(getActivity(), mWebView);
        mWebView.addJavascriptInterface(dli, "Android");

        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                String jUrl = "javascript:dotLearnLib.api.load(" + gson.toJson(mQuizModule) + ")";
                Logger.INSTANCE.d(jUrl);
                view.loadUrl(jUrl);
            }


        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Logger.INSTANCE.d(".__MyApplication" + message + " -- From line "
                        + lineNumber + " of " + sourceID);
            }
        });

        mWebView.loadUrl("file:///android_asset/modules/quiz/base.html");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        mWebView.saveState(savedInstanceState);
    }

    public class dotLearnInterface {

        final Context mContext;
        final WebView webView;


        dotLearnInterface(Context c, final WebView webView) {
            mContext = c;
            this.webView = webView;
        }


        public void onNext() {
            Toast.makeText(getActivity(), "onNext called", Toast.LENGTH_LONG).show();
            final JSONObject toSend = new JSONObject();

            try {
                toSend.put("data", mQuizModule);
            } catch (JSONException e) {
                e.printStackTrace();
                Logger.INSTANCE.e(e.getMessage());
            }
            Logger.INSTANCE.d("javascript:dotLearnLib.api.next(" + toSend.toString() + ")");
            webView.loadUrl("javascript:dotLearnLib.api.next(" + toSend.toString() + ")");

        }

        public void onCheck() {
            //No longer used
        }

        @JavascriptInterface
        public void setUser(final String userdata) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Logger.INSTANCE.d("Practice Quiz Option Selected. Response: " + userdata);

                        JSONObject userResponse = new JSONObject(userdata);
                        mListener.onOptionSelected(mQuizModule.data, userResponse.getInt("response"),
                                QuizFragment.this);
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "An unknown error occurred. Please try again", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    public void showAnswerAndExplanation() {
        Logger.INSTANCE.d("Practice Quiz Completed. Quiz prompt: " + mQuizModule.data.prompt);
        mWebView.loadUrl("javascript:dotLearnLib.api.check(" + gson.toJson(mQuizModule) + ")");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else if(getParentFragment() instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) getParentFragment();
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onQuizDataLoaded(QuizData quizData, QuizFragment quizFragment);
        void onOptionSelected(QuizData quizData, int userSelectedOption, QuizFragment quizFragment);
    }

    class QuizDataWrapper {

        public QuizData data;

        QuizDataWrapper(QuizData quiz) {
            data = quiz;
        }

    }

}
